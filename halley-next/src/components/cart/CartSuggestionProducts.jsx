"use client";

import React from "react";
import productService from "@/services/productService";
import ProductList from "../product/ProductList";

const CartSuggestionProducts = ({ initialPage = 0, size = 10 }) => {
    const fetchProducts = (page, size) => {
        return productService.getProductsByCategoryId("yardimci-urunler", { page, size });
    };

    return (
        <ProductList
            title="Sepetinize Özel Yardımcı Ürünler"
            fetchFunction={fetchProducts}
            initialPage={initialPage}
            size={size}
        />
    );
};

export default CartSuggestionProducts;