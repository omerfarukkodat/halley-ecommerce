package com.kodat.of.halleyecommerce.address;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAllAddresses(Authentication connectedUser);

    AddressDto getAddressById(Long addressId, Authentication connectedUser);

    void createAddress(AddressDto addressDto, Authentication connectedUser);

    void updateAddressById(Long addressId, AddressDto addressDto, Authentication connectedUser);

    void deleteAddressById(Long addressId, Authentication connectedUser);
}
