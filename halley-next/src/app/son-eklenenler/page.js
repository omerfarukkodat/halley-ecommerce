"use client";

import productService from "@/services/productService";
import ProductListingPage from "@/components/product/ProductListingPage";
import React, {useEffect, useState} from "react";
import {useParams, useSearchParams} from "next/navigation";
import {Box, CircularProgress, } from "@mui/material";
import Breadcrumb from "@/components/common/BreadCrumb";


const LastAddedProductsPage = () => {

    const searchParams = useSearchParams();
    const { slug } = useParams();
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        setLoading(false);


    }, [slug, searchParams]);







    if (loading) {
        return (
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                height="80vh"
            >
                <CircularProgress size={60} />
            </Box>
        );
    }
    return (
        <Box>
            <Breadcrumb basePath={`/son-eklenenler`} pageTitle={`Son Eklenen Ürünler`}/>
            <ProductListingPage
                fetchDataFunction={(filters) => productService.getLastAddedProducts(filters)}
                basePath="/son-eklenenler"
                pageTitle="Son Eklenen Ürünler"
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

    );

};


export default LastAddedProductsPage;