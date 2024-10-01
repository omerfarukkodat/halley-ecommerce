package com.kodat.of.halleyecommerce.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FuzzySearchService {
    private final ProductRepository productRepository;


    public FuzzySearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> fuzzySearch(String searchTerm , Pageable pageable){

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Page.empty(pageable);
        }


        Page<Product> trigramResults = productRepository.fuzzySearchByName(searchTerm,pageable);
        Page<Product> levenshteinResults = productRepository.fuzzySearchByNameWithWildcard(searchTerm,pageable);

        Set<Product> combinedResults = new HashSet<>();
        combinedResults.addAll(trigramResults.getContent());
        combinedResults.addAll(levenshteinResults.getContent());

        List<Product> pagedResults = combinedResults.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(pagedResults,pageable,pagedResults.size());

    }


}
