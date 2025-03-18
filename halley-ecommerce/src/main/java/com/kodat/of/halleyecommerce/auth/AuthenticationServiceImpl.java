package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationEmailDto;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
import com.kodat.of.halleyecommerce.exception.BadCredentialsException;
import com.kodat.of.halleyecommerce.exception.RateLimiterAttemptException;
import com.kodat.of.halleyecommerce.user.CustomUserDetails;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.exception.UserAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.auth.UserMapper;
import com.kodat.of.halleyecommerce.user.UserRepository;
import com.kodat.of.halleyecommerce.security.JwtService;
import com.kodat.of.halleyecommerce.user.enums.Role;
import com.kodat.of.halleyecommerce.util.AuthenticatedCartUtils;
import com.kodat.of.halleyecommerce.email.RegistrationEmailUtils;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final CartRepository cartRepository;
    private final AuthenticatedCartUtils authenticatedCartUtils;
    private final RegistrationEmailUtils registrationEmailUtils;
    private final RegistrationEmailProducer registrationEmailProducer;


  //  @RateLimiter(name = "registerRateLimiter", fallbackMethod = "registerFallBack")
  @Override
  public void register(RegistrationRequest request) {
      checkIfUserExists(request.getEmail());
      var user = userMapper.toUser(request);
      userRepository.save(user);
      Cart cart = new Cart();
      cart.setUser(user);
      cartRepository.save(cart);
      LOGGER.info("User with email: {} registered", request.getEmail());
      RegistrationEmailDto registrationEmailDto = registrationEmailUtils.setRegistrationEmailDto(user);
      registrationEmailProducer.sendRegistrationEmail(registrationEmailDto);
  }

    private void checkIfUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists in the system");
        }
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());
        String token = authenticationAndGenerateToken(request, user);

        applyRateLimiter(request.getEmail());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication connectedUser = new UsernamePasswordAuthenticationToken
                (customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(connectedUser);

        if (!user.getRole().equals(Role.ADMIN)) {
            authenticatedCartUtils.mergeCarts(connectedUser);
            LOGGER.info("User with email: {} successfully merged cart", request.getEmail());
        }

        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public AuthenticationResponse adminLogin(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new BadCredentialsException("Only admins can access this endpoint.");
        }

        String token = authenticationAndGenerateToken(request, user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication connectedUser = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(connectedUser);

        return AuthenticationResponse.builder().token(token).build();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " does not exist."));
    }

    private String authenticationAndGenerateToken(LoginRequest loginRequest, User user) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            var claims = new HashMap<String, Object>();
            claims.put("email", user.getEmail());
            var customUserDetails = (CustomUserDetails) auth.getPrincipal();
            return jwtService.generateToken(claims, customUserDetails);
        } catch (AuthenticationException e) {
            LOGGER.error("Authentication error for email: {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new BadCredentialsException("Authentication failed: " + e.getMessage());
        }
    }

    @RateLimiter(name = "loginRateLimiter", fallbackMethod = "loginFallBack")
    private void applyRateLimiter(String email) {
        LOGGER.info("Rate limiter applied for email: {}", email);
    }

    public AuthenticationResponse loginFallBack(String email, Throwable throwable) {
        LOGGER.warn("Too many login attempts for email: {}", email);
        throw new RateLimiterAttemptException("Too many login attempts. You have exceeded the maximum number of attempts." +
                " Please try again later.");
    }
}