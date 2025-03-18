"use client";

import React, { useEffect, useState, useRef } from "react";
import { Box, Button, Typography } from "@mui/material";
import ProductCard from "../product/ProductCard";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import SliderArrow from "@/components/home/slider/sliderArrow/SliderArrow";
import "swiper/css";
import "swiper/css/navigation";
import { useRouter } from "next/navigation";
import Image from "next/image";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";

const ProductSlider = ({
                           fetchService,
                           title,
                           icon,
                           backgroundImage,
                           redirectPath,
                       }) => {
    const [products, setProducts] = useState([]);
    const swiperRef = useRef(null);
    const router = useRouter();

    const handleRedirect = () => {
        router.push(redirectPath);
    };

    useEffect(() => {
        fetchService()
            .then((response) => {
                const data =  response.data?.content || [];
                setProducts(data);
            })
            .catch((error) => console.error("Error fetching products:", error));
    }, [fetchService]);

    return (
        <Box sx={{ mt: 10, mb: 10, position: "relative" }}>
            <Box
                sx={{
                    position: "relative",
                    height: { xs: "105px", sm: "140px", md: "170px", lg: "195px" },
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    overflow: "hidden",
                    boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
                }}
            >
                <Image
                    src={backgroundImage}
                    alt={title}
                    onClick={handleRedirect}
                    style={{ cursor: "pointer" }}
                    layout="fill"
                    objectFit="cover"
                />

                <Typography
                    variant="h4"
                    sx={{
                        fontWeight: "bold",
                        color: "#ffffff",
                        textShadow: "0 2px 4px rgba(0,0,0,0.6)",
                        cursor: "pointer",
                        zIndex: 2,
                        display: "flex",
                        alignItems: "center",
                        gap: 1,
                    }}
                    onClick={handleRedirect}
                >
                    {icon}
                    {title}
                </Typography>

                <Button
                    onClick={handleRedirect}
                    endIcon={<ArrowForwardIcon />}
                    sx={{
                        position: "absolute",
                        bottom: "10px",
                        right: "10px",
                        backgroundColor: "#ffffffbd",
                        color: "#000000",
                        fontWeight: "bold",
                        textTransform: "none",
                        borderRadius: "5px",
                        padding: "6px 14px",
                        boxShadow: "0 2px 8px rgba(0, 0, 0, 0.2)",
                        transition: "background-color 0.3s ease",
                        "&:hover": {
                            backgroundColor: "#ffffff",
                            color: "#000000",
                        },
                        display: { xs: "none", sm: "flex" },
                    }}
                >
                    İncele
                </Button>
            </Box>

            <Swiper
                style={{ marginTop: "10px" }}
                spaceBetween={5}
                slidesPerView={2}
                onSwiper={(swiper) => (swiperRef.current = swiper)}
                modules={[Navigation]}
                breakpoints={{
                    0: { slidesPerView: 2 },
                    600: { slidesPerView: 2 },
                    960: { slidesPerView: 3 },
                    1280: { slidesPerView: 4 },
                    1960: { slidesPerView: 5 },
                }}
            >
                {products.map((product) => (
                    <SwiperSlide key={product.id}>
                        <ProductCard product={product} />
                    </SwiperSlide>
                ))}
            </Swiper>

            <SliderArrow
                direction="prev"
                onClick={() => swiperRef.current?.slidePrev()}
            />
            <SliderArrow
                direction="next"
                onClick={() => swiperRef.current?.slideNext()}
            />
        </Box>
    );
};

export default ProductSlider;