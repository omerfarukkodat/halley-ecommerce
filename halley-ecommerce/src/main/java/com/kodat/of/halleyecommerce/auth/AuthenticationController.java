package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user name , lastname , email , phone number and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials")})
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }


    @PostMapping("/admin/login")
    public ResponseEntity<AuthenticationResponse> adminLogin(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(service.adminLogin(request));
    }


    @GetMapping("/admin/check")
    @Secured("ADMIN")
    public ResponseEntity<Void> checkAdmin() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/check")
    @Secured("USER")
    public ResponseEntity<Void> checkUser() {
        return ResponseEntity.ok().build();
    }


}
