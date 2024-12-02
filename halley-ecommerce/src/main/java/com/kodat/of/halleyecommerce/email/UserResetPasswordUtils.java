package com.kodat.of.halleyecommerce.email;

import com.kodat.of.halleyecommerce.dto.user.ResetPasswordEmailDto;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserResetPasswordUtils {
    private final EmailService emailService;

    public UserResetPasswordUtils(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendEmailResetPassword(ResetPasswordEmailDto resetPasswordEmailDto) {
        Map<String , Object> data = new HashMap<>();
        data.put("firstName", resetPasswordEmailDto.getFirstName());
        data.put("lastName", resetPasswordEmailDto.getLastName());
        data.put("resetLink", resetPasswordEmailDto.getResetLink());
        emailService.sendPasswordResetEmail(resetPasswordEmailDto.getEmail(), data);
    }

    public ResetPasswordEmailDto sendResetPasswordEmail(User user, String resetLink) {
        return ResetPasswordEmailDto.builder()
                .resetLink(resetLink)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
