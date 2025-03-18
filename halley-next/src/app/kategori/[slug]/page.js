"use client";

import React, { useEffect, useState } from "react";
import { useParams, useSearchParams } from "next/navigation";
import { Box, Button, CircularProgress, Typography } from "@mui/material";
import productService from "../../../services/productService";
import CategoryBreadCrumb from "@/components/category/CategoryBreadCrumb";
import categoryService from "@/services/categoryService";
import ProductListingPage from "@/components/product/ProductListingPage";

const CategoryProductsPage = () => {
    const searchParams = useSearchParams();
    const { slug } = useParams();
    const [loading, setLoading] = useState(true);
    const [categoryPaths, setCategoryPaths] = useState([]);
    const [isExpanded, setIsExpanded] = useState(false);
    const [showProducts, setShowProducts] = useState(false);


    const handleReadMore = () => {
        setIsExpanded(!isExpanded);
    };



    useEffect(() => {
        if (slug) {
            categoryService
                .getCategoryPaths(slug)
                .then((response) => {
                    const data = response.data;
                    setCategoryPaths(data || []);
                    setLoading(false);
                })
                .catch((error) => {
                    console.error("Kategori yolları alınırken hata oluştu:", error);
                    setCategoryPaths([]);
                    setLoading(false);
                });
        }
    }, [slug]);

    useEffect(() => {
        const scrollY = window.scrollY;

        const timeout = setTimeout(() => {
            window.scrollTo(0, scrollY);
            setShowProducts(true);
            setLoading(false);
        }, 400);

        return () => clearTimeout(timeout);
    }, [slug,searchParams]);

    if (loading ) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="800px">
                <CircularProgress size={21} />
            </Box>
        );
    }
    const currentCategory = categoryPaths.length > 0 ? categoryPaths[categoryPaths.length - 1] : null;


    return (
        <Box sx={{ margin: "0 auto" ,minHeight:"800px" }}>
            <Box>
                {categoryPaths.length > 0 ? (
                    <CategoryBreadCrumb categoryPaths={categoryPaths} />
                ) : (
                    <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "21px" }}>
                        <CircularProgress size={21} />
                    </Box>
                )}
            </Box>

            <Box sx={{
            }}>
                <Typography variant="h4" mb={4} sx={{ fontWeight: 700, color: "#101010", textAlign: "center" }}>
                    {currentCategory.name}
                </Typography>
                <Box>
                    <Typography
                        variant="body2"
                        mb={4}
                        sx={{

                            fontWeight: 500, color: "#000000", textAlign: "center" }}
                    >
                        {isExpanded ? currentCategory.description || "" : `${(currentCategory.description || "").slice(0, 100)}...`}
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
                    fetchDataFunction={(filters) => productService.getProductsByCategoryId(slug, filters)}
                    basePath={`/kategori/${slug}`}
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

export default CategoryProductsPage;