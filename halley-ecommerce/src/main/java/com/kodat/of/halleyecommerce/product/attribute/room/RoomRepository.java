package com.kodat.of.halleyecommerce.product.attribute.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM  Room r WHERE r.name ILIKE :name")
    Optional<Room> findByName(String name);

    Optional<Room> findBySlug(String slug);
}

