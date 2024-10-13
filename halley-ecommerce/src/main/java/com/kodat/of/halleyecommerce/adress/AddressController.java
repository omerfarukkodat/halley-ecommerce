package com.kodat.of.halleyecommerce.adress;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;


    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    @Secured("USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddressDto> createAddress(
           @Valid @RequestBody AddressDto addressDto,
            Authentication connectedUser) {
        AddressDto createdAddress = addressService.createAddress(addressDto,connectedUser);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }


    @Secured("USER")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(
            @PathVariable Long addressId , Authentication connectedUser) {
        return ResponseEntity.ok(addressService.getAddressById(addressId,connectedUser));
    }

    @Secured("USER")
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses(Authentication connectedUser) {
        return ResponseEntity.ok(addressService.getAllAddresses(connectedUser));
    }


}
