package com.kodat.of.halleyecommerce.address;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.mapper.address.AddressMapper;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.user.UserRepository;
import com.kodat.of.halleyecommerce.validator.AddressValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final RoleValidator roleValidator;
    private final UserRepository userRepository;
    private final AddressValidator addressValidator;

    public AddressServiceImpl(AddressRepository addressRepository, RoleValidator roleValidator, UserRepository userRepository, AddressValidator addressValidator) {
        this.addressRepository = addressRepository;
        this.roleValidator = roleValidator;
        this.userRepository = userRepository;
        this.addressValidator = addressValidator;
    }

    @Override
    public List<AddressDto> getAllAddresses(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        addressValidator.validateUserAddresses(user.getId());
        List<Address> addresses = addressRepository.findAllByUserId(user.getId()).get();
        return  addresses.stream()
                .map(AddressMapper::toAddressDto)
                .toList();
    }

    @Override
    public AddressDto getAddressById(Long addressId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        addressValidator.validateUserAddress(user.getId());
        Address address = addressRepository.findById(addressId).get();
        return AddressMapper.toAddressDto(address);
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        Address newAddress = AddressMapper.toAddress(addressDto);
        newAddress.setUser(user);
        Address savedAddress = addressRepository.save(newAddress);
        return AddressMapper.toAddressDto(savedAddress);
    }

    @Override
    public AddressDto updateAddressById(Long addressId, AddressDto addressDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
        addressValidator.validateUserAddress(user.getId());
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));
        Address updatedAddress = addressRepository.save(AddressMapper.updateAddressToDto(existingAddress, addressDto));
        return AddressMapper.toAddressDto(updatedAddress);
    }
    @Transactional
    @Override
    public void deleteAddressById(Long addressId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username).get();
       Address address = addressValidator.validateUserAddress(addressId,user.getId());
        addressRepository.delete(address);
    }
}
