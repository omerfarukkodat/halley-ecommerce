"use client";

import React, { useEffect, useState } from "react";
import { useParams, useSearchParams } from "next/navigation";
import {Box, Button, CircularProgress, Typography,} from "@mui/material";
import productService from "@/services/productService";

import ProductListingPage from "@/components/product/ProductListingPage";
import {productAttributeService} from "@/services/productAttributeService";
import Breadcrumb from "@/components/common/BreadCrumb";

const RoomProductsPage = () => {
    const searchParams = useSearchParams();
    const { slug } = useParams();
    const [loading, setLoading] = useState(true);
    const [isExpanded, setIsExpanded] = useState(false);
    const [showProducts, setShowProducts] = useState(false);
    const [design , setDesign] = useState(null);


    const handleReadMore = () => {
        setIsExpanded(!isExpanded);
    };

    useEffect(() => {
        const scrollY = window.scrollY;

        const timeout = setTimeout(() => {
            window.scrollTo(0, scrollY);
            setShowProducts(true);
            setLoading(false);
        }, 400);

        return () => clearTimeout(timeout);
    }, [slug,searchParams]);


    useEffect(() => {
        if (slug) {
            productAttributeService
                .getDesignBySlug(slug)
                .then((response) => {
                    const data = response.data;
                    setDesign(data || []);
                    setLoading(false);
                })
                .catch((error) => {
                    console.error("Kategori yolları alınırken hata oluştu:", error);
                    setLoading(false);
                });
        }
    }, [slug]);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="800px">
                <CircularProgress size={21} />
            </Box>
        );
    }

    return (
        <Box sx={{ margin: "0 auto" ,minHeight:"800px" }}>
            <Box>
                <Breadcrumb pageTitle={design.name} basePath={`/${slug}`} />

            </Box>
            <Box>
                <Typography variant="h4" mb={4} sx={{ fontWeight: 700, color: "#101010", textAlign: "center" }}>
                    {design.name}
                </Typography>
                <Box>
                    <Typography
                        variant="body2"
                        mb={4}
                        sx={{ fontWeight: 500, color: "#000000", textAlign: "center" }}
                    >
                        {isExpanded ? design.description || "" : `${(design.description || "").slice(0, 100)}...`}
                    </Typography>
                    <Box display="flex" justifyContent="center" mb={4}>
                        <Button
                            variant="contained"
                            onClick={handleReadMore}
                            sx={{
                                borderRadius: "20px",
                                textTransform: "none",
                                backgroundColor: "#0F3460",
                                '&:hover': {
                                    backgroundColor: "#0F3460",
                                },
                            }}
                        >
                            {isExpanded ? "Azalt" : "Devamını Oku"}
                        </Button>
                    </Box>
                </Box>
            </Box>





            {showProducts ? (
                <ProductListingPage
                    fetchDataFunction={(filters) => productService.getByDesignSlug(slug, filters)}
                    basePath={`/desenler/${slug}`}
                    visibleFilters={{
                        price: true,
                        brand: true,
                        wallpaperType: true,
                        wallpaperSize: true,
                        category: false
                    }}
                    additionalFilters={{}}
                />
            ) : (
                <Box display="flex" justifyContent="center" alignItems="center" minHeight="800px">
                    <CircularProgress size={24} />
                </Box>
            )}
        </Box>
    );
};

export default RoomProductsPage;