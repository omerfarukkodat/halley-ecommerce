package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
import com.kodat.of.halleyecommerce.entity.CustomUserDetails;
import com.kodat.of.halleyecommerce.entity.User;
import com.kodat.of.halleyecommerce.entity.enums.Role;
import com.kodat.of.halleyecommerce.exception.UserAlreadyExistsException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.repository.UserRepository;
import com.kodat.of.halleyecommerce.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public void register(RegistrationRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            LOGGER.warn("User with email {} already exists", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .phone(request.getPhone())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        LOGGER.info("User with email: {} registered", request.getEmail());
    }

    @Override
    public AuthenticationResponse login(
            LoginRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new UserNotFoundException("User Email: " + request.getEmail() + " address does not exist.");
        }
    try {
        var auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
        var claims = new HashMap<String,Object>();
        var customUserDetails = (CustomUserDetails) auth.getPrincipal();
        var user = customUserDetails.getUser();
        claims.put("username", user.getEmail());
        var jwtToken = jwtService.generateToken(claims,customUserDetails);
        LOGGER.info("Authenticated user: {}", user.getEmail());
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }catch (AuthenticationException e) {
        LOGGER.error("Authentication error user id: {}: {}",request.getEmail(), e.getMessage());
        throw new RuntimeException("Authentication failed"+ e.getMessage());
    }
        }
}
