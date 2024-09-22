package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
import com.kodat.of.halleyecommerce.entity.User;
import com.kodat.of.halleyecommerce.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public void register(RegistrationRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .accountLocked(false)
                .enabled(true)
                .phone(request.getPhone())
                .build();

        userRepository.save(user);



    }
}
