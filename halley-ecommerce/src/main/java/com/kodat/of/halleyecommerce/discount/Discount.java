package com.kodat.of.halleyecommerce.discount;


import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal discountPercentage;

    @Column(updatable = false , nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<Product> products;

}
