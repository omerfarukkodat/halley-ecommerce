package com.kodat.of.halleyecommerce.auth;

import com.kodat.of.halleyecommerce.dto.auth.AuthenticationResponse;
import com.kodat.of.halleyecommerce.dto.auth.LoginRequest;
import com.kodat.of.halleyecommerce.dto.auth.RegistrationRequest;

public interface AuthenticationService {

    void register(RegistrationRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse adminLogin(LoginRequest request);
}
