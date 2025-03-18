package com.kodat.of.halleyecommerce.address;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.address.AddressMapper;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.user.UserRepository;
import com.kodat.of.halleyecommerce.validator.AddressValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final RoleValidator roleValidator;
    private final UserRepository userRepository;
    private final AddressValidator addressValidator;


    @Override
    public List<AddressDto> getAllAddresses(Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);

        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        addressValidator.validateUserAddresses(user.getId());

        List<Address> addresses = addressRepository.findAllByUserIdAndIsDeletedFalse(user.getId())
                .orElseThrow(()->new AddressNotFoundException("Any addresses not found"));

        return  addresses.stream()
                .map(AddressMapper::toAddressDto)
                .toList();
    }

    @Override
    public AddressDto getAddressById(Long addressId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));

        addressValidator.validateUserAddress(user.getId());

        Address address = addressRepository.findByIdAndIsDeletedFalse(addressId)
                .orElseThrow(()->new AddressNotFoundException("Any address not found"));

        return AddressMapper.toAddressDto(address);
    }

    @Override
    public void createAddress(AddressDto addressDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        Address newAddress = AddressMapper.toAddress(addressDto);
        newAddress.setUser(user);
        newAddress.setIsDeleted(false);
        addressRepository.save(newAddress);
    }

    @Override
    public void updateAddressById(Long addressId, AddressDto addressDto, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        addressValidator.validateUserAddress(user.getId());

        Address existingAddress = addressRepository.findByIdAndIsDeletedFalse(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        addressRepository.save(AddressMapper.updateAddressToDto(existingAddress, addressDto));
    }
    @Transactional
    @Override
    public void deleteAddressById(Long addressId, Authentication connectedUser) {
        roleValidator.verifyUserRole(connectedUser);
        String username = connectedUser.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UserNotFoundException("User not found"));
       Address address = addressValidator.validateUserAddress(addressId,user.getId());
       address.setIsDeleted(true);
       addressRepository.save(address);
    }
}
