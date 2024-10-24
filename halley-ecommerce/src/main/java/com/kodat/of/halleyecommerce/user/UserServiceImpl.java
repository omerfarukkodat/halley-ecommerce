package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.user.UserMapper;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleValidator roleValidator, TokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
        this.tokenService = tokenService;
        this.emailService = emailService;
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
        User user = userRepository.findByEmail(username).get();
        User updatedUser = UserMapper.updateUserFromDto(user,userProfileDto);
        userRepository.save(updatedUser);
        return UserMapper.toUserProfileDto(updatedUser);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        String token = tokenService.generateResetTokenForUser(user);

        // Create password reset link
        String resetLink = "https://localhost:8088/reset-password?token=" + token;

        // Using email service , we send an email
        String subject = "Password Reset Request";
        String body = "<p>Dear " + user.getFirstName() + ",</p>"
                + "<p>We received a request to reset your password.</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href=\"" + resetLink + "\">Reset Password</a>"
                + "<p>If you did not request this, please ignore this email.</p>";
        emailService.sendEmail(user.getEmail(), subject, body);

    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                        .orElseThrow(()-> new UserNotFoundException("User not found with token: " + token));
        if (tokenService.isTokenValid(user,token)){
            tokenService.resetPassword(newPassword,token, user.getEmail());
        }
    }


}
