package com.kodat.of.halleyecommerce.brand;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kodat.of.halleyecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
@Entity
public class Brand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column( unique = true, length = 100)
    private String slug;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "brand" , cascade = CascadeType.PERSIST)
    private List<Product> products;

}
