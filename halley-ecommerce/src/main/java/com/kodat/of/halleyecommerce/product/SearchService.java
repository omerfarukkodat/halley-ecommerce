package com.kodat.of.halleyecommerce.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SearchService {
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;


    public SearchService(ProductRepository productRepository, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productSearchRepository = productSearchRepository;
    }

//    public Page<Product> searchProducts(String searchTerm , Pageable pageable){
//        return productRepository.findProductsByFullTextSearch(searchTerm,pageable);
//    }

    public List<ProductSearch> productSearches(String searchTerm){
        return productSearchRepository.findByNameContaining(searchTerm);
    }
    public Page<Product> findProductsByIdsFromSearch(String searchTerm , Pageable pageable){
        List<ProductSearch> productSearchPage = productSearches(searchTerm);
        List<Long> productIds = productSearchPage.stream()
                .map(productSearch -> Long.parseLong(productSearch.getId()))
                .toList();
        return productRepository.findAllByIdsWithPagination(productIds,pageable);
    }


}
