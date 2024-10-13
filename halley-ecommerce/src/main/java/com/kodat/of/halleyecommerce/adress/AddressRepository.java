package com.kodat.of.halleyecommerce.adress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<List<Address>> findAllByUserId(Integer user_id);

    Optional<Address> findByUserId(Integer user_id);

}
