package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id" , nullable = false)
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;
    @Column(nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

}
