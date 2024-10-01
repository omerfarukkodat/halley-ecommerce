package com.kodat.of.halleyecommerce.validator;

import com.kodat.of.halleyecommerce.exception.UnauthorizedAdminAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleValidator.class);

    public void verifyAdminRole(Authentication connectedAdmin) {
        if (connectedAdmin.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))){
            throw new UnauthorizedAdminAccessException("Only the main admin can perform this operation");
        }
        LOGGER.info("Admin role verified for user: {}", connectedAdmin.getName());
    }

}