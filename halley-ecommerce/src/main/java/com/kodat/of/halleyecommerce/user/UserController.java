package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Secured("USER")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication connectedUser){
    return ResponseEntity.ok(userService.getProfile(connectedUser));
    }

}
