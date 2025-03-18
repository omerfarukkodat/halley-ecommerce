package com.kodat.of.halleyecommerce.product.attribute.colour;

import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.product.attribute.colour.ColourDto;
import com.kodat.of.halleyecommerce.dto.product.attribute.design.DesignDto;
import com.kodat.of.halleyecommerce.exception.ProductAttributeAlreadyExists;
import com.kodat.of.halleyecommerce.exception.ProductAttributeNotFound;
import com.kodat.of.halleyecommerce.mapper.product.attribute.colour.ColourMapper;
import com.kodat.of.halleyecommerce.mapper.product.attribute.design.DesignMapper;
import com.kodat.of.halleyecommerce.product.attribute.design.Design;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColourService {

    private final ColourRepository colourRepository;
    private final SlugService slugService;
    private final RoleValidator roleValidator;

    public List<ColourDto> findAll() {

        List<Colour> colours = colourRepository.findAll();
        return ColourMapper.toColourDtos(colours);
    }

    public ColourDto findById(Long id) {
        Colour colour = colourRepository.findById(id)
                .orElseThrow(() -> new ProductAttributeNotFound("Colour not found with this id: " + id));

        return ColourMapper.toColourDto(colour);
    }

    public ColourDto findBySlug(String slug) {

        Colour colour = colourRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Colour not found with this slug: " + slug));

        return ColourMapper.toColourDto(colour);
    }

    public void deleteById(Long id, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);

        if (colourRepository.findById(id).isPresent()) {
            colourRepository.deleteById(id);
        } else {
            throw new ProductAttributeNotFound("Colour not found with this id: " + id);
        }
    }

    public ColourDto add(ColourDto colourDto, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        if (colourRepository.findByName(colourDto.getName()).isPresent()) {
            throw new ProductAttributeAlreadyExists("Colour " + colourDto.getName() + " already exists");
        }
        String slug = slugService.generateSlugWithNoCode(colourDto.getName());

        Colour colour = ColourMapper.toColour(colourDto);
        colour.setSlug(slug);
        colourRepository.save(colour);

        return ColourMapper.toColourDto(colour);

    }
}
