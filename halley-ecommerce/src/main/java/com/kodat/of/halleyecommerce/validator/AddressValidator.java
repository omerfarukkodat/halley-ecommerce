package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.address.Address;
import com.kodat.of.halleyecommerce.address.AddressRepository;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressValidator {
    private final AddressRepository addressRepository;

    public AddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    public void validateUserAddress(Integer userId){
        Address address = addressRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new AddressNotFoundException("User has no registered address"));
    }
    public void validateUserAddresses(Integer userId){
        List<Address> addresses = addressRepository.findAllByUserId(userId)
                .orElseThrow(() -> new AddressNotFoundException("User has no registered addresses"));
    }
    public Address validateUserAddress(Long addressId , Integer userId){
        return addressRepository.findByIdAndUserId(addressId,userId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found for this user"));
    }

}
