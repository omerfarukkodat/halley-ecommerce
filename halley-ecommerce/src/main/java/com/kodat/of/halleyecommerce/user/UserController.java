package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.PasswordResetDto;
import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("USER")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication connectedUser) {
        return ResponseEntity.ok(userService.getProfile(connectedUser));
    }

    @Secured("USER")
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UserProfileDto profileDto, Authentication connectedUser) {
        return ResponseEntity.ok(userService.updateProfile(profileDto, connectedUser));
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        userService.sendPasswordResetEmail(email);
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody @Valid PasswordResetDto passwordResetDto) {
        userService.resetPassword(token, passwordResetDto);
        return ResponseEntity.ok("Password has been reset successfully");
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = userService.isTokenValid(token);
        return ResponseEntity.ok(isValid);
    }

}
