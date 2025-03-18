package com.kodat.of.halleyecommerce.showcase;

import com.kodat.of.halleyecommerce.dto.showcase.ShowcaseDto;
import com.kodat.of.halleyecommerce.exception.ShowcaseNotFoundException;
import com.kodat.of.halleyecommerce.mapper.showcase.ShowcaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowcaseServiceImpl implements ShowcaseService {

    private final ShowcaseRepository showcaseRepository;


    @Override
    public List<ShowcaseDto> getAll() {

        List<Showcase> showcases = showcaseRepository.findAll();

        if (showcases.isEmpty()) {
            throw new ShowcaseNotFoundException("No showcases found");
        }

        return showcases.stream()
                .map(ShowcaseMapper::toShowcaseDto)
                .toList();
    }
}
