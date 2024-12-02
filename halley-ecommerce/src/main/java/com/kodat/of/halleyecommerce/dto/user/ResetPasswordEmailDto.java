package com.kodat.of.halleyecommerce.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordEmailDto {
    private String firstName;
    private String lastName;
    private String resetLink;
    @Email(message = "Email must formatted")
    private String email;
}
