package com.kodat.of.halleyecommerce.product;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, String> {

    List<ProductSearch> findByNameContaining(String query);
}
