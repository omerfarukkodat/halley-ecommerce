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
                .street(address.getStreet())
                .zipCode(address.getZipCode())
                .buildingNumber(address.getBuildingNumber())
                .floor(address.getFloor())
                .apartmentNumber(address.getApartmentNumber())
                .addressType(address.getAddressType())
                .build();
    }

    public static Address updateAddressToDto(Address existingAddress , AddressDto addressDto) {
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setDistrict(addressDto.getDistrict());
        existingAddress.setNeighborhood(addressDto.getNeighborhood());
        existingAddress.setStreet(addressDto.getStreet());
        existingAddress.setZipCode(addressDto.getZipCode());
        existingAddress.setBuildingNumber(addressDto.getBuildingNumber());
        existingAddress.setFloor(addressDto.getFloor());
        existingAddress.setApartmentNumber(addressDto.getApartmentNumber());
        existingAddress.setAddressType(addressDto.getAddressType());
        return existingAddress;

    }
}
