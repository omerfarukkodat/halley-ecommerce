package com.kodat.of.halleyecommerce.mapper.address;


import com.kodat.of.halleyecommerce.adress.Address;
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
                .street(addressDto.getStreet())
                .zipCode(addressDto.getZipCode())
                .buildingNumber(addressDto.getBuildingNumber())
                .floor(addressDto.getFloor())
                .apartmentNumber(addressDto.getApartmentNumber())
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
                .street(address.getStreet())
                .zipCode(address.getZipCode())
                .buildingNumber(address.getBuildingNumber())
                .floor(address.getFloor())
                .apartmentNumber(address.getApartmentNumber())
                .build();
    }
}
