package com.kodat.of.halleyecommerce.product.attribute.design;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DesignRepository extends JpaRepository<Design, Long> {

    @Query("SELECT d FROM Design d WHERE d.name ILIKE :name")
    Optional<Design> findByName(String name);

    Optional<Design> findBySlug(String slug);
}
