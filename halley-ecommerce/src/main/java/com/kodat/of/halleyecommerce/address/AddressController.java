package com.kodat.of.halleyecommerce.address;

import com.kodat.of.halleyecommerce.dto.address.AddressDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Address")
@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @Secured("USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createAddress(@Valid @RequestBody AddressDto addressDto, Authentication connectedUser) {
         addressService.createAddress(addressDto, connectedUser);
        return new ResponseEntity<>("Address created successfully.",HttpStatus.CREATED);
    }

    @Secured("USER")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId, Authentication connectedUser) {
        return ResponseEntity.ok(addressService.getAddressById(addressId, connectedUser));
    }

    @Secured("USER")
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses(Authentication connectedUser) {
        return ResponseEntity.ok(addressService.getAllAddresses(connectedUser));
    }

    @Secured("USER")
    @PutMapping("/{addressId}")
    public ResponseEntity<String> updateAddressById(
            @PathVariable Long addressId, @Valid @RequestBody AddressDto addressDto, Authentication connectedUser) {
        addressService.updateAddressById(addressId, addressDto,connectedUser);
        return ResponseEntity.ok("Address updated successfully" );
    }

    @Secured("USER")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long addressId, Authentication connectedUser) {
        addressService.deleteAddressById(addressId, connectedUser);
        return ResponseEntity.noContent().build();
    }

}
