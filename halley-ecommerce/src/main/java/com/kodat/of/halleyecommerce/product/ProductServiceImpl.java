package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.common.SlugService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final RoleValidator roleValidator;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;
    private final CategoryUtils categoryUtils;
    private final SearchService searchService;
    private final SlugService slugService;

    public ProductServiceImpl(ProductRepository productRepository, RoleValidator roleValidator, CategoryValidator categoryValidator, ProductValidator productValidator, CategoryUtils categoryUtils, SearchService searchService, SlugService slugService) {
        this.productRepository = productRepository;
        this.roleValidator = roleValidator;
        this.categoryValidator = categoryValidator;
        this.productValidator = productValidator;
        this.categoryUtils = categoryUtils;
        this.searchService = searchService;
        this.slugService = slugService;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser); //Check the user role(admin or not)
        categoryValidator.validateCategoryIds(productDto.getCategoryIds()); // check category exists or not?
        productValidator.validateProductCode(productDto.getProductCode()); // check product added before or not?
        Set<Category> categories = productDto.getCategoryIds().stream()
                .map(categoryUtils::findCategoryById)
                .collect(Collectors.toSet());
        String slug = slugService.generateSlug(productDto.getName() , productDto.getProductCode()); //Create slug for friendly-url
        Product product = productRepository.save(ProductMapper.toProduct(productDto, categories , slug));
        LOGGER.info("Product: {} added successfully with product code: {}", productDto.getName(), product.getProductCode());
        return ProductMapper.toProductDto(product);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        categoryValidator.validateCategoryIds(productDto.getCategoryIds());
        productValidator.validateProductCode(productDto.getProductCode(), productId);
        Product exsistingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        Set<Category> category = productDto.getCategoryIds().stream()
                .map(categoryUtils::findCategoryById)
                .collect(Collectors.toSet());
        String slug = slugService.generateSlug(productDto.getName() , productDto.getProductCode());
        Product updatedProduct = ProductMapper.updateProductFromDto(productDto, exsistingProduct, category);
        updatedProduct.setSlug(slug);
        productRepository.save(updatedProduct);
        LOGGER.info("Product with ID: {} updated successfully", productId);
        return ProductMapper.toProductDto(updatedProduct);
    }

    @Override
    public PageResponse<ProductDto> findAllProducts(int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository.findAll(pageable);
        return createPageResponse(products);
    }

    @Override
    public ProductDto findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with id:" + productId + " not found"));
    }

    @Override
    public PageResponse<ProductDto> findProductsByCategoryId(int page, int size, Long categoryId, String sortBy, String sortDirection) {
        categoryValidator.validateCategoryIds(Set.of(categoryId));
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository.findByCategories_Id(categoryId, pageable);
        return createPageResponse(products);
    }

    @Override
    public void deleteProductById(Long productId, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        productValidator.validateProductId(productId);
        LOGGER.info("Product with ID: {} deleted successfully", productId);
        productRepository.deleteById(productId);
    }

    @Override
    public PageResponse<ProductDto> findProductsBySearch(String searchTerm, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> productsPage = searchService.searchProducts(searchTerm, pageable);
        return createPageResponse(productsPage);
    }

    @Override
    public PageResponse<ProductDto> filterProducts(Set<Long> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository.findAllWithFilters(categoryIds,minPrice,maxPrice,pageable);
        if (products.isEmpty()){
            throw new ProductNotFoundException("Cannot finding any products with these filter elements.");
        }
        return createPageResponse(products);
    }

    @Override
    public PageResponse<ProductDto> findSimilarProducts(Long productId, int page, int size) {
        Product existingProduct = productValidator.validateProductAndFindById(productId);
        Set<Long> categoryIds = existingProduct.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        Pageable pageable = createPageable(page,size,"id" ,"desc");
        Page<Product> similarProductsPage = productRepository.findByCategories_IdInAndIdNot(categoryIds,productId,pageable);
        return createPageResponse(similarProductsPage);

    }

    @Override
    public List<ProductDto> findFeaturedProducts(int limit) {
        return productRepository.findTopFeaturedProducts(limit)
                .stream()
                .map(ProductMapper::toProductDto)
                .toList();
    }


    private PageResponse<ProductDto> createPageResponse(Page<Product> products) {
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

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }


}
