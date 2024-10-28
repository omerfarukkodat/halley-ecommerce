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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private RoleValidator roleValidator;
    @Mock
    private CategoryValidator categoryValidator;
    @Mock
    private CategoryUtils categoryUtils;
    @Mock
    private SlugService slugService;
    @Mock
    private ProductValidator productValidator;
    private ProductDto productDto;
    private Product sampleProduct;
    private Category sampleCategory;

    @BeforeEach
    public void setUp() {
        sampleCategory = Category.builder()
                .id(1L)
                .categoryName("Sample Category")
                .build();

        sampleProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .originalPrice(BigDecimal.valueOf(200))
                .discountedPrice(BigDecimal.valueOf(100))
                .stock(19)
                .productCode("TP002")
                .categories(Set.of(sampleCategory))
                .build();

         productDto = ProductDto.builder()
                .name("Test product")
                .description("Test description")
                .originalPrice(BigDecimal.valueOf(200))
                .discountedPrice(BigDecimal.valueOf(100))
                .stock(19)
                .productCode("TP001")
                .categoryIds(Set.of(1L))
                .build();

    }

    @Test
    void when_addProduct_validInput_productIsAdded() {

        Authentication mockAuthentication = mock(Authentication.class);
        doNothing().when(roleValidator).verifyAdminRole(mockAuthentication);
        doNothing().when(categoryValidator).validateCategoryIds(any());
        doNothing().when(productValidator).validateProductCode(any());

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setCategoryName("Test Category");

        when(categoryUtils.findCategoryById(mockCategory.getId())).thenReturn(mockCategory);
        String generatedSlug = "test-product-slug";
        when(slugService.generateSlug(anyString(), anyString())).thenReturn(generatedSlug);
        Product product = ProductMapper.toProduct(productDto,Set.of(mockCategory),generatedSlug);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDto result = productService.addProduct(productDto,mockAuthentication);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getDescription(), result.getDescription());
        assertEquals(productDto.getOriginalPrice(), result.getOriginalPrice());
        assertEquals(productDto.getDiscountedPrice(), result.getDiscountedPrice());
        assertEquals(productDto.getStock(), result.getStock());
        assertEquals(productDto.getProductCode(), result.getProductCode());
        assertEquals(productDto.getCategoryIds(), result.getCategoryIds());
        verify(roleValidator).verifyAdminRole(mockAuthentication);
        verify(categoryValidator).validateCategoryIds(productDto.getCategoryIds());
        verify(productValidator).validateProductCode(productDto.getProductCode());
        verify(productRepository).save(any(Product.class));
    }
    @Test
    void when_updateProduct_existingId_productIsUpdated() {
        Long productId = 1L;
        Authentication mockAuthentication = mock(Authentication.class);
        doNothing().when(roleValidator).verifyAdminRole(mockAuthentication);

        Product existingProduct = sampleProduct;

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        ProductDto updatedProduct = ProductDto.builder()
                .name("Updated product")
                .description("updated description")
                .originalPrice(BigDecimal.valueOf(350))
                .discountedPrice(BigDecimal.valueOf(320))
                .stock(20)
                .productCode("UP001")
                .categoryIds(Set.of(1L,2L))
                .build();

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setCategoryName("Test Category1");
        Category mockCategory2 = new Category();
        mockCategory2.setId(2L);
        mockCategory2.setCategoryName("Test Category2");

        when(categoryUtils.findCategoryById(1L)).thenReturn(mockCategory);
        when(categoryUtils.findCategoryById(2L)).thenReturn(mockCategory2);

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        ProductDto result = productService.updateProduct(productId,updatedProduct,mockAuthentication);

        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getOriginalPrice(), result.getOriginalPrice());
        assertEquals(updatedProduct.getDiscountedPrice(), result.getDiscountedPrice());
        assertEquals(updatedProduct.getStock(), result.getStock());
        assertEquals(updatedProduct.getProductCode(), result.getProductCode());
        assertEquals(updatedProduct.getCategoryIds(), result.getCategoryIds());
        verify(roleValidator).verifyAdminRole(mockAuthentication);
        verify(categoryValidator).validateCategoryIds(updatedProduct.getCategoryIds());
        verify(productValidator).validateProductCode(updatedProduct.getProductCode(),productId);
        verify(productRepository).save(any(Product.class));




    }
    @Test
    void when_findAllProducts_validInput_productIsFound() {
        Product product1 = sampleProduct;

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test product2");
        product2.setDescription("Test description2");
        product2.setOriginalPrice(BigDecimal.valueOf(200));
        product2.setDiscountedPrice(BigDecimal.valueOf(100));
        product2.setStock(19);
        product2.setProductCode("UP001");
        product2.setCategories(Set.of(Category.builder().id(1L).build()));

        List<Product> productList = List.of(product1,product2);
        Pageable pageable = PageRequest.of(0, 10 , Sort.by("name").ascending());
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(productList));
        PageResponse<ProductDto> result = productService.findAllProducts(0,10,"name","asc");

        assertNotNull(result);
        assertEquals(2,result.getTotalElements());
        assertEquals("Test Product",result.getContent().get(0).getName());
        assertEquals("Test product2",result.getContent().get(1).getName());
        verify(productRepository).findAll(any(Pageable.class));
    }
    @Test
    void when_findProductById_productExists_returnsProduct() {
        Long productId = 1L;
        Product product = sampleProduct;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDto result = productService.findProductById(productId);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(product.getOriginalPrice(), result.getOriginalPrice());
        assertEquals(product.getDiscountedPrice(), result.getDiscountedPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(product.getProductCode(), result.getProductCode());
        verify(productRepository).findById(productId);

    }
    @Test
    void when_findProductById_productDoesNotExist_throwsProductNotFoundException() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
        verify(productRepository).findById(productId);
    }
    @Test
    void when_findProductsByCategoryId_validInput_productIsFound() {
        Category category = sampleCategory;
        Product product = sampleProduct;

        doNothing().when(categoryValidator).validateCategoryIds(Set.of(category.getId()));
        List<Product> productList = List.of(product);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(productRepository.findByCategories_Id(category.getId(), pageable)).thenReturn(new PageImpl<>(productList));
        PageResponse<ProductDto> result = productService.findProductsByCategoryId(0,10, category.getId(), "name","asc");

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals("Test Product",result.getContent().getFirst().getName());
        assertEquals("TP002",result.getContent().getFirst().getProductCode());
        verify(productRepository).findByCategories_Id(category.getId(), pageable);
        verify(categoryValidator).validateCategoryIds(Set.of(category.getId()));


    }
    @Test
    void when_deleteProductById_validInput_productIsFound() {
        Long productId = 1L;
        Authentication mockAuthentication = mock(Authentication.class);
        doNothing().when(roleValidator).verifyAdminRole(mockAuthentication);
        doNothing().when(productValidator).validateProductId(productId);
        productService.deleteProductById(productId, mockAuthentication);
        verify(roleValidator).verifyAdminRole(mockAuthentication);
        verify(productValidator).validateProductId(productId);
        verify(productRepository).deleteById(productId);
    }
    @Test
    void when_deleteProductById_productDoesNotExist_throwsProductNotFoundException() {
        Long invalidProductId = 1L;
        Authentication mockAuthentication = mock(Authentication.class);
        doThrow(new ProductNotFoundException("Product not found")).when(productValidator).validateProductId(invalidProductId);
        assertThrows(ProductNotFoundException.class , () -> productService.deleteProductById(invalidProductId, mockAuthentication));

        verify(roleValidator).verifyAdminRole(mockAuthentication);
        verify(productValidator).validateProductId(invalidProductId);
        verify(productRepository , never()).deleteById(invalidProductId);
    }
    @Test
    void when_filterProducts_validInput_productIsFound() {
        Category category = sampleCategory;
        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryName("Test Category2");
    Set<Long> categoryIds = Set.of(category.getId(), category2.getId());
    BigDecimal minPrice = BigDecimal.valueOf(150);
    BigDecimal maxPrice = BigDecimal.valueOf(200);
    int page = 0;
    int size = 10;
    String sortBy = "name";
    String sortDirection = "asc";

    Product product = sampleProduct;

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Test product2");
    product2.setDescription("Test description2");
    product2.setOriginalPrice(BigDecimal.valueOf(99));
    product2.setDiscountedPrice(BigDecimal.valueOf(99));
    product2.setStock(19);
    product2.setProductCode("UP001");
    product2.setCategories(Set.of(category2));
    List<Product> productList = List.of(product,product2);
    Page<Product> productPage = new PageImpl<>(productList);

    Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.ASC,sortBy));
    when(productRepository.findAllWithFilters(categoryIds,minPrice,maxPrice,pageable)).thenReturn(productPage);
    PageResponse<ProductDto> response = productService.filterProducts(categoryIds,minPrice,maxPrice,page,size,sortBy,sortDirection);

    assertNotNull(response);
    assertEquals(2,response.getContent().size());
    assertEquals("Test Product",response.getContent().get(0).getName());
    assertEquals("Test product2",response.getContent().get(1).getName());
    verify(productRepository).findAllWithFilters(categoryIds,minPrice,maxPrice,pageable);
    }
    @Test
    void when_filterProducts_productDoesNotExist_throwsProductNotFoundException() {
        Set<Long> categoryIds = Set.of(1L, 2L);
        BigDecimal minPrice = BigDecimal.valueOf(300);
        BigDecimal maxPrice = BigDecimal.valueOf(400);
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortDirection = "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Product> emptyPage = Page.empty();
        when(productRepository.findAllWithFilters(categoryIds, minPrice, maxPrice, pageable)).thenReturn(emptyPage);


        assertThrows(ProductNotFoundException.class, () -> productService.filterProducts(categoryIds, minPrice, maxPrice, page, size, sortBy, sortDirection));
        verify(productRepository).findAllWithFilters(categoryIds, minPrice, maxPrice, pageable);
    }
    @Test
    void when_findSimilarProducts_validInput_productIsFound() {
        Long productId = 2L;
        Category category = sampleCategory;
        Product product = sampleProduct;

        Product product2 = new Product();
        product2.setId(3L);
        product2.setName("Test product2");
        product2.setDescription("Test description2");
        product2.setOriginalPrice(BigDecimal.valueOf(99));
        product2.setDiscountedPrice(BigDecimal.valueOf(99));
        product2.setStock(19);
        product2.setProductCode("UP001");
        product2.setCategories(Set.of(category));

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Test product3");
        existingProduct.setDescription("Test description3");
        existingProduct.setOriginalPrice(BigDecimal.valueOf(99));
        existingProduct.setDiscountedPrice(BigDecimal.valueOf(99));
        existingProduct.setStock(19);
        existingProduct.setProductCode("UP002");
        existingProduct.setCategories(Set.of(category));

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Product> productList = List.of(product,product2);

        when(productValidator.validateProductAndFindById(productId)).thenReturn(existingProduct);
        Set<Long> categoryIds = existingProduct.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
        when(productRepository.findByCategories_IdInAndIdNot(categoryIds,productId,pageable))
                .thenReturn(new PageImpl<>(productList));
        PageResponse<ProductDto> response = productService.findSimilarProducts(productId,page,size);

        assertNotNull(response);
        assertEquals(2,response.getContent().size());
        assertEquals("TP002",response.getContent().get(0).getProductCode());
        assertEquals("UP001",response.getContent().get(1).getProductCode());
        verify(productValidator).validateProductAndFindById(productId);
        verify(productRepository).findByCategories_IdInAndIdNot(categoryIds,productId,pageable);
    }
    @Test
    void when_findSimilarProducts_productDoesNotExist_throwsProductNotFoundException() {
        Long productId = -1L;

        when(productValidator.validateProductAndFindById(productId))
                .thenThrow(new ProductNotFoundException("Product with id " + productId + " not found"));

        assertThrows(ProductNotFoundException.class , () -> {
            productService.findSimilarProducts(productId, 0, 10);
        });
    }
    @Test
    void when_findFeaturedProducts_validInput_productIsFound() {
        sampleProduct.setFeatured(true);
        Product product = Product.builder()
                .id(2L)
                .name("Test Product2")
                .description("Test Description2")
                .originalPrice(BigDecimal.valueOf(200))
                .discountedPrice(BigDecimal.valueOf(100))
                .stock(19)
                .productCode("TP003")
                .categories(Set.of(sampleCategory))
                .build();
        List<Product> productList = List.of(sampleProduct,product);

        int limit = 2;
        when(productRepository.findTopFeaturedProducts(limit)).thenReturn(productList);
        List<ProductDto> response = productService.findFeaturedProducts(limit);
        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals("Test Product2",response.get(1).getName());
        verify(productRepository).findTopFeaturedProducts(limit);
    }
    @Test
    void when_findFeaturedProducts_productDoesNotExist_throwsProductNotFoundException() {
    List<Product> productList = Collections.emptyList();
    int limit = 2;
    when(productRepository.findTopFeaturedProducts(limit)).thenReturn(productList);
    assertThrows(ProductNotFoundException.class , () -> {
        productService.findFeaturedProducts(limit);
    });
    verify(productRepository).findTopFeaturedProducts(limit);



    }



}
