package com.kodat.of.halleyecommerce.product.attribute.colour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ColourRepository extends JpaRepository<Colour, Long> {

    @Query("SELECT c FROM  Colour c WHERE c.name ILIKE :name")
    Optional<Colour> findByName(String name);


    Optional<Colour> findBySlug(String slug);
}
