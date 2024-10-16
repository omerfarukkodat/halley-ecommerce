package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUser_Email(String username);

    Cart findByUser(User user);

}
