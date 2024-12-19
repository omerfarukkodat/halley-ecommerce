package com.kodat.of.halleyecommerce.config;

import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.user.UserRepository;
import com.kodat.of.halleyecommerce.user.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.admin.email}")
    private String email;
    @Value("${spring.admin.password}")
    private String password;


    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initAdminUser(){
        return args -> {
            // If admin exist , don't create again
            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = User.builder()
                        .firstName("Faruk")
                        .lastName("Kodat")
                        .password(passwordEncoder.encode(password))
                        .email(email)
                        .enabled(true)
                        .accountLocked(false)
                        .phone("+905468732334")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };

    }


}
