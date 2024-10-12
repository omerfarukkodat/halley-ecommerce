package com.kodat.of.halleyecommerce.dto.user;

import com.kodat.of.halleyecommerce.adress.Adress;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Adress> adresses;
}
