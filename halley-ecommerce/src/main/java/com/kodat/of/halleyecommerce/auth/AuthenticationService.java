package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;

public interface AuthenticationService {

    void register(RegistrationRequest request);
}
