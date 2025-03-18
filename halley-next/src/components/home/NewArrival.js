
"use client";
import React from "react";
import ProductSlider from "./ProductSlider";
import productService from "@/services/productService";
import ElectricBoltIcon from "@mui/icons-material/ElectricBolt";

const NewArrivalProducts = ({ page, size }) => (
    <ProductSlider
        fetchService={() => productService.getLastAddedProducts(page, size)}
        title="Son Eklenen Ürünler"
        icon={<ElectricBoltIcon sx={{ fontSize: "28px", color: "#ffffff" }} />}
        backgroundImage="https://res.cloudinary.com/dxhftwif6/image/upload/v1740168499/pngtree-high-resolution-capture-of-marble-s-natural-texture-glossy-slab-marbel-picture-image_13417576_knarxc.png"
        redirectPath="/son-eklenenler"
    />
);

export default NewArrivalProducts;