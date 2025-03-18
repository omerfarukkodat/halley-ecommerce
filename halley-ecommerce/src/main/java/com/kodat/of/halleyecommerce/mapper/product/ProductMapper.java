package com.kodat.of.halleyecommerce.mapper.product;

import com.kodat.of.halleyecommerce.brand.Brand;
import com.kodat.of.halleyecommerce.dto.product.ProductDto;
import com.kodat.of.halleyecommerce.category.Category;
import com.kodat.of.halleyecommerce.product.Product;
import com.kodat.of.halleyecommerce.product.attribute.colour.Colour;
import com.kodat.of.halleyecommerce.product.attribute.design.Design;
import com.kodat.of.halleyecommerce.product.attribute.room.Room;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ProductMapper {

    public static Product toProduct(
            ProductDto productDto,
            Set<Category> categories,
            String slug,
            Brand brand,
            List<Colour> colours,
            List<Design> designs,
            List<Room> rooms
    ) {


        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .originalPrice(productDto.getOriginalPrice())
                .discountedPrice(productDto.getDiscountedPrice())
                .brand(brand)
                .colours(colours)
                .designs(designs)
                .rooms(rooms)
                .size(productDto.getSize())
                .wallpaperType(productDto.getWallpaperType())
                .stock(productDto.getStock())
                .productCode(productDto.getProductCode())
                .imageUrls(productDto.getImageUrls())
                .categories(categories)
                .slug(slug)
                .isFeatured(productDto.isFeatured())
                .build();
    }

    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .originalPrice(product.getOriginalPrice())
                .discountedPrice(product.getDiscountedPrice())
                .brand(product.getBrand().getName())
                .colourName(product.getColours().stream().map(Colour::getName).collect(Collectors.toList()))
                .designName(product.getDesigns().stream().map(Design::getName).collect(Collectors.toList()))
                .roomName(product.getRooms().stream().map(Room::getName).collect(Collectors.toList()))
                .size(product.getSize())
                .wallpaperType(product.getWallpaperType())
                .stock(product.getStock())
                .productCode(product.getProductCode())
                .imageUrls(product.getImageUrls())
                .categoryIds(product.getCategories().stream().map(Category::getId).collect(Collectors.toSet()))
                .slug(product.getSlug())
                .isFeatured(product.isFeatured())
                .build();
    }

    public static Product updateProductFromDto(
            ProductDto productDto,
            Product existingProduct,
            Set<Category> categories,
            Brand brand,
            List<Colour> colours,
            List<Design> designs,
            List<Room> rooms) {
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setOriginalPrice(productDto.getOriginalPrice());
        existingProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        existingProduct.setBrand(brand);
        existingProduct.setColours(colours);
        existingProduct.setDesigns(designs);
        existingProduct.setRooms(rooms);
        existingProduct.setSize(productDto.getSize());
        existingProduct.setWallpaperType(productDto.getWallpaperType());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setImageUrls(productDto.getImageUrls());
        existingProduct.setCategories(categories);
        existingProduct.setSlug(productDto.getSlug());
        existingProduct.setFeatured(productDto.isFeatured());
        return existingProduct;
    }


}
