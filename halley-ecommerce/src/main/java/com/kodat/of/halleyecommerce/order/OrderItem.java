package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @Min(value = 1 , message = "quantity must be at least 1")
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
