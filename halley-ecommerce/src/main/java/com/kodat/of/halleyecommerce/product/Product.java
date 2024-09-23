package com.kodat.of.halleyecommerce.product;


import com.kodat.of.halleyecommerce.category.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , length = 100)
    private String name;
    @Column(nullable = false , length = 1000)
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String imageUrl;
    @CreationTimestamp()
    @Column(updatable = false , nullable = false)
    private LocalDateTime createdTime;
    @Column(nullable = false)
    @UpdateTimestamp()
    private LocalDateTime updatedTime;


}
