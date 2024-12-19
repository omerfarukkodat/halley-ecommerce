package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.cart.Cart;
import com.kodat.of.halleyecommerce.cart.CartRepository;
import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationEmailDto;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
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

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper, CartRepository cartRepository, AuthenticatedCartUtils authenticatedCartUtils, RegistrationEmailUtils registrationEmailUtils, RegistrationEmailProducer registrationEmailProducer) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.cartRepository = cartRepository;
        this.authenticatedCartUtils = authenticatedCartUtils;
        this.registrationEmailUtils = registrationEmailUtils;
        this.registrationEmailProducer = registrationEmailProducer;
    }

    @RateLimiter(name = "registerRateLimiter", fallbackMethod = "registerFallBack")
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

    public void registerFallBack(
            RegistrationRequest request
    ) {
        LOGGER.warn("Too many registration attempts for email: {}", request.getEmail());
        throw new RateLimiterAttemptException("Too many registration attempts.You have exceeded the maximum number of attempts.Please try again 10 minutes later.");
    }


    private void checkIfUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists in the system");
        }
    }
    @RateLimiter(name = "loginRateLimiter", fallbackMethod = "loginFallBack")
    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());
        String token = authenticationAndGenerateToken(request, user);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication connectedUser = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(connectedUser);
        if (!user.getRole().equals(Role.ADMIN)) {
            authenticatedCartUtils.mergeCarts(connectedUser);
        }
        LOGGER.info("Authenticated user: {}", user.getEmail());
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
            LOGGER.error("Authentication error user id: {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    public AuthenticationResponse loginFallBack(
            LoginRequest request, Throwable throwable
    ){
        LOGGER.warn("Too many login attempts for email: {}", request.getEmail());
        throw new RateLimiterAttemptException("Too many login attempts.You have exceeded the maximum number of attempts.Please try again 10 minutes later.");
    }
}
