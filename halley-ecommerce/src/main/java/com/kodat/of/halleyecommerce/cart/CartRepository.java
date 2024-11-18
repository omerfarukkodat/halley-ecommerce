package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_Email(String username);
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByCartToken(String cartToken);
    List<Cart> findAllByCartTokenIsNotNullAndCreatedAtBefore(LocalDateTime createdAt);
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cartToken IN :cartTokens")
    void deleteAllByCartTokens(@Param("cartTokens") List<String> expiredCartTokens);
}
