"use client";

import React, { useEffect, useState, useRef } from "react";
import { Box, Typography, Button, useTheme } from "@mui/material";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import { useRouter } from "next/navigation";
import { Navigation } from "swiper/modules";
import categoryService from "@/services/categoryService";
import WidgetsIcon from "@mui/icons-material/Widgets";

const CategorySwiper = () => {
    const [categories, setCategories] = useState([]);
    const router = useRouter();
    const swiperRef = useRef(null);
    const theme = useTheme();

    useEffect(() => {
        categoryService
            .getCategories()
            .then((response) => {
                const fetchedCategories = response.data || [];
                const flatCategories = fetchedCategories.flatMap((category) => [
                    {
                        categoryId: category.categoryId,
                        categoryName: category.categoryName,
                        slug: category.slug,
                        icon: "/wallpaper.png",
                    },
                    ...(category.subCategories || []).map((subCategory) => ({
                        categoryId: subCategory.categoryId,
                        categoryName: subCategory.categoryName,
                        slug: subCategory.slug,
                        icon:  "/wallpaper.png",
                    })),
                ]);
                setCategories(flatCategories);
            })
            .catch((error) => {
                console.error("Error fetching categories:", error);
            });
    }, []);

    const handleCategoryClick = (slug) => {
        router.push(`/kategori/${slug}`);
    };

    return (
        <Box sx={{ mt: 20, mb: 20 }}>
            <Typography
                variant="h5"
                sx={{
                    fontWeight: "bold",
                    mb: 3,
                    textAlign: "center",
                    color: theme.palette.text.primary,
                }}
            >
                <WidgetsIcon sx={{ fontSize: "28px", verticalAlign: "middle", mr: 1 }} />
                Kategoriler
            </Typography>
            <Swiper
                ref={swiperRef}
                spaceBetween={15}
                slidesPerView={2}
                onSwiper={(swiper) => (swiperRef.current = swiper)}

                navigation
                modules={[Navigation]}
                breakpoints={{
                    0: { slidesPerView: 1 },
                    600: { slidesPerView: 2 },
                    960: { slidesPerView: 3 },
                    1280: { slidesPerView: 5 },
                }}
                style={{
                    padding: "0 10px",
                }}
            >
                {categories.map((category) => (
                    <SwiperSlide key={category.categoryId}>
                        <Box
                            onClick={() => handleCategoryClick(category.slug)}
                            sx={{
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center",
                                textAlign: "center",
                                cursor: "pointer",
                                borderRadius: "8px",
                                overflow: "hidden",
                                boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                                padding: "10px",
                                backgroundColor: theme.palette.background.paper,
                            }}
                        >
                            <Box
                                component="img"
                                src={category.icon}
                                alt={category.categoryName}
                                sx={{
                                    width: "50px",
                                    height: "50px",
                                    mb: 1,
                                }}
                            />
                            <Typography
                                variant="subtitle1"
                                sx={{ fontWeight: "bold", color: theme.palette.text.primary }}
                            >
                                {category.categoryName}
                            </Typography>
                        </Box>
                    </SwiperSlide>
                ))}
            </Swiper>
        </Box>
    );
};

export default CategorySwiper;
