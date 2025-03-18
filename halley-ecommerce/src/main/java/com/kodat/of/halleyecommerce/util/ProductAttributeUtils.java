package com.kodat.of.halleyecommerce.util;

import com.kodat.of.halleyecommerce.exception.ProductAttributeNotFound;
import com.kodat.of.halleyecommerce.product.attribute.colour.Colour;
import com.kodat.of.halleyecommerce.product.attribute.colour.ColourRepository;
import com.kodat.of.halleyecommerce.product.attribute.design.Design;
import com.kodat.of.halleyecommerce.product.attribute.design.DesignRepository;
import com.kodat.of.halleyecommerce.product.attribute.room.Room;
import com.kodat.of.halleyecommerce.product.attribute.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAttributeUtils {

    private final ColourRepository colourRepository;
    private final DesignRepository designRepository;
    private final RoomRepository roomRepository;



    public Colour findColour(String colourName) {

        return colourRepository.findByName(colourName)
                .orElseThrow(() -> new ProductAttributeNotFound("Colour with name: " + colourName + " not found"));
    }

    public Design findDesign(String designName) {

        return designRepository.findByName(designName)
                .orElseThrow(() -> new ProductAttributeNotFound("Design with name: " + designName + " not found"));
    }

    public Room findRoom(String roomName) {

        return roomRepository.findByName(roomName)
                .orElseThrow(() -> new ProductAttributeNotFound("Room with name: " + roomName + " not found"));
    }

    public List<Colour> findColours(List<String> colourNames) {
        return colourNames.stream()
                .map(this::findColour)
                .toList();
    }

    public List<Design> findDesigns(List<String> designNames) {
        return designNames.stream()
                .map(this::findDesign)
                .toList();
    }

    public List<Room> findRooms(List<String> roomNames) {
        return roomNames.stream()
                .map(this::findRoom)
                .toList();
    }

}
