package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.mapper.product.ProductMapper;
import com.kodat.of.halleyecommerce.util.CategoryUtils;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.ProductValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
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
    public ProductDto addProduct(ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser); //Check the user role(admin or not)
        categoryValidator.validateCategoryId(productDto.getCategoryId()); // check category exists or not?
        productValidator.validateProductCode(productDto.getProductCode()); // check product added before or not?
        Category category = categoryUtils.findCategoryById(productDto.getCategoryId());
        Product product = productRepository.save(ProductMapper.toProduct(productDto, category));
        LOGGER.info("Product: {} added successfully with product code: {}", productDto.getName(), product.getProductCode());
        return ProductMapper.toProductDto(product);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        categoryValidator.validateCategoryId(productDto.getCategoryId());
        productValidator.validateProductCode(productDto.getProductCode(), productId);
        Product exsistingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        Category category = categoryUtils.findCategoryById(productDto.getCategoryId());
        Product updatedProduct = ProductMapper.updateProductFromDto(productDto, exsistingProduct, category);
        productRepository.save(updatedProduct);
        LOGGER.info("Product with ID: {} updated successfully", productId);
        return ProductMapper.toProductDto(updatedProduct);
    }

    @Override
    public PageResponse<ProductDto> findAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productCode").descending());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductDto> productDtos = products.stream()
                .map(ProductMapper::toProductDto)
                .toList();
        return new PageResponse<>(
                productDtos,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast()
        );
    }

    @Override
    public ProductDto findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with id:" + productId + " not found"));
    }

    @Override
    public PageResponse<ProductDto> findProductsByCategoryId(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("productCode").descending());
        Page<Product> products = productRepository.findByCategoryId(categoryId , pageable);
        List<ProductDto> productDtos = products.stream()
                .map(ProductMapper::toProductDto)
                .toList();
        return new PageResponse<>(
                productDtos,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast()
        );

    }

}
