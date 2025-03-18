
"use client";
import React from "react";
import ProductSlider from "./ProductSlider";
import productService from "@/services/productService";
import ElectricBoltIcon from "@mui/icons-material/ElectricBolt";

const DiscountedProducts = ({ page, size }) => (
    <ProductSlider
        fetchService={() => productService.getDiscountedProducts(page, size)}
        title="İndirimli Ürünler"
        icon={<ElectricBoltIcon sx={{ fontSize: "28px", color: "#ffffff" }} />}
        backgroundImage="https://res.cloudinary.com/dxhftwif6/image/upload/v1737225933/vecteezy_abstract-marble-marbled-stone-ink-liquid-fluid-pain_47005452_srfrhy.jpg"
        redirectPath="/indirimli-urunler"
    />
);

export default DiscountedProducts;


