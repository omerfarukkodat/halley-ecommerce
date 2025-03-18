"use client";
import React, { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import {
    Box,
    Select,
    MenuItem,
    CircularProgress,
    Typography,
    useMediaQuery,
    Pagination,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import ProductCard from "@/components/product/ProductCard";
import FilterComponent from "@/components/product/Filter/FilterComponent";
import {PaginationItem} from "@mui/lab";
import Divider from "@mui/material/Divider";

const ProductListingPage = ({
                                fetchDataFunction,
                                pageTitle,
                                description,
                                basePath,
                                sortOptions = [
                                    { value: "default", label: "Varsayılan" },
                                    { value: "price-asc", label: "Artan Fiyata Göre" },
                                    { value: "price-desc", label: "Azalan Fiyata Göre" },
                                    { value: "name", label: "A'dan Z'ye İsme Göre" },
                                    { value: "added-date", label: "En Yeniler" },
                                ],
                                visibleFilters = {
                                    price: true,
                                    brand: true,
                                    wallpaperType: true,
                                    wallpaperSize: true,
                                    category: true,
                                },
                                additionalFilters = {},
                            }) => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [sortOption, setSortOption] = useState("default");
    const [totalPages, setTotalPages] = useState(0);

    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const [isExpanded, setIsExpanded] = useState(false);

    useEffect(() => {
        const fetchProducts = async () => {
            setLoading(true);

            const filters = {
                page: searchParams.get("page") || 0,
                size: searchParams.get("size") || 24,
                sortBy: searchParams.get("sortBy") || "productCode",
                sortDirection: searchParams.get("sortDirection") || "desc",
                minPrice: searchParams.get("minPrice") || undefined,
                maxPrice: searchParams.get("maxPrice") || undefined,
                brand: searchParams.get("brand") || undefined,
                wallpaperType: searchParams.get("wallpaperType") || undefined,
                wallpaperSize: searchParams.get("wallpaperSize") || undefined,
                categoryId: searchParams.get("categoryId") || undefined,
                ...additionalFilters,
            };

            try {
                const response = await fetchDataFunction(filters);
                const { content, totalPages } = response.data;
                setProducts(content || []);
                setTotalPages(totalPages);
            } catch (error) {
                console.error("Error fetching products: ", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [searchParams, additionalFilters, fetchDataFunction]);

    const sortProducts = (products, option) => {
        switch (option) {
            case "price-asc":
                return [...products].sort((a, b) => a.discountedPrice - b.discountedPrice);
            case "price-desc":
                return [...products].sort((a, b) => b.discountedPrice - a.discountedPrice);
            case "name":
                return [...products].sort((a, b) => a.name.localeCompare(b.name));
            case "added-date":
                return [...products].sort(
                    (a, b) => new Date(a.createdTime) - new Date(b.createdTime)
                );
            default:
                return products;
        }
    };

    const handleSortChange = (e) => {
        const newSort = e.target.value;
        setSortOption(newSort);

        const currentParams = Object.fromEntries([...searchParams.entries()]);

        switch (newSort) {
            case "price-asc":
                currentParams.sortBy = "discountedPrice";
                currentParams.sortDirection = "asc";
                break;
            case "price-desc":
                currentParams.sortBy = "discountedPrice";
                currentParams.sortDirection = "desc";
                break;
            case "name":
                currentParams.sortBy = "name";
                currentParams.sortDirection = "asc";
                break;
            case "added-date":
                currentParams.sortBy = "createdTime";
                currentParams.sortDirection = "desc";
                break;
            default:
                currentParams.sortBy = "productCode";
                currentParams.sortDirection = "desc";
                break;
        }

        Object.keys(currentParams).forEach((key) => {
            if (currentParams[key] === "" || currentParams[key] === null) {
                delete currentParams[key];
            }
        });

        const queryString = new URLSearchParams(currentParams).toString();
        router.push(`${basePath}?${queryString}`);
    };

    const handleFilterChange = (newFilters) => {
        const currentParams = Object.fromEntries([...searchParams.entries()]);
        const updatedParams = { ...currentParams, ...newFilters };

        Object.keys(updatedParams).forEach((key) => {
            if (updatedParams[key] === "" || updatedParams[key] === null) {
                delete updatedParams[key];
            }
        });

        const queryString = new URLSearchParams(updatedParams).toString();
        router.push(`${basePath}?${queryString}`);
    };

    const handlePageChange = (event, newPage) => {
        const currentParams = Object.fromEntries([...searchParams.entries()]);
        currentParams.page = newPage - 1;

        Object.keys(currentParams).forEach((key) => {
            if (currentParams[key] === "" || currentParams[key] === null) {
                delete currentParams[key];
            }
        });

        const queryString = new URLSearchParams(currentParams).toString();
        router.push(`${basePath}?${queryString}`);
    };

    const sortedProducts = sortProducts(products, sortOption);

    return (
        <Box>
            {loading ? (
                <Box display="flex" justifyContent="center" alignItems="center" minHeight="700px">
                    <CircularProgress size={60} />
                </Box>
            ) : (


                <Box>
                    <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", width: "100%" }}>
                        <Typography variant="h3" sx={{ fontWeight: "700", textAlign: "center", width: "100%" }}>
                            {pageTitle}
                        </Typography>
                    <Typography gutterBottom variant="body1" sx={{
                        fontWeight: "700", textAlign: "left", width: "100%" }}>
                        Ürün Sayısı: {products.length}
                    </Typography>



                    <Divider sx={{  mb: 1, borderColor: "gray", width: "100%" }} />
                </Box>

                    {isMobile ? (
                        <>
                            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}
                            sx={{
                                mt:10,
                                mb:5
                            }}
                            >
                                <Box sx={{ flex: 1, mr: 1  }}>
                                    <FilterComponent
                                        onChange={handleFilterChange}
                                        visibleFilters={visibleFilters}
                                    />
                                </Box>

                                <Box sx={{ flex: 1, ml: 1 }}>
                                    <Select
                                        value={sortOption}
                                        onChange={handleSortChange}
                                        variant="outlined"
                                        sx={{ backgroundColor: "white" }}
                                    >
                                        {sortOptions.map((option) => (
                                            <MenuItem key={option.value} value={option.value}>
                                                {option.label}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </Box>
                            </Box>
                            <ProductGrid products={sortedProducts} isMobile={isMobile} />
                        </>
                    ) : (
                        <Box display="flex" gap={3} >
                            <FilterComponent
                                onChange={handleFilterChange}
                                visibleFilters={visibleFilters}
                            />
                            <Box flex={1}>
                                <Box display="flex" justifyContent="flex-end" mb={3}>
                                    <Select
                                        value={sortOption}
                                        onChange={handleSortChange}
                                        variant="outlined"
                                        sx={{ backgroundColor: "white" }}
                                    >
                                        {sortOptions.map((option) => (
                                            <MenuItem key={option.value} value={option.value}>
                                                {option.label}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </Box>
                                <ProductGrid products={sortedProducts} isMobile={isMobile} />
                            </Box>
                        </Box>
                    )}
                    <Box display="flex" justifyContent="center" mt={4}>
                        <Pagination
                            count={totalPages}
                            page={parseInt(searchParams.get("page") || 0) + 1}
                            onChange={handlePageChange}
                            shape="rounded"
                            size="large"
                            showFirstButton
                            showLastButton
                            boundaryCount={1}
                            siblingCount={1}
                            renderItem={(item) => {
                                const currentPage = parseInt(searchParams.get("page") || 0);
                                const totalPages = item.count;
                                let pageNumbers = [];

                                if (totalPages <= 3) {
                                    pageNumbers = [1, 2, 3].slice(0, totalPages);
                                } else {
                                    if (currentPage < 2) {
                                        pageNumbers = [1, 2, 3];
                                    } else if (currentPage > totalPages - 3) {
                                        pageNumbers = [totalPages - 2, totalPages - 1, totalPages];
                                    } else {
                                        pageNumbers = [currentPage, currentPage + 1, currentPage + 2];
                                    }
                                }

                                if (item.type === 'first' || item.type === 'last' || pageNumbers.includes(item.page)) {
                                    return (
                                        <PaginationItem
                                            {...item}
                                            sx={{
                                                color: "#7F8083",
                                                backgroundColor: "#ddd",
                                                borderRadius: 20,
                                                alignItems: "center",
                                                margin: "0 4px",
                                                mb: 5,
                                                fontSize: "1.2rem",
                                                "&.Mui-selected": {
                                                    backgroundColor: "#0F3476",
                                                    color: "white",
                                                    "&:hover": {
                                                        backgroundColor: "#0F3476",
                                                    },
                                                },
                                                "&:hover": {
                                                    backgroundColor: "#ccc",
                                                },
                                            }}
                                        />
                                    );
                                }
                                return null;
                            }}
                        />
                    </Box>

                </Box>
            )}
        </Box>
    );
};

const ProductGrid = ({ products, isMobile }) => (
    <Box
        mb={5}
        display="grid"
        gridTemplateColumns={{
            xs: "repeat(auto-fill, minmax(180px, 1fr))",
            sm: "repeat(auto-fill, minmax(250px, 1fr))",
            md: !isMobile && "repeat(auto-fill, minmax(250px, 1fr))",
            lg: !isMobile && "repeat(auto-fill, minmax(290px, 1fr))",
            xl: !isMobile && "repeat(auto-fill, minmax(330px, 1fr))",
        }}
        gap={1}
    >
        {products.map((product) => (
            <ProductCard key={product.id} product={product} />
        ))}
        {products.length === 0 && (
            <Box p={4} textAlign="center" width="100%">
                <Typography variant="h6" color="textSecondary">
                    Ürün bulunamadı
                </Typography>
            </Box>
        )}
    </Box>
);

export default ProductListingPage;