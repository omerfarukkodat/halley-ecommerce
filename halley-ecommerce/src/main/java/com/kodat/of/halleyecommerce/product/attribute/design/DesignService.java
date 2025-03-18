package com.kodat.of.halleyecommerce.product.attribute.design;


import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.product.attribute.design.DesignDto;
import com.kodat.of.halleyecommerce.dto.product.attribute.room.RoomDto;
import com.kodat.of.halleyecommerce.exception.ProductAttributeAlreadyExists;
import com.kodat.of.halleyecommerce.exception.ProductAttributeNotFound;
import com.kodat.of.halleyecommerce.mapper.product.attribute.design.DesignMapper;
import com.kodat.of.halleyecommerce.mapper.product.attribute.room.RoomMapper;
import com.kodat.of.halleyecommerce.product.attribute.room.Room;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignService {

    private final DesignRepository designRepository;
    private final SlugService slugService;
    private final RoleValidator roleValidator;

    public DesignDto findById(Long id) {

        Design design = designRepository.findById(id)
                .orElseThrow(() -> new ProductAttributeNotFound("Design not found with this id: " + id));

        return DesignMapper.toDesignDto(design);
    }

    public DesignDto add(DesignDto designDto, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        if (designRepository.findByName(designDto.getName()).isPresent()) {
            throw new ProductAttributeAlreadyExists("Design " + designDto.getName() + " already exists");
        }
        String slug = slugService.generateSlugWithNoCode(designDto.getName());

        Design design = DesignMapper.toDesign(designDto);
        design.setSlug(slug);
        designRepository.save(design);

        return DesignMapper.toDesignDto(design);


    }

    public List<DesignDto> findAll() {

        List<Design> designs = designRepository.findAll();
        return DesignMapper.toDesignDtoList(designs);
    }

    public void deleteById(Long id, Authentication connectedUser) {

        roleValidator.verifyAdminRole(connectedUser);

        if (designRepository.findById(id).isPresent()) {
            designRepository.deleteById(id);
        } else {
            throw new ProductAttributeNotFound("Design not found with this id: " + id);
        }
    }

    public DesignDto findBySlug(String slug) {

        Design design = designRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Design not found with this slug: " + slug));

        return DesignMapper.toDesignDto(design);
    }
}
