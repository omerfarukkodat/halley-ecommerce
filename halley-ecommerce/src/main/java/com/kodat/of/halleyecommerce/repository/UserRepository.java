package com.kodat.of.halleyecommerce.repository;

import com.kodat.of.halleyecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
