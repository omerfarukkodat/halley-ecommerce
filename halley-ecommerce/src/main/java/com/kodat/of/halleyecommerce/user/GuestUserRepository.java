package com.kodat.of.halleyecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestUserRepository extends JpaRepository<GuestUser, Long> {
    Optional<GuestUser> findByEmail(String email);
}
