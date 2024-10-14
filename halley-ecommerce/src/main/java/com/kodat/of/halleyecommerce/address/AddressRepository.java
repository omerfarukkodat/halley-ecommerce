package com.kodat.of.halleyecommerce.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<List<Address>> findAllByUserId(Integer user_id);

    Optional<Address> findFirstByUserId(Integer user_id);

    Optional<Address> findByIdAndUserId(Long addressId, Integer user_id);
}
