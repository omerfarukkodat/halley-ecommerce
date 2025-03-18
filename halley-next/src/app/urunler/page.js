"use client";

import productService from "@/services/productService";
import ProductListingPage from "@/components/product/ProductListingPage";
import {Box} from "@mui/material";
import Breadcrumb from "@/components/common/BreadCrumb";
import React from "react";


const AllProductsPage = () => {
    return (
        <Box>
            <Box>
                <Breadcrumb pageTitle={"Tüm Ürünler"} basePath={`Tüm Ürünler`} />
            </Box>
            <Box>
                <ProductListingPage
                    fetchDataFunction={(filters) => productService.getAllProducts(filters)}
                    basePath="/urunler"
                    pageTitle="Tüm Ürünler"
                    visibleFilters={{
                        price: true,
                        brand: true,
                        wallpaperType: true,
                        wallpaperSize: true,
                        category:true
                    }}
                    additionalFilters={{}}
                />
            </Box>
        </Box>


    );

};


export default AllProductsPage;