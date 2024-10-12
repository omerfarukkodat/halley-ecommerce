package com.kodat.of.halleyecommerce.user;

import com.kodat.of.halleyecommerce.dto.user.UserProfileDto;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserProfileDto getProfile(Authentication connectedUser);
}
