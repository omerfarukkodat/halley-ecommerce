package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.PasswordResetDto;
import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserProfileDto getProfile(Authentication connectedUser);

    UserProfileDto updateProfile(UserProfileDto profileDto, Authentication connectedUser);

    void sendPasswordResetEmail(String email);

    void resetPassword(String token, PasswordResetDto passwordResetDto);

    boolean isTokenValid(String token);
}
