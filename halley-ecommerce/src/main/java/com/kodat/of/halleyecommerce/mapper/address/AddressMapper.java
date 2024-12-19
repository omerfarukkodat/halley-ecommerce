package com.kodat.of.halleyecommerce.mapper.address;


import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.dto.address.AddressDto;

public class AddressMapper {
    public static Address toAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return Address.builder()
                .id(addressDto.getId())
                .city(addressDto.getCity())
                .district(addressDto.getDistrict())
                .neighborhood(addressDto.getNeighborhood())
                .generalAddress(addressDto.getGeneralAddress())
                .zipCode(addressDto.getZipCode())
                .addressType(addressDto.getAddressType())
                .build();
    }

    public static AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .district(address.getDistrict())
                .neighborhood(address.getNeighborhood())
                .generalAddress(address.getGeneralAddress())
                .zipCode(address.getZipCode())
                .addressType(address.getAddressType())
                .build();
    }

    public static Address updateAddressToDto(Address existingAddress , AddressDto addressDto) {
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setDistrict(addressDto.getDistrict());
        existingAddress.setNeighborhood(addressDto.getNeighborhood());
        existingAddress.setGeneralAddress(addressDto.getGeneralAddress());
        existingAddress.setZipCode(addressDto.getZipCode());
        existingAddress.setAddressType(addressDto.getAddressType());
        return existingAddress;
    }
}
