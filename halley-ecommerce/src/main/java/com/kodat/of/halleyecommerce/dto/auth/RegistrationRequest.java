package com.kodat.of.halleyecommerce.dto.auth;

import com.kodat.of.halleyecommerce.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private String middleName;  // Optional not mandatory
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Email(message = "Email is not formatted")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8 , message = "Password should be 8 characters long minimum")
    private String password;
    @NotBlank(message = "Phone number is mandatory")
    private String phone;
    private Role role = Role.USER;
}
