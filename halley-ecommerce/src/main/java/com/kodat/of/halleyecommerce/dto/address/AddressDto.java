package com.kodat.of.halleyecommerce.dto.address;

import com.kodat.of.halleyecommerce.address.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {

    private Long id;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "District is mandatory")
    private String district;

    @NotBlank(message = "Neighborhood is mandatory")
    private String neighborhood;

    @NotBlank(message = "General address is mandatory")
    private String generalAddress;

    @NotBlank(message = "Zip code is mandatory")
    @Pattern(regexp = "\\d{5}", message = "Zip code must be 5 digits")
    private String zipCode;


    @NotNull( message = "AddressType is mandatory")
    private AddressType addressType;


}
