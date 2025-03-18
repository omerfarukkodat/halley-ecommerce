"use client";

import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import React from "react";
import Slider from "react-slick";
import Image from "next/image";
import { Box, useTheme, ThemeProvider, createTheme, useMediaQuery } from "@mui/material";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

function NextArrow(props) {
    const { onClick } = props;
    return (
        <ArrowForwardIosIcon
            onClick={onClick}
            className="custom-next-arrow"
            sx={{
                color: "#202020",
                backgroundColor: "white",
                borderRadius: "50%",
                height: "36px",
                width: "36px",
                position: "absolute",
                right: "16px",
                top: "50%",
                transform: "translateY(-50%)",
                cursor: "pointer",
                zIndex: 10,
                display: "none",
            }}
        />
    );
}

function PrevArrow(props) {
    const { onClick } = props;
    return (
        <ArrowBackIosNewIcon
            onClick={onClick}
            className="custom-prev-arrow"
            sx={{
                color: "#202020",
                fontSize: 12,
                backgroundColor: "white",
                borderRadius: "50%",
                height: "36px",
                width: "36px",
                position: "absolute",
                left: "16px",
                top: "50%",
                transform: "translateY(-50%)",
                cursor: "pointer",
                zIndex: 10,
                display: "none",
            }}
        />
    );
}

export default function HeroSlider() {
    const theme = useTheme();

    const settings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 3000,
        fade: true,
        nextArrow: <NextArrow />,
        prevArrow: <PrevArrow />,
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                },
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                },
            },
        ],
    };

    const themeConfig = createTheme({
        breakpoints: {
            values: {
                xs: 0,
                sm: 600,
                md: 960,
                lg: 1280,
                xl: 1920,
            },
        },
    });

    const isMobile = useMediaQuery(themeConfig.breakpoints.down("sm"));

    const images = [
        "https://www.adawall.com.tr/asset/img/Supersized/rumi_wallpaper_collection_-1.jpg?v=1",
        "https://www.adawall.com.tr/asset/img/Supersized/kalinka_wallpaper_collection_-3.jpg?v=1",
        "https://www.adawall.com.tr/asset/img/Supersized/kalinka_wallpaper_collection_-1.jpg?v=1",
    ];

    return (
        <ThemeProvider theme={themeConfig}>
            <Box
                sx={{
                    "@media (max-width: 600px)": {
                        padding: "0 0px",
                    },
                    position: "relative",
                    width: "90%",
                    maxWidth: "1200px",
                    margin: "0 auto",
                    padding: { xs: "0 16px", sm: "0 24px", md: "0 0px", lg: "0", xl: "0" },

                    marginTop: isMobile ? "94px" : "16px",
                    height: "480px",
                    "&:hover .custom-next-arrow, &:hover .custom-prev-arrow": {
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        zIndex: 10,
                        opacity: 1,
                    },
                    "& .custom-next-arrow, & .custom-prev-arrow": {
                        transition: "opacity 0.3s ease",
                        opacity: 0,
                    },
                }}
            >
                <Slider {...settings}>
                    {images.map((src, index) => (
                        <Box
                            key={index}
                            sx={{
                                position: "relative",
                                borderRadius: "10px",
                                height: "480px",
                                overflow: "hidden",
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                            }}
                        >
                            <Image
                                src={src}
                                alt={`Slide ${index + 1}`}
                                layout="fill"
                                objectFit="cover"
                                priority={index === 0}
                            />
                        </Box>
                    ))}
                </Slider>
            </Box>
        </ThemeProvider>
    );
}
