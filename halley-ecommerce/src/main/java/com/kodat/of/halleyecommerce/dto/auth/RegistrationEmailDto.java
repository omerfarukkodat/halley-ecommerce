package com.kodat.of.halleyecommerce.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationEmailDto {
    private String firstName;
    private String lastName;
    private String email;
}
