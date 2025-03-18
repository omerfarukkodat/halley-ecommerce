package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.brand.Brand;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.common.PageResponse;
import com.kodat.of.halleyecommerce.common.SlugService;
import com.kodat.of.halleyecommerce.dto.category.CategoryPathDto;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.exception.CategoryDoesNotExistsException;
import com.kodat.of.halleyecommerce.exception.ProductAttributeNotFound;
import com.kodat.of.halleyecommerce.exception.ProductNotFoundException;
import com.kodat.of.halleyecommerce.mapper.category.CategoryPathMapper;
import com.kodat.of.halleyecommerce.mapper.product.ProductMapper;
import com.kodat.of.halleyecommerce.product.attribute.colour.Colour;
import com.kodat.of.halleyecommerce.product.attribute.colour.ColourRepository;
import com.kodat.of.halleyecommerce.product.attribute.design.Design;
import com.kodat.of.halleyecommerce.product.attribute.design.DesignRepository;
import com.kodat.of.halleyecommerce.product.attribute.room.Room;
import com.kodat.of.halleyecommerce.product.attribute.room.RoomRepository;
import com.kodat.of.halleyecommerce.util.BrandUtils;
import com.kodat.of.halleyecommerce.util.CategoryUtils;
import com.kodat.of.halleyecommerce.util.ProductAttributeUtils;
import com.kodat.of.halleyecommerce.validator.CategoryValidator;
import com.kodat.of.halleyecommerce.validator.ProductValidator;
import com.kodat.of.halleyecommerce.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final RoleValidator roleValidator;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;
    private final CategoryUtils categoryUtils;
    private final SearchService searchService;
    private final SlugService slugService;
    private final ProductSearchRepository productSearchRepository;
    private final BrandUtils brandUtils;
    private final ColourRepository colourRepository;
    private final DesignRepository designRepository;
    private final RoomRepository roomRepository;
    private final ProductAttributeUtils productAttributeUtils;


    @Override
    public ProductDto addProduct(ProductDto productDto, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser); //Check the user role(admin or not)
        categoryValidator.validateCategoryIds(productDto.getCategoryIds()); // check category exists or not?
        productValidator.validateProductCode(productDto.getProductCode()); // check product added before or not?

        Set<Category> categories = productDto.getCategoryIds().stream()
                .map(categoryUtils::findCategoryById)
                .collect(Collectors.toSet());

        Brand brand = brandUtils.findBrand(productDto.getBrand());

        List<Colour> colours = productAttributeUtils.findColours(productDto.getColourName());
        List<Design> designs = productAttributeUtils.findDesigns(productDto.getDesignName());
        List<Room> rooms = productAttributeUtils.findRooms(productDto.getRoomName());

        String slug = slugService.generateSlug(productDto.getName(), productDto.getProductCode()); //Create slug for friendly-url

        Product product = productRepository
                .save(ProductMapper.toProduct(productDto, categories, slug, brand, colours, designs, rooms));

        LOGGER.info("Product: {} added successfully with product code: {}", productDto.getName(), product.getProductCode());

        ProductSearch productSearch = ProductSearch.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .brandName(product.getBrand().getName())
                .build();

        productSearchRepository.save(productSearch);
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

        Brand brand = brandUtils.findBrand(productDto.getBrand());
        List<Colour> colours = productAttributeUtils.findColours(productDto.getColourName());
        List<Design> designs = productAttributeUtils.findDesigns(productDto.getDesignName());
        List<Room> rooms = productAttributeUtils.findRooms(productDto.getRoomName());

        String slug = slugService.generateSlug(productDto.getName(), productDto.getProductCode());

        Product updatedProduct = ProductMapper
                .updateProductFromDto(productDto, exsistingProduct, category, brand, colours, designs, rooms);
        updatedProduct.setSlug(slug);
        productRepository.save(updatedProduct);

        ProductSearch productSearch = productSearchRepository.findById(updatedProduct.getId().toString())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + updatedProduct.getId()));

        productSearch.setId(updatedProduct.getId().toString());
        productSearch.setName(updatedProduct.getName());
        productSearch.setDescription(updatedProduct.getDescription());
        productSearch.setBrandName(updatedProduct.getBrand().getName());
        productSearchRepository.save(productSearch);

        LOGGER.info("Product with ID: {} updated successfully", productId);

        return ProductMapper.toProductDto(updatedProduct);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all_products_' + #page + '_' + #size + '_' + #sortBy + '_' + #sortDirection" +
            " + '_' + #categoryIds + '_' + #brand + '_' + #wallpaperType + '_' + #wallpaperSize+ '_' + #maxPrice" +
            " + '_' + #minPrice")
    @Override
    public PageResponse<ProductDto> findAllProducts(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryIds,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository
                .findAllWithFilters(categoryIds, minPrice, maxPrice, pageable, wallpaperType, wallpaperSize, brand);
        return createPageResponse(products);
    }

    @Cacheable(value = "findById", key = "#productId")
    @Override
    public ProductDto findProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with id:" + productId + " not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "findByCategoryId", key = "'find_by_category_id_' + #page + '_' + #size  + '_' + #sortBy" +
            " + '_' + #sortDirection + '_' + #brand + '_' + #wallpaperType + '_' + #wallpaperSize + '_' + #slug " +
            " + '_' + #maxPrice + '_' + #minPrice ")
    @Override
    public PageResponse<ProductDto> findProductsByCategoryId
            (int page,
             int size,
             String slug,
             String sortBy,
             String sortDirection,
             BigDecimal minPrice,
             BigDecimal maxPrice,
             Long brand,
             String wallpaperType,
             String wallpaperSize
            ) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Long categoryId = categoryUtils.findCategoryBySlug(slug);
        Page<Product> products = productRepository
                .findByCategories_Id(categoryId, minPrice, maxPrice, brand, wallpaperType, wallpaperSize, pageable);

        return createPageResponse(products);
    }

    @Override
    public void deleteProductById(Long productId, Authentication connectedUser) {
        roleValidator.verifyAdminRole(connectedUser);
        productValidator.validateProductId(productId);
        LOGGER.info("Product with ID: {} deleted successfully", productId);
        productRepository.deleteById(productId);
        productSearchRepository.deleteById(productId.toString());
    }

    @Cacheable(value = "find_by_search", key = "'find_by_search_' + #searchTerm + '_' + #page + '_' + #size + '_' + #sortBy" +
            " + '_' + #sortDirection + '_' + #categoryId + '_' + #brand + '_' + #wallpaperType + '_' + #wallpaperSize" +
            " + '_' + #maxPrice + '_' + #minPrice")
    @Override
    public PageResponse<ProductDto> findProductsBySearch
            (String searchTerm,
             int page,
             int size,
             String sortBy,
             String sortDirection,
             BigDecimal minPrice,
             BigDecimal maxPrice,
             Long categoryId,
             Long brand,
             String wallpaperType,
             String wallpaperSize) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> productsPage = searchService
                .findProductsByIdsFromSearch(searchTerm, pageable, minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize);
        return createPageResponse(productsPage);
    }

    @Override
    public PageResponse<ProductDto> filterProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository
                .findAllWithFilters(categoryId, minPrice, maxPrice, pageable, wallpaperType, wallpaperSize, brand);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Cannot finding any products with these filter elements.");
        }
        return createPageResponse(products);
    }

    @Cacheable(value = "similarProducts", key = "'similar_products_' + #productId + '_' + #page + '_' + #size")
    @Override
    public PageResponse<ProductDto> findSimilarProducts(Long productId, int page, int size) {
        Product existingProduct = productValidator.validateProductAndFindById(productId);
        Set<Long> categoryIds = existingProduct.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        Pageable pageable = createPageable(page, size, "id", "desc");
        Page<Product> similarProductsPage = productRepository.findByCategories_IdInAndIdNot(categoryIds, productId, pageable);
        return createPageResponse(similarProductsPage);

    }

    @Override
    public PageResponse<ProductDto> findFeaturedProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Page<Product> productsPage = productRepository.findTopFeaturedProductsWithFilters(
                        categoryId, minPrice, maxPrice, brand, wallpaperType, wallpaperSize,pageable);

        if (productsPage.isEmpty()) {
            throw new ProductNotFoundException("Cannot find featured any products.");
        }
        return createPageResponse(productsPage);
    }

    @Override
    public PageResponse<ProductDto> getDiscountedProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> discountedProducts = productRepository
                .findDiscountedProducts(categoryId, minPrice, maxPrice, brand, wallpaperType, wallpaperSize, pageable);
        return createPageResponse(discountedProducts);
    }

    @Override
    public Integer getStockById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
        return product.getStock() == 0 ? 0 : product.getStock();
    }

    @Override
    public List<CategoryPathDto> getCategoryPaths(Long productId, Long categoryId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        Set<Category> productCategories = product.getCategories().stream()
                .sorted(Comparator.comparing(Category::getId))
                .collect(Collectors.toSet());

        if (categoryId == null) {

            Optional<Category> leafCategory = productCategories.stream()
                    .filter(category -> category.getSubCategories().isEmpty())
                    .min(Comparator.comparing(Category::getCreatedTime));

            if (leafCategory.isEmpty()) {
                throw new CategoryDoesNotExistsException("No valid category found for product with id " + productId);
            }
            categoryId = leafCategory.get().getId();
        }

        Category category = categoryUtils.findCategoryById(categoryId);

        List<CategoryPathDto> categoryPathDtos = new ArrayList<>();


        categoryPathDtos.add(CategoryPathMapper.toCategoryPath(category));


        for (Category subCategory : category.getSubCategories()) {
            if (productCategories.contains(subCategory)) { // Ürüne ait olanları ekliyoruz
                categoryPathDtos.add(CategoryPathMapper.toCategoryPath(subCategory));
            }
        }

        while (category.getParent() != null) {
            category = category.getParent();
            CategoryPathDto parentDto = CategoryPathMapper.toCategoryPath(category);
            if (!categoryPathDtos.contains(parentDto)) {
                categoryPathDtos.add(parentDto);
            }
        }
        Collections.reverse(categoryPathDtos);
        return categoryPathDtos;
    }

    @Cacheable(value = "find_all_by_brand", key = "'find_all_by_brand' + #brand + '_' + #page + '_' + #size + '_'" +
            " + #sortBy + '_' + #sortDirection + '_' + #categoryId + '_' + #wallpaperType + '_' + #wallpaperSize" +
            " + '_' + #maxPrice + '_' + #minPrice ")
    @Override
    public PageResponse<ProductDto> findAllByBrand(
            String brand,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            String wallpaperType,
            String wallpaperSize) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> productPage = productRepository
                .findByBrandAndFilters(minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize, pageable);
        return createPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductDto> findLastAdded(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Long brand,
            String wallpaperType,
            String wallpaperSize) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Product> products = productRepository
                .findLastAddedWithFilters(minPrice, maxPrice, categoryId, brand, wallpaperType, wallpaperSize, pageable);
        return createPageResponse(products);
    }


    @Cacheable(value = "find_all_by_colour", key = "'find_products_by_colour_id' + '_' + #slug + '_' + #page + '_'" +
            " + #size + '_' + #sortBy + '_' + #sortDirection  + '_' +  #brand + '_' + #wallpaperType + '_'" +
            " + #wallpaperSize + '_' + #maxPrice + '_' + #minPrice ")
    @Override
    public PageResponse<ProductDto> findByColourSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Colour colour = colourRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Colour with name: " + slug + " not found"));

        Page<Product> products = productRepository
                .findByColours_Id(colour.getId(), minPrice, maxPrice, brand, wallpaperType, wallpaperSize, pageable);

        return createPageResponse(products);
    }

    @Cacheable(value = "find_all_by_design", key = "'find_products_by_design_id' + '_' + #slug + '_' + #page + '_'" +
            " + #size + '_' + #sortBy + '_' + #sortDirection  + '_' +  #brand + '_' + #wallpaperType + '_'" +
            " + #wallpaperSize + '_' + #maxPrice + '_' + #minPrice ")
    @Override
    public PageResponse<ProductDto> findByDesignSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Design design = designRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Design with name: " + slug + " not found"));

        Page<Product> products = productRepository
                .findByDesigns_Id(design.getId(), minPrice, maxPrice, brand, wallpaperType, wallpaperSize, pageable);

        return createPageResponse(products);

    }

    @Cacheable(value = "find_all_by_room", key = "'find_products_by_room_id' + '_' + #slug + '_' + #page + '_'" +
            " + #size + '_' + #sortBy + '_' + #sortDirection  + '_' +  #brand + '_' + #wallpaperType + '_'" +
            " + #wallpaperSize + '_' + #maxPrice + '_' + #minPrice ")
    @Override
    public PageResponse<ProductDto> findByRoomSlug(
            String slug,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            Long brand,
            String wallpaperType,
            String wallpaperSize,
            BigDecimal maxPrice,
            BigDecimal minPrice) {

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Room room = roomRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductAttributeNotFound("Room with name: " + slug + " not found"));

        Page<Product> products = productRepository
                .findByRooms_Id(room.getId(), minPrice, maxPrice, brand, wallpaperType, wallpaperSize, pageable);

        return createPageResponse(products);
    }

    public PageResponse<ProductDto> createPageResponse(Page<Product> products) {
        List<ProductDto> productDtos = products.stream()
                .map(product -> {
                    Hibernate.initialize(product.getImageUrls());
                    return ProductMapper.toProductDto(product);
                })
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
