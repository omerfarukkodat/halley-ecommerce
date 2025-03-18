"use client";
import React, { useState, useEffect } from "react";
import {
    Box, Button,
    Card,
    CardContent,
    Drawer,
    IconButton,
    useMediaQuery,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import FilterListIcon from "@mui/icons-material/FilterList";
import CloseIcon from "@mui/icons-material/Close";
import { getCategories } from "@/services/categoryService";
import { getAllBrands } from "@/services/brandService";
import FilterContent from "./FilterContent";

const FilterComponent = ({ onChange, visibleFilters }) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const [filters, setFilters] = useState({
        minPrice: "",
        maxPrice: "",
        brand: "",
        wallpaperType: "",
        wallpaperSize: "",
        categoryId: "",
    });
    const [brands, setBrands] = useState([]);
    const [loadingBrands, setLoadingBrands] = useState(true);
    const [categories, setCategories] = useState([]);
    const [loadingCategories, setLoadingCategories] = useState(visibleFilters.category || false);
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    useEffect(() => {
        getAllBrands()
            .then((res) => {
                setBrands(res.data || []);
                setLoadingBrands(false);
            })
            .catch(() => setLoadingBrands(false));
    }, [visibleFilters.brand]);

    useEffect(() => {
        if (visibleFilters.category) {
            setLoadingCategories(true);
            getCategories()
                .then((res) => {
                    setCategories(res.data || []);
                    setLoadingCategories(false);
                })
                .catch(() => setLoadingCategories(false));
        }
    }, [visibleFilters.category]);

    const applyFilters = () => {
        const applied = Object.fromEntries(
            Object.entries(filters).map(([key, value]) => [key, value || null])
        );
        onChange(applied);
    };

    return (
        <>
            {isMobile ? (
                <Box>
                    <Box display="flex" justifyContent="flex-end" >
                        <Button
                            variant="contained"
                            onClick={() => setIsDrawerOpen(true)}
                            startIcon={<FilterListIcon />}
                            sx={{
                                height:56,
                                width:128.59,
                                fontWeight:600,
                                fontSize: "16px",
                                borderColor:"#ddd",
                                backgroundColor: "#ffffff",
                                color: "#000000",
                                textTransform: "none",
                                borderRadius: "4px",
                                '&:hover': {
                                    backgroundColor: "#ffffff",
                                },
                            }}
                        >
                            Filtrele
                        </Button>
                    </Box>
                    <Drawer
                        anchor="left"
                        open={isDrawerOpen}
                        onClose={() => setIsDrawerOpen(false)}
                        variant="temporary"
                    >
                        <Box sx={{ position: "relative", width: "100%", height: "100%" }}>
                            <IconButton
                                onClick={() => setIsDrawerOpen(false)}
                                sx={{
                                    position: "absolute",
                                    right: 8,
                                    top: 8,
                                    zIndex: 1,
                                    color: "black",
                                }}
                            >
                                <CloseIcon sx={{color:"#000000" , fontSize: "32px"}} />
                            </IconButton>

                            <FilterContent
                                filters={filters}
                                setFilters={setFilters}
                                applyFilters={applyFilters}
                                brands={brands}
                                loadingBrands={loadingBrands}
                                visibleFilters={visibleFilters}
                                categories={categories}
                                loadingCategories={loadingCategories}
                            />
                        </Box>
                    </Drawer>
                </Box>
            ) : (
                <Box width={300}>
                    <Card>
                        <CardContent>
                            <FilterContent
                                filters={filters}
                                setFilters={setFilters}
                                applyFilters={applyFilters}
                                brands={brands}
                                loadingBrands={loadingBrands}
                                visibleFilters={visibleFilters}
                                categories={categories}
                                loadingCategories={loadingCategories}
                            />
                        </CardContent>
                    </Card>
                </Box>
            )}
        </>
    );
};

export default FilterComponent;