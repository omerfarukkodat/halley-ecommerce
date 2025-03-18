package com.kodat.of.halleyecommerce.product.attribute.room;


import com.kodat.of.halleyecommerce.dto.product.attribute.room.RoomDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomDto>> findAll() {

        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> findById(@PathVariable Long id) {

        return ResponseEntity.ok(roomService.findById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<RoomDto> findBySlug(@PathVariable String slug) {

        return ResponseEntity.ok(roomService.findBySlug(slug));
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<RoomDto> add(@RequestBody @Valid RoomDto roomDto, Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.add(roomDto,connectedUser));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication connectedUser) {

        roomService.deleteById(id,connectedUser);
        return ResponseEntity.noContent().build();
    }
}
