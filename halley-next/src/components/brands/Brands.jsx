"use client";

import React, { useEffect, useState } from "react";
import { Box, CircularProgress, Typography } from "@mui/material";
import brandService from "@/services/brandService";
import { useRouter } from "next/navigation";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Navigation, Autoplay } from "swiper/modules";

const Brands = () => {
    const [brands, setBrands] = useState([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    const handleRedirect = (slug) => {
        router.push(`/marka/${slug}`);
    };

    useEffect(() => {
        const getBrands = async () => {
            const response = await brandService.getAllBrands();
            setBrands(response.data);
            setLoading(false);
        };
        getBrands();
    }, []);

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <Box sx={{ backgroundColor: "#ffffff" }}>
            <Typography
                variant="h4"
                sx={{
                    textAlign: "center",
                    fontWeight: "bold",
                    color: "#0F3460",
                    marginBottom: "40px",
                    fontSize: { xs: "24px", sm: "28px", md: "32px" },
                }}
            >
                Markalara Göre Ürünler
            </Typography>

            <Box sx={{ position: "relative", maxWidth: "100%" }}>
                <Swiper
                    modules={[Navigation, Autoplay]}
                    slidesPerView={1}
                    spaceBetween={20}
                    navigation

                    autoplay={{ delay: 2000 }}
                    breakpoints={{
                        1024: { slidesPerView: 4 },
                        768: { slidesPerView: 3 },
                        600: { slidesPerView: 2 },
                        480: { slidesPerView: 1 },
                    }}
                    threshold={20}
                >
                    {brands.map((brand, index) => (
                        <SwiperSlide key={index}>
                            <Box
                                sx={{
                                    textAlign: "center",
                                    display: "flex!important",
                                    justifyContent: "center",
                                    alignItems: "center",
                                    marginBottom: "22px",
                                }}
                            >
                                <img
                                    src={brand.imageUrl}
                                    alt={brand.name}
                                    onClick={() => handleRedirect(brand.slug)}
                                    style={{
                                        borderRadius: "50%",
                                        width: "150px",
                                        height: "150px",
                                        objectFit: "cover",
                                        boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)",
                                        transition: "transform 0.3s ease-in-out",
                                        cursor: "pointer",
                                    }}
                                    onMouseEnter={(e) =>
                                        (e.currentTarget.style.transform = "scale(1.1)")
                                    }
                                    onMouseLeave={(e) =>
                                        (e.currentTarget.style.transform = "scale(1)")
                                    }
                                />
                            </Box>
                        </SwiperSlide>
                    ))}
                </Swiper>
            </Box>
        </Box>
    );
};

export default Brands;
