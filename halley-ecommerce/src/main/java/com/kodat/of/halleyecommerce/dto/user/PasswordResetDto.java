package com.kodat.of.halleyecommerce.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordResetDto {

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,15}$",
            message = "Password must be 8-15 characters long, contain at least one uppercase letter, one lowercase letter, and one digit."
    )
    private String newPassword;

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,15}$",
            message = "Password must be 8-15 characters long, contain at least one uppercase letter, one lowercase letter, and one digit."
    )
    private String confirmPassword;
}
