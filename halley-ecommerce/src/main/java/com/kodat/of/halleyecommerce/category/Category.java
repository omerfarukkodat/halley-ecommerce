package com.kodat.of.halleyecommerce.category;

import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false ,unique = true)
    private String categoryName;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent; // Reference to upper category
    @OneToMany(mappedBy = "parent" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Category> subCategories = new ArrayList<>();

    @ManyToMany(mappedBy = "categories",cascade = CascadeType.ALL)
    private Set<Product> products;
    @Column(unique = true)
    private String slug;
}
