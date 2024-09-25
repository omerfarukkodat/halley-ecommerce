package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.mapper.product.ProductMapper;
import com.kodat.of.halleyecommerce.util.CategoryUtils;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.ProductValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final RoleValidator roleValidator;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;
    private final CategoryUtils categoryUtils;

    public ProductServiceImpl(ProductRepository productRepository, RoleValidator roleValidator, CategoryValidator categoryValidator, ProductValidator productValidator, CategoryUtils categoryUtils) {
        this.productRepository = productRepository;
        this.roleValidator = roleValidator;
        this.categoryValidator = categoryValidator;
        this.productValidator = productValidator;
        this.categoryUtils = categoryUtils;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto , Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser); //Check the user role(admin or not)
        categoryValidator.validateCategoryId(productDto.getCategoryId()); // check category exists or not?
        productValidator.validateProductCode(productDto.getProductCode()); // check product added before or not?
        Category category = categoryUtils.findCategoryById(productDto.getCategoryId());
        Product product = productRepository.save(ProductMapper.toProduct(productDto , category));
        LOGGER.info("Product: {} added successfully with product code: {}", productDto.getName(), product.getProductCode());
        return ProductMapper.toProductDto(product);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        categoryValidator.validateCategoryId(productDto.getCategoryId());
        productValidator.validateProductCode(productDto.getProductCode(), id);
        Product exsistingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + id));
        Category category = categoryUtils.findCategoryById(productDto.getCategoryId());
        Product updatedProduct = ProductMapper.updateProductFromDto(productDto, exsistingProduct, category);
        productRepository.save(updatedProduct);
        LOGGER.info("Product with ID: {} updated successfully",id);
        return ProductMapper.toProductDto(updatedProduct);
    }

}
