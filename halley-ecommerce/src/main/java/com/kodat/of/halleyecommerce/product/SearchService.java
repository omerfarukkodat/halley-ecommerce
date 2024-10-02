package com.kodat.of.halleyecommerce.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class SearchService {
    private final ProductRepository productRepository;


    public SearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> searchProducts(String searchTerm , Pageable pageable){
        return productRepository.findProductsByFullTextSearch(searchTerm,pageable);

    }


}
