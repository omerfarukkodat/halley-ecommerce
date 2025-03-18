package com.kodat.of.halleyecommerce.product.attribute.design;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "designs", indexes = {
        @Index(name = "idx_design_id", columnList = "id"),
        @Index(name = "idx_design_name", columnList = "name", unique = true),
        @Index(name = "idx_design_slug", columnList = "slug")})

public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String name;

    private String slug;

    @Size(max = 800)
    private String description;

    private String imageUrl;

}
