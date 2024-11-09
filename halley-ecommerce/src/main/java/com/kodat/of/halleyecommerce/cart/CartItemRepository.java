package com.kodat.of.halleyecommerce.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductIdAndCartId(Long productId, Long id);
    void deleteAllByCartId(Long id);
    void deleteCartItemByCart_CartToken(String cartToken);
}
