package com.kodat.of.halleyecommerce.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT b FROM  Brand b WHERE b.name ILIKE :brandName")
    Optional<Brand> findBrandByName(@Param("brandName") String brandName);
}
