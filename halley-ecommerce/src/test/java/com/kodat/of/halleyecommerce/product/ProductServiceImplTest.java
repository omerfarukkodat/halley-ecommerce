package com.kodat.of.halleyecommerce.product;

import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.category.CategoryRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private SearchService searchService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SlugService slugService;
    @Mock
    private ProductValidator productValidator;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
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
    void addProduct_validInput_productIsAdded() {

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
    void updateProduct_existingId_productIsUpdated() {
        Long productId = 1L;
        Authentication mockAuthentication = mock(Authentication.class);
        doNothing().when(roleValidator).verifyAdminRole(mockAuthentication);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old product");
        existingProduct.setDescription("Old description");
        existingProduct.setOriginalPrice(BigDecimal.valueOf(200));
        existingProduct.setDiscountedPrice(BigDecimal.valueOf(100));
        existingProduct.setStock(19);
        existingProduct.setProductCode("OP001");

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
    void findAllProducts_validInput_productIsFound() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test product1");
        product1.setDescription("Test description1");
        product1.setOriginalPrice(BigDecimal.valueOf(200));
        product1.setDiscountedPrice(BigDecimal.valueOf(100));
        product1.setStock(19);
        product1.setProductCode("OP001");
        product1.setCategories(Set.of(Category.builder().id(1L).build()));

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
        assertEquals("Test product1",result.getContent().get(0).getName());
        assertEquals("Test product2",result.getContent().get(1).getName());
        verify(productRepository).findAll(any(Pageable.class));
    }
    @Test
    void findProductById_productExists_returnsProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test product");
        product.setDescription("Test description");
        product.setOriginalPrice(BigDecimal.valueOf(200));
        product.setDiscountedPrice(BigDecimal.valueOf(100));
        product.setStock(19);
        product.setProductCode("TP001");
        product.setCategories(Set.of(Category.builder().id(1L).build()));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDto result = productService.findProductById(productId);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals("Test description", result.getDescription());
        assertEquals(product.getOriginalPrice(), result.getOriginalPrice());
        assertEquals(product.getDiscountedPrice(), result.getDiscountedPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(product.getProductCode(), result.getProductCode());
        verify(productRepository).findById(productId);

    }
    @Test
    void findProductById_productDoesNotExist_throwsProductNotFoundException() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
        verify(productRepository).findById(productId);
    }
    @Test
    void findProductsByCategoryId_validInput_productIsFound() {
        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Test Category1");

        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test product");
        product.setDescription("Test description");
        product.setOriginalPrice(BigDecimal.valueOf(200));
        product.setDiscountedPrice(BigDecimal.valueOf(100));
        product.setStock(19);
        product.setProductCode("OP001");
        product.setCategories(Set.of(category));

        doNothing().when(categoryValidator).validateCategoryIds(Set.of(category.getId()));
        List<Product> productList = List.of(product);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(productRepository.findByCategories_Id(category.getId(), pageable)).thenReturn(new PageImpl<>(productList));
        PageResponse<ProductDto> result = productService.findProductsByCategoryId(0,10, category.getId(), "name","asc");

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals("Test product",result.getContent().getFirst().getName());
        assertEquals("OP001",result.getContent().getFirst().getProductCode());
        verify(productRepository).findByCategories_Id(category.getId(), pageable);
        verify(categoryValidator).validateCategoryIds(Set.of(category.getId()));


    }



}
