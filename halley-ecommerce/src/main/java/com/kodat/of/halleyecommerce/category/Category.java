package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    private String ImageUrl;

    @OneToMany(mappedBy = "parent" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Category> subCategories = new ArrayList<>();

    @ManyToMany(mappedBy = "categories",cascade = CascadeType.ALL)
    private Set<Product> products;

    @Column(unique = true)
    private String slug;

    @Size(max = 800)
    private String description;


    @CreationTimestamp()
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp()
    private LocalDateTime updatedTime;

}
