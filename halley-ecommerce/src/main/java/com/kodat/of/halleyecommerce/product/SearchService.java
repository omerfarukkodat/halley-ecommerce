package com.kodat.of.halleyecommerce.product;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;


    public List<ProductSearch> productSearches(String searchTerm){
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }

        searchTerm = sanitizeSearchTerm(searchTerm);

        String[] words = searchTerm.split("\\s+");

        Criteria criteria = new Criteria();

        for (String word : words) {
            criteria = criteria.or(new Criteria("name").fuzzy(word))
                    .or(new Criteria("description").contains(word))
                    .or(new Criteria("brand").fuzzy(word));
        }

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(criteriaQuery, ProductSearch.class)
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public Page<Product> findProductsByIdsFromSearch(
            String searchTerm,
            Pageable pageable,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {
        List<ProductSearch> productSearchPage = productSearches(searchTerm);
        List<Long> productIds = productSearchPage.stream()
                .map(productSearch -> Long.parseLong(productSearch.getId()))
                .toList();
        return productRepository
                .findAllByIdsWithPagination(productIds,minPrice,maxPrice,categoryId,brand,wallpaperType,wallpaperSize,pageable);
    }

    private String sanitizeSearchTerm(String searchTerm) {
        return searchTerm.replaceAll("[^\\p{L}\\p{N}\\s]", "").trim();
    }


}
