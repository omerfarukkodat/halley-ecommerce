package com.kodat.of.halleyecommerce.discount;


import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal discountPercentage;
    @CreationTimestamp
    @Column(updatable = false , nullable = false)
    private LocalDateTime startDate;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime endDate;
    @OneToMany(mappedBy = "discount")
    private List<Product> products;

}
