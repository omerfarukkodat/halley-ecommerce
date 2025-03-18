package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.exception.DuplicatePasswordException;
import com.kodat.of.halleyecommerce.exception.InvalidTokenException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenService {
    private static final long EXPIRATION_TIME = 60000;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public String generateResetTokenForUser(User user) {
        String tokenData = user.getEmail() + ":" + System.currentTimeMillis();
        String token = hashToken(tokenData);

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiryDate(Instant.now().plusMillis(EXPIRATION_TIME));
        userRepository.save(user);
        return token;
    }

    private String hashToken(String tokenData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tokenData.getBytes());
            return Base64.getUrlEncoder().encodeToString(hash);
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing token", e);
        }
    }
    public boolean validateResetToken(String token , String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return isTokenValid(user,token);
        }
        return false;
    }
    public boolean isTokenValid(User user, String token) {
        if (user.getResetPasswordExpiryDate() == null || user.getResetPasswordExpiryDate().isBefore(Instant.now())){
            return false;
        }
        return user.getResetPasswordToken().equals(token);
    }
    public void resetPassword(String newPassword, String token , String email) {
        if (!validateResetToken(token,email)){
            throw new InvalidTokenException("Invalid or expired token");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));
        if (passwordEncoder.matches(newPassword,user.getPassword())) {
            throw new DuplicatePasswordException("You cannot use existing password.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiryDate(null);
        userRepository.save(user);

    }

}
