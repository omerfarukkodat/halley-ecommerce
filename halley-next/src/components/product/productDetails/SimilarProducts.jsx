"use client";

import React from "react";
import productService from "@/services/productService";
import ProductList from "../ProductList";

const SimilarProducts = ({ productId, initialPage = 0, size = 10 }) => {
    const fetchProducts = (page, size) => {
        return productService.getSimilarProducts(productId, { page, size });
    };

    return (
        <ProductList
            title="İncelediğiniz Ürüne Benzer Ürünler"
            fetchFunction={fetchProducts}
            initialPage={initialPage}
            size={size}
        />
    );
};

export default SimilarProducts;