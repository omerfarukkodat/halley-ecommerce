package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_Email(String username);
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByCartToken(String cartToken);

}
