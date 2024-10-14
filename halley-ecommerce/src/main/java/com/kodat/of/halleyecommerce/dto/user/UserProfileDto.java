package com.kodat.of.halleyecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileDto {

    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @Email(message = "Email is not formatted")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

}
