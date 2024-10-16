package com.kodat.of.halleyecommerce.cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductIdAndCartId(Long productId, Long id);

    void deleteAllByCartId(Long id);
}
