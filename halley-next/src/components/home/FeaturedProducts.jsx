"use client";

import React from "react";
import productService from "@/services/productService";
import "swiper/css";
import "swiper/css/navigation";
import ProductSlider from "@/components/home/ProductSlider";
import ElectricBoltIcon from "@mui/icons-material/ElectricBolt";


    const FeaturedProducts = ({ page, size }) => (
        <ProductSlider
            fetchService={() => productService.getFeaturedProducts(page, size)}
            title="Öne Çıkan Ürünler"
            icon={<ElectricBoltIcon sx={{ fontSize: "28px", color: "#ffffff" }} />}
            backgroundImage="/2.jpg"
            redirectPath="/one-cikan-urunler"
        />
    );


export default FeaturedProducts;