package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NonMemberInfoDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDto address;
}
