package com.kodat.of.halleyecommerce.product;


import com.kodat.of.halleyecommerce.cart.CartItem;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.discount.Discount;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products",
        indexes = {
        @Index(name = "idx_product_code", columnList = "product_code", unique = true),
        @Index(name = "idx_product_discounted_price", columnList = "discounted_price"),
        @Index(name = "idx_product_featured_created", columnList = "is_featured, created_time")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , length = 150)
    private String name;
    @Column(nullable = false , length = 1000)
    private String description;
    @Column(nullable = false , unique = true , length = 50)
    private String productCode;
    @Column(nullable = false , precision = 10, scale = 2)
    private BigDecimal originalPrice;
    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal discountedPrice;
    @Column(nullable = false)
    private Integer stock;
    @ManyToMany
    @JoinTable(name = "product_category",
           joinColumns = @JoinColumn(name = "product_id"),
           inverseJoinColumns = @JoinColumn(name = "category_id"),
            indexes = {
                    @Index(name = "idx_product_category_product_id", columnList = "product_id"),
                    @Index(name = "idx_product_category_category_id", columnList = "category_id")
            })
    private Set<Category> categories;
    @Column(unique = true)
    private String slug;
    private boolean isFeatured = false;
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL )
    private List<CartItem> cartItem;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
    private String imageUrl;
    @CreationTimestamp()
    @Column(updatable = false , nullable = false)
    private LocalDateTime createdTime;
    @UpdateTimestamp()
    private LocalDateTime updatedTime;


}
