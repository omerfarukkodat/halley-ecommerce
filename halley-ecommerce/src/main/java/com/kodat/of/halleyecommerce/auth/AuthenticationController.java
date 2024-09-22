package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ){
    service.register(request);
    return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
           @RequestBody @Valid LoginRequest request
    ){
        return ResponseEntity.ok(service.login(request));
    }




}
