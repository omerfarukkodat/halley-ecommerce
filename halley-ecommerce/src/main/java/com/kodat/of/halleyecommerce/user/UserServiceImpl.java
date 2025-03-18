package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.PasswordResetDto;
import com.kodat.of.halleyecommerce.dto.user.ResetPasswordEmailDto;
import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import com.kodat.of.halleyecommerce.email.EmailService;
import com.kodat.of.halleyecommerce.email.UserResetPasswordUtils;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.user.UserMapper;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;
    private final TokenService tokenService;
    private final UserResetPasswordProducer userResetPasswordProducer;
    private final UserResetPasswordUtils userResetPasswordUtils;

    public UserServiceImpl(UserRepository userRepository, RoleValidator roleValidator, TokenService tokenService, UserResetPasswordProducer userResetPasswordProducer, UserResetPasswordUtils userResetPasswordUtils) {
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
        this.tokenService = tokenService;
        this.userResetPasswordProducer = userResetPasswordProducer;
        this.userResetPasswordUtils = userResetPasswordUtils;
    }


    @Override
    public UserProfileDto getProfile(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        return UserMapper.toUserProfileDto(user);
    }

    @Override
    public UserProfileDto updateProfile(UserProfileDto userProfileDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        User updatedUser = UserMapper.updateUserFromDto(user, userProfileDto);
        userRepository.save(updatedUser);
        return UserMapper.toUserProfileDto(updatedUser);


}

@Override
public void sendPasswordResetEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    String token = tokenService.generateResetTokenForUser(user);
    // Create password reset link
    String resetLink = "http://localhost:3001/reset-password?token=" + token;
    ResetPasswordEmailDto resetPasswordEmailDto = userResetPasswordUtils.sendResetPasswordEmail(user, resetLink);
    userResetPasswordProducer.sendResetPasswordQueue(resetPasswordEmailDto);
}

@Override
public void resetPassword(String token, PasswordResetDto passwordResetDto) {
    User user = userRepository.findByResetPasswordToken(token)
            .orElseThrow(() -> new UserNotFoundException("User not found with token: " + token));

    if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmPassword())) {
        throw new IllegalArgumentException("Passwords do not match");
    }

    if (tokenService.isTokenValid(user, token)) {
        tokenService.resetPassword(passwordResetDto.getNewPassword(), token, user.getEmail());
        user.setResetPasswordToken(null);
    }
}

    @Override
    public boolean isTokenValid(String token) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new UserNotFoundException("User not found with token: " + token));
        return tokenService.isTokenValid(user, token);
    }


}
