package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.adress.Address;
import com.kodat.of.halleyecommerce.adress.AddressRepository;
import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressValidator {
    private final AddressRepository addressRepository;

    public AddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    public void validateUserAddress(Integer userId){
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new AddressNotFoundException("User has no registered address"));
    }
    public void validateUserAddresses(Integer userId){
        List<Address> addresses = addressRepository.findAllByUserId(userId)
                .orElseThrow(() -> new AddressNotFoundException("User has no registered addresses"));
    }

}
