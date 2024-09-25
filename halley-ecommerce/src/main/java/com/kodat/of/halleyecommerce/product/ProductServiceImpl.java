package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.mapper.product.ProductMapper;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.ProductValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final RoleValidator roleValidator;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;

    public ProductServiceImpl(ProductRepository productRepository, RoleValidator roleValidator, CategoryValidator categoryValidator, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.roleValidator = roleValidator;
        this.categoryValidator = categoryValidator;
        this.productValidator = productValidator;
    }


    @Override
    public ProductDto addProduct(ProductDto productDto , Authentication connectedUser) {
        //check the user or admin?
        roleValidator.verifyAdminRole(connectedUser);
        // check category is exists or not?
        categoryValidator.validateCategoryId(productDto.getCategoryId());
        // check product added before or not?
        productValidator.validateProductCode(productDto.getProductCode());
        Product product = productRepository.save(ProductMapper.toProduct(productDto))




        return null;
    }
}
