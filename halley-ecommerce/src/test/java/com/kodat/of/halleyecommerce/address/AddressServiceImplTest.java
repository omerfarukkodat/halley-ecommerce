package com.kodat.of.halleyecommerce.address;

import com.kodat.of.halleyecommerce.address.enums.AddressType;
import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import com.kodat.of.halleyecommerce.exception.AddressNotFoundException;
import com.kodat.of.halleyecommerce.exception.UserNotFoundException;
import com.kodat.of.halleyecommerce.mapper.address.AddressMapper;
import com.kodat.of.halleyecommerce.user.User;
import com.kodat.of.halleyecommerce.user.UserRepository;
import com.kodat.of.halleyecommerce.user.enums.Role;
import com.kodat.of.halleyecommerce.validator.AddressValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @InjectMocks
    private AddressServiceImpl addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressValidator addressValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleValidator roleValidator;
    private AddressDto addressDto;
    private User user;
    private Address address;
    private Authentication mockAuth;

    @BeforeEach
    void setUp() {
        addressDto = AddressDto.builder()
                .id(1L)
                .city("İstanbul")
                .district("Üsküdar")
                .neighborhood("Altunizade")
                .generalAddress("Trablus Sokağı")
                .zipCode("34200")
                .addressType(AddressType.HOME)
                .build();
        user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .password("password")
                .enabled(true)
                .phone("1234567890")
                .role(Role.USER)
                .accountLocked(false)
                .addresses(new ArrayList<>())
                .build();

        address = AddressMapper.toAddress(addressDto);
        address.setUser(user);
        user.setAddresses(List.of(address));

        mockAuth = mock(Authentication.class);
    }

    @Test
    void when_createAddress_validInput_addressIsAdded() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto result = addressService.createAddress(addressDto,mockAuth);

        assertNotNull(result);
        assertEquals(addressDto.getId(), result.getId());
        assertEquals(addressDto.getCity(), result.getCity());
        assertEquals(addressDto.getDistrict(), result.getDistrict());
        assertEquals(addressDto.getNeighborhood(), result.getNeighborhood());
        assertEquals(addressDto.getGeneralAddress(), result.getGeneralAddress());
        assertEquals(addressDto.getZipCode(), result.getZipCode());
        assertEquals(addressDto.getAddressType(), result.getAddressType());
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressRepository).save(any(Address.class));
    }
    @Test
    void when_createAddress_userHasNoExistsEmail_thenThrowUserNotFoundException() {
        when(mockAuth.getName()).thenReturn(user.getEmail());
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        user.setEmail("noneexist@gmail.com");
        assertThrows(UserNotFoundException.class,() -> addressService.createAddress(addressDto,mockAuth));
        verify(roleValidator).verifyUserRole(mockAuth);
    }
    @Test
    void when_getAllAddresses_validInput_addressIsReturned() {
    doNothing().when(roleValidator).verifyUserRole(mockAuth);
    when(mockAuth.getName()).thenReturn(user.getEmail());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    doNothing().when(addressValidator).validateUserAddresses(user.getId());
    List<Address> addresses = List.of(address);
    when(addressRepository.findAllByUserId(user.getId())).thenReturn(Optional.of(addresses));
    List<AddressDto> response = addressService.getAllAddresses(mockAuth);
    assertNotNull(response);
    assertEquals(addressDto.getId(), response.getFirst().getId());
    assertEquals(addressDto.getCity(), response.getFirst().getCity());
    assertEquals(addressDto.getDistrict(), response.getFirst().getDistrict());
    assertEquals(addressDto.getNeighborhood(), response.getFirst().getNeighborhood());
    assertEquals(addressDto.getGeneralAddress(), response.getFirst().getGeneralAddress());
    assertEquals(addressDto.getZipCode(), response.getFirst().getZipCode());
    assertEquals(addressDto.getAddressType(), response.getFirst().getAddressType());
    verify(roleValidator).verifyUserRole(mockAuth);
    verify(userRepository).findByEmail(user.getEmail());
    verify(addressRepository).findAllByUserId(user.getId());
    verify(addressValidator).validateUserAddresses(user.getId());

    }
    @Test
    void when_getAllAddresses_userNotFound_thenThrowUserNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> addressService.getAllAddresses(mockAuth));
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
    }
    @Test
    void when_getAllAddresses_addressNotFound_thenThrowAddressNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(addressValidator).validateUserAddresses(user.getId());
        when(addressRepository.findAllByUserId(user.getId())).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class,() -> addressService.getAllAddresses(mockAuth));
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddresses(user.getId());
        verify(addressRepository).findAllByUserId(user.getId());


    }
    @Test
    void when_getAddressById_validInput_addressIsReturned() {
        Long addressId = address.getId();
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(addressValidator).validateUserAddress(user.getId());
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        AddressDto response = addressService.getAddressById(addressId,mockAuth);
        assertNotNull(response);
        assertEquals(addressDto.getId(), response.getId());
        assertEquals(addressDto.getCity(), response.getCity());
        assertEquals(addressDto.getDistrict(), response.getDistrict());
        assertEquals(addressDto.getNeighborhood(), response.getNeighborhood());
        assertEquals(addressDto.getGeneralAddress(), response.getGeneralAddress());
        assertEquals(addressDto.getZipCode(), response.getZipCode());
        assertEquals(addressDto.getAddressType(), response.getAddressType());
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(user.getId());
        verify(addressRepository).findById(addressId);





    }
    @Test
    void when_getAddressById_userNotFound_thenThrowUserNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> {
            addressService.getAddressById(address.getId(),mockAuth);
        });

        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());


    }
    @Test
    void when_getAddressById_addressNotFound_thenThrowAddressNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(addressValidator).validateUserAddress(user.getId());
        when(addressRepository.findById(address.getId()))
                .thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class,()->{
            addressService.getAddressById(address.getId(),mockAuth);
        });
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(user.getId());
        verify(addressRepository).findById(address.getId());
    }
    @Test
    void when_updateAddress_validInput_addressIsReturned() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(addressValidator).validateUserAddress(user.getId());
        when(addressRepository.findById(address.getId()))
                .thenReturn(Optional.of(address));
        AddressDto updatedAddressDto = AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .district(address.getDistrict())
                .generalAddress(address.getGeneralAddress())
                .zipCode(address.getZipCode())
                .addressType(address.getAddressType())
                .build();
     Address updatedAddress = AddressMapper.updateAddressToDto(address, updatedAddressDto);
        address.setUser(user);
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        AddressDto response = addressService.updateAddressById(address.getId(),updatedAddressDto,mockAuth);

        assertNotNull(response);
        assertEquals(updatedAddressDto.getId(), response.getId());
        assertEquals(updatedAddressDto.getCity(), response.getCity());
        assertEquals(updatedAddressDto.getDistrict(), response.getDistrict());
        assertEquals(updatedAddressDto.getGeneralAddress(), response.getGeneralAddress());
        assertEquals(updatedAddressDto.getZipCode(), response.getZipCode());
        assertEquals(updatedAddressDto.getAddressType(), response.getAddressType());
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(user.getId());
        verify(addressRepository).findById(address.getId());
    }
    @Test
    void when_updateAddress_userNotFound_thenThrowUserNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            addressService.updateAddressById(address.getId(),addressDto,mockAuth);
        });
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());

    }
    @Test
    void when_updateAddress_addressNotFound_thenThrowAddressNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(addressValidator).validateUserAddress(user.getId());
        when(addressRepository.findById(address.getId()))
        .thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class,()->{
            addressService.updateAddressById(address.getId(),addressDto,mockAuth);
        });
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(user.getId());
        verify(addressRepository).findById(address.getId());
    }
    @Test
    void when_deleteAddress_validInput_addressIsDeleted() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(addressValidator.validateUserAddress(address.getId(),user.getId()))
                .thenReturn(address);
        addressService.deleteAddressById(address.getId(),mockAuth);
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(address.getId(),user.getId());
       verify(addressRepository).delete(address);
    }
    @Test
    void when_deleteAddress_userNotFound_thenThrowUserNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->{
            addressService.deleteAddressById(address.getId(),mockAuth);
        });
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
    }
    @Test
    void when_deleteAddress_addressNotFound_thenThrowAddressNotFoundException() {
        doNothing().when(roleValidator).verifyUserRole(mockAuth);
        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(addressValidator.validateUserAddress(address.getId(),user.getId()))
                .thenThrow(new AddressNotFoundException("Address not found"));
        assertThrows(AddressNotFoundException.class,()->{
            addressService.deleteAddressById(address.getId(),mockAuth);
        });
        verify(roleValidator).verifyUserRole(mockAuth);
        verify(userRepository).findByEmail(user.getEmail());
        verify(addressValidator).validateUserAddress(address.getId(),user.getId());
        verify(addressRepository,never()).delete(address);

    }

}