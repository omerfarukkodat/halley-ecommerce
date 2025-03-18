package com.kodat.of.halleyecommerce.product.attribute.room;


import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.product.attribute.room.RoomDto;
import com.kodat.of.halleyecommerce.exception.BrandAlreadyExists;
import com.kodat.of.halleyecommerce.exception.ProductAttributeAlreadyExists;
import com.kodat.of.halleyecommerce.exception.ProductAttributeNotFound;
import com.kodat.of.halleyecommerce.mapper.product.attribute.room.RoomMapper;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final SlugService slugService;
    private final RoleValidator roleValidator;


    public List<RoomDto> findAll() {
        List<Room> rooms = roomRepository.findAll();
        return RoomMapper.roomDtoList(rooms);
    }

    public RoomDto findById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ProductAttributeNotFound("Room not found with this id: " + id));

        return RoomMapper.toRoomDto(room);
    }

    public RoomDto add(RoomDto roomDto, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        if (roomRepository.findByName(roomDto.getName()).isPresent()) {
            throw new ProductAttributeAlreadyExists("Room " + roomDto.getName() + " already exists");
        }
        String slug = slugService.generateSlugWithNoCode(roomDto.getName());

        Room room = RoomMapper.toRoom(roomDto);
        room.setSlug(slug);
        roomRepository.save(room);

        return RoomMapper.toRoomDto(room);

    }

    public void deleteById(Long id, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        if (roomRepository.findById(id).isPresent()) {
            roomRepository.deleteById(id);
        }

        throw new ProductAttributeNotFound("Room not found with this id: " + id);

    }

    public RoomDto findBySlug(String slug) {

        Room room = roomRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Room not found with this slug: " + slug));

        return RoomMapper.toRoomDto(room);
    }
}
