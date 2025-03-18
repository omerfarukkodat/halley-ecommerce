"use client";

import productService from "@/services/productService";
import ProductListingPage from "@/components/product/ProductListingPage";
import {useParams} from "next/navigation";
import Breadcrumb from "@/components/common/BreadCrumb";
import React from "react";
import {Box} from "@mui/material";


const BrandProductsSearchPage = () => {

    const { slug } = useParams();


    return (
<Box>
    <Breadcrumb basePath={`/marka`} pageTitle={`${slug}`}/>
    <ProductListingPage
        fetchDataFunction={(filters) => productService.getProductsByBrand(slug,filters)}
        basePath={`/marka/${slug}`}
        pageTitle="Marka"
        visibleFilters={{
            price: true,
            brand: false,
            wallpaperType: true,
            wallpaperSize: true,
            category:true
        }}
        additionalFilters={{}}
    />
</Box>

    );

};


export default BrandProductsSearchPage;