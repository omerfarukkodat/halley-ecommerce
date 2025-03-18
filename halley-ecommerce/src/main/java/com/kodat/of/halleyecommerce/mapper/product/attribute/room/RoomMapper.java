package com.kodat.of.halleyecommerce.mapper.product.attribute.room;

import com.kodat.of.halleyecommerce.dto.product.attribute.room.RoomDto;
import com.kodat.of.halleyecommerce.mapper.product.attribute.colour.ColourMapper;
import com.kodat.of.halleyecommerce.product.attribute.room.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {

    public static Room toRoom(RoomDto roomDto) {

        return  Room.builder()
                .name(roomDto.getName())
                .description(roomDto.getDescription())
                .imageUrl(roomDto.getImageUrl())
                .build();
    }

    public static RoomDto toRoomDto(Room room) {

        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .slug(room.getSlug())
                .description(room.getDescription())
                .imageUrl(room.getImageUrl())
                .build();
    }

    public static List<RoomDto> roomDtoList(List<Room> rooms) {

        return rooms.stream()
                .map(RoomMapper::toRoomDto)
                .collect(Collectors.toList());

    }
}
