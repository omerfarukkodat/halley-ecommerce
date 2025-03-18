package com.kodat.of.halleyecommerce.dto.auth;

import com.kodat.of.halleyecommerce.cart.Cart;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Email is not formatted")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,15}$",
            message = "Password must be 8-15 characters long, contain at least one uppercase letter, one lowercase letter, and one digit."
    )
    private String password;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(
            regexp = "^\\+90\\d{10}$",
            message = "Phone number must start with +90 and followed by 10 digits"
    )
    private String phone;

}
