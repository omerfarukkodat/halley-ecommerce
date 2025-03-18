package com.kodat.of.halleyecommerce.product.attribute.colour;


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
@Table(name = "colours",indexes = {
        @Index(name = "idx_colour_id", columnList = "id"),
        @Index(name = "idx_colour_name", columnList = "name", unique = true),
        @Index(name = "idx_colour_slug", columnList = "slug")})
public class Colour {

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
