"use client";

import {useEffect, useState} from "react";
import {Box, Typography, CircularProgress} from "@mui/material";
import Image from "next/image";
import {Swiper, SwiperSlide} from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import {Pagination, Autoplay} from "swiper/modules";
import {useRouter} from "next/navigation";
import {productAttributeService} from "@/services/productAttributeService";

const ColourSlider = ({title}) => {
    const [colours, setColours] = useState([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();


    const handleColourClick = async (event,slug) => {
        event.preventDefault();

        router.push(`/renkler/${slug}`);
    };

    useEffect(() => {
        productAttributeService.getAllColour()
            .then((response) => {
                setColours(response.data || []);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Alt kategoriler alınamadı:", error);
                setLoading(false);
            });
    }, []);

    return (
        <Box sx={{width: "100%", maxWidth: 1200, mx: "auto", mt: 12}}>
            <Typography variant="h4" fontWeight="bold" mb={2} textAlign="center">
                {title}
            </Typography>

            {loading ? (
                <Box display="flex" justifyContent="center" alignItems="center" height={200}>
                    <CircularProgress/>
                </Box>
            ) : (
                <Swiper
                    slidesPerView={3}
                    spaceBetween={3}
                    pagination={{clickable: true, dynamicBullets: true}}
                    modules={[Pagination, Autoplay]}
                    autoplay={{delay: 3000, disableOnInteraction: false}}
                    breakpoints={{
                        320: {slidesPerView: 2},
                        600: {slidesPerView: 3},
                        900: {slidesPerView: 4},
                        1200: {slidesPerView: 5},
                        1500: {slidesPerView: 5},
                    }}
                    style={{padding: 0, margin: 0}}
                >
                    {colours.map((colour) => (
                        <SwiperSlide key={colour.id}>
                            <Box
                                sx={{
                                    width: "100%",
                                    maxHeight: "430px",
                                    display: "flex",
                                    flexDirection: "column",
                                    alignItems: "center",
                                }}
                            >
                                <Box sx={{position: "relative", maxWidth: "212px", maxHeight: "212px"}}>
                                    <Image
                                        src={colour.imageUrl || "/placeholder.png"}
                                        alt={colour.name}
                                        style={{objectFit: "cover", cursor: "pointer", width: "100%", height: "100%"}}
                                        quality={100}
                                        width={212}
                                        height={212}
                                        onClick={(event) => handleColourClick(event,colour.slug)}
                                    />
                                </Box>
                                <Typography
                                    href={`/renkler/${colour.slug}`}
                                    component="a"
                                    variant="body1"
                                    fontWeight="bold"
                                    textAlign="center"
                                    onClick={(event) => handleColourClick(event,colour.slug)}
                                    mt={1}
                                    sx={{
                                        fontSize: "20px",
                                        width: "100%",
                                        cursor: "pointer",
                                        textDecoration: 'none',
                                        color: 'black',

                                        '&:visited': {
                                            color: 'black',
                                        },
                                    }}
                                >
                                    {colour.name}
                                </Typography>
                            </Box>
                        </SwiperSlide>
                    ))}
                </Swiper>
            )}

            <style jsx global>{`
                .swiper-pagination {
                    position: relative;

                }

                .swiper-pagination-bullet {
                    background-color: #000;
                    margin: 24px 8px !important;
                }
            `}</style>
        </Box>
    );
};

export default ColourSlider;
