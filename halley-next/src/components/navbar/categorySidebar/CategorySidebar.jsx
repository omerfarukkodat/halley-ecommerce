"use client";

import React, { useState, useCallback, memo, useMemo, useEffect } from "react";
import { useRouter } from "next/navigation";
import {
    Box,
    Button,
    useTheme,
    useMediaQuery, Collapse,
} from "@mui/material";
import { useCategory } from "@/context/CategoryContext";
import { productAttributeService } from "@/services/productAttributeService";
import CategoryDropdown from "./CategoryDropdown";
import Divider from "@mui/material/Divider";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";

const CategorySidebar = memo(() => {
    const [wallpaperHovered, setWallpaperHovered] = useState(false);
    const [roomsHovered, setRoomsHovered] = useState(false);
    const [designHovered, setDesignHovered] = useState(false);
    const [colourHovered, setColourHovered] = useState(false);
    const [hoveredCategoryId, setHoveredCategoryId] = useState(null);
    const router = useRouter();
    const { categories, loading } = useCategory();
    const [colours, setColours] = useState([]);
    const [designs, setDesigns] = useState([]);
    const [rooms, setRooms] = useState([]);

    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("1025"));

    useEffect(() => {
        const fetchData = async () => {
            try {
                const coloursResponse = await productAttributeService.getAllColour();
                const designsResponse = await productAttributeService.getAllDesign();
                const roomsResponse = await productAttributeService.getAllRoom();

                setColours(coloursResponse.data);
                setDesigns(designsResponse.data);
                setRooms(roomsResponse.data);
            } catch (error) {
                console.error("Veri çekme hatası:", error);
            }
        };

        fetchData();
    }, []);

    const handleCategoryClick = useCallback((slug) => {
        router.push(`/kategori/${slug}`);
        setWallpaperHovered(false);
        setRoomsHovered(false);
    }, [router]);

    const handleSubCategoryClick = useCallback((slug) => {
        router.push(`/kategori/${slug}`);
        setWallpaperHovered(false);
        setRoomsHovered(false);
    }, [router]);

    const handleRoomClick = useCallback((slug) => {
        router.push(`/odalar/${slug}`);
        setWallpaperHovered(false);
        setRoomsHovered(false);
    }, [router]);

    const handleDesignClick = useCallback((slug) => {
        router.push(`/desenler/${slug}`);
        setWallpaperHovered(false);
        setDesignHovered(false);
    }, [router]);

    const handleColourClick = useCallback((slug) => {
        router.push(`/renkler/${slug}`);
        setWallpaperHovered(false);
        setColourHovered(false);
    }, [router]);

    const staticLinks = useMemo(() => [
        { name: "Blog", path: "/blog" },
        { name: "Sayfalar", path: "/sayfalar" },
        { name: "Sizden Gelenler", path: "/sizden-gelenler" },
    ], []);

    if (isMobile) {
        return null;
    }

    return (
        <Box
            display="flex"
            marginTop="64px"
            height="40px"
            flexGrow={1}
            alignItems="center"
            sx={{
                zIndex: 100,
                border: "1px solid gray",
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                paddingX: 2,
                gap: 4
            }}
        >
            <Box
                onMouseEnter={() => setWallpaperHovered(true)}
                onMouseLeave={() => setWallpaperHovered(false)}
                sx={{ position: "relative", height: "100%", display: "flex", alignItems: "center" }}
            >
                <Button
                    sx={{
                        color: "#0F3460",
                        fontWeight: "bold",
                        fontSize: "16px",
                        textTransform: "none",
                        fontFamily: "Quicksand",
                        ":hover": { color: "orange" }
                    }}
                >
                    Duvar Kağıtları
                </Button>

                <Collapse
                    in={wallpaperHovered}
                    sx={{
                        position: "absolute",
                        top: "100%",
                        left: 0,
                        zIndex: 10,
                        minWidth: 250
                    }}
                >
                    <Box
                        sx={{
                            bgcolor: "white",
                            boxShadow: 3,
                            borderRadius: 1,
                            mt: 1,
                            py: 1
                        }}
                    >
                        {categories.map((category, index) => (
                            <Box key={category.categoryId}>
                                <Box
                                    onMouseEnter={() => setHoveredCategoryId(category.categoryId)}
                                    onMouseLeave={() => setHoveredCategoryId(null)}
                                    sx={{ position: "relative" }}
                                >
                                    <Button
                                        onClick={() => handleCategoryClick(category.slug)}
                                        fullWidth
                                        sx={{
                                            justifyContent: "space-between",
                                            px: 3,
                                            color: "#0F3460",
                                            fontWeight: "bold",
                                            textTransform: "none",
                                            fontFamily: "Quicksand",
                                            ":hover": {
                                                bgcolor: "#f5f5f5",
                                                color: "orange",
                                                "& .arrow-icon": {
                                                    color: "orange"
                                                }
                                            }
                                        }}
                                    >
                                        {category.categoryName}
                                        <KeyboardArrowRightIcon
                                            className="arrow-icon"
                                            sx={{
                                                fontSize: 18,
                                                color: "#0F3460",
                                                ml: 1
                                            }}
                                        />
                                    </Button>

                                    <Collapse
                                        in={hoveredCategoryId === category.categoryId}
                                        sx={{
                                            position: "absolute",
                                            left: "100%",
                                            top: 0,
                                            zIndex: 10
                                        }}
                                    >
                                        <Box
                                            sx={{
                                                bgcolor: "white",
                                                boxShadow: 3,
                                                borderRadius: 1,
                                                ml: 1,
                                                minWidth: 250
                                            }}
                                        >
                                            {category.subCategories?.map((subCategory, subIndex) => (
                                                <Box key={subCategory.categoryId}>
                                                    <Button
                                                        onClick={() => handleSubCategoryClick(subCategory.slug)}
                                                        fullWidth
                                                        sx={{
                                                            justifyContent: "flex-start",
                                                            px: 3,
                                                            color: "#0F3460",
                                                            fontWeight: "bold",
                                                            textTransform: "none",
                                                            fontFamily: "Quicksand",
                                                            ":hover": {
                                                                bgcolor: "#f5f5f5",
                                                                color: "orange"
                                                            }
                                                        }}
                                                    >
                                                        {subCategory.categoryName}
                                                    </Button>
                                                    {subIndex < category.subCategories.length - 1 && (
                                                        <Divider sx={{ mx: 2 }} />
                                                    )}
                                                </Box>
                                            ))}
                                        </Box>
                                    </Collapse>
                                </Box>
                                {index < categories.length - 1 && (
                                    <Divider sx={{ mx: 2 }} />
                                )}
                            </Box>
                        ))}
                    </Box>
                </Collapse>
            </Box>

            <CategoryDropdown
                label="Odalar"
                items={rooms}
                isHovered={roomsHovered}
                onMouseEnter={() => setRoomsHovered(true)}
                onMouseLeave={() => setRoomsHovered(false)}
                onClickItem={handleRoomClick}
            />

            <CategoryDropdown
                label="Renkler"
                items={colours}
                isHovered={colourHovered}
                onMouseEnter={() => setColourHovered(true)}
                onMouseLeave={() => setColourHovered(false)}
                onClickItem={handleColourClick}
            />

            <CategoryDropdown
                label="Desenler"
                items={designs}
                isHovered={designHovered}
                onMouseEnter={() => setDesignHovered(true)}
                onMouseLeave={() => setDesignHovered(false)}
                onClickItem={handleDesignClick}
            />

            {staticLinks.map((link) => (
                <Button
                    key={link.name}
                    sx={{
                        color: "#0F3460",
                        fontWeight: "bold",
                        fontSize: "16px",
                        textTransform: "none",
                        fontFamily: "Quicksand",
                        ":hover": {
                            color: "orange",
                            "&:after": {
                                content: '""',
                                display: 'block',
                                width: '100%',
                                height: 2,
                                position: 'absolute',
                                bottom: 0,
                                left: 0
                            }
                        },
                        position: 'relative'
                    }}
                    onClick={() => router.push(link.path)}
                >
                    {link.name}
                </Button>
            ))}
        </Box>
    );
});

export default CategorySidebar;