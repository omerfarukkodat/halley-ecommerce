"use client";

import React, { useState } from "react";
import { Box, Typography, Button, Divider } from "@mui/material";
import ProductDescription from "@/components/product/productDetails/ProductDescription";
import {useRouter} from "next/navigation";

const ProductTabs = ({product}) => {
    const [selectedTab, setSelectedTab] = useState(0);
    const router = useRouter();


    const handleHowToMeasureClick = () => {
        router.push("/blog/nasil-yapilir")
    }

    const tabData = [
        { label: "Nasıl Yapılır?", content: (
                <Box sx={{
                    display: 'flex',
                    flexDirection: {xs: "column", sm: "column", md: "column", lg: "row", xl: "row"},

                }}>
                    <Box>
                        <Typography sx={{
                            color: "#9F1127",
                            fontSize: "24px",
                            fontWeight: "bold",
                            mb: 5,
                            mt: 4,
                            textAlign: "center",
                        }}>
                            Nasıl ölçülür?
                        </Typography>
                        <Box sx={{
                            display: "flex",
                            justifyContent: "center",
                            mb: 2,
                        }}>
                            <img

                                src="/product-guide-measure.svg"
                                alt="Nasıl Ölçülür?"
                                style={{
                                    width: "100%",
                                    maxWidth: "400px",
                                    height: "auto",
                                    maxHeight: "300px",
                                    borderRadius: "4px",
                                }}
                            />

                        </Box>

                        <Typography sx={{
                            color: "#202020",
                            fontSize: "16px",
                            fontWeight: 400,
                            mt: 7,
                            textAlign: "center",
                        }}>Duvarlarınızı nasıl ölçeceğinizi bilmiyor musunuz? Ölçüm
                            rehberimiz, standart duvarları, eğimli duvarları , kapı ve pencereli duvarları nasıl
                            ölçeceğinizi adım adım anlatıyor.
                        </Typography>
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                            }}
                        >
                            <Button
                                onClick={() => handleHowToMeasureClick()}
                                sx={{
                                    mt: 5,
                                    border: "1px solid #9F1127",
                                    backgroundColor: "white",
                                    cursor: "pointer",
                                }}
                            >
                                <Typography
                                    sx={{
                                        color: "#9F1127",
                                        fontSize: "18px",
                                        fontWeight: 400,
                                        textTransform: "none",
                                    }}
                                >
                                    Tam ölçüm rehberini okuyun
                                </Typography>
                            </Button>
                        </Box>
                    </Box>
                    <Divider orientation="vertical" flexItem sx={{display: {xs: "none", md: "block"}, mx: 2}}/>
                    <Divider orientation="horizontal" flexItem sx={{display: {xs: "block", md: "none"}, my: 2}}/>

                    <Box>
                        <Typography sx={{
                            color: "#9F1127",
                            fontSize: "24px",
                            fontWeight: "bold",
                            mb: 5,
                            mt: 4,
                            textAlign: "center",
                        }}>
                            Nasıl yapılır?
                        </Typography>
                        <Box sx={{
                            display: "flex",
                            justifyContent: "center",
                            mb: 2,
                        }}>
                            <img
                                src="/product-guide-install.svg"
                                alt="Nasıl Ölçülür?"
                                style={{
                                    width: "100%",
                                    maxWidth: "400px",
                                    height: "auto",
                                    maxHeight: "300px",
                                    borderRadius: "4px",
                                }}
                            />

                        </Box>
                        <Typography sx={{
                            color: "#202020",
                            fontSize: "16px",
                            fontWeight: 400,
                            mt: 7,
                            textAlign: "center",
                        }}>Duvar kağıtlarını nasıl uygulayacağınıza dair ayrıntılı uygulama rehberlerimizi inceleyin.
                            Duvar kağıdı malzeme tipi ve ebatlarıyla alakalı her türlü bilgiyi uygulama rehberimizden
                            bulabilirsiniz.

                        </Typography>
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                            }}
                        >
                            <Button
                                sx={{
                                    mt: 5,
                                    border: "1px solid #9F1127",
                                    backgroundColor: "white",
                                    cursor: "pointer",
                                }}
                            >
                                <Typography
                                    sx={{
                                        color: "#9F1127",
                                        fontSize: "18px",
                                        fontWeight: 400,
                                        textTransform: "none",
                                    }}
                                >
                                    Detaylı uygulama rehberi
                                </Typography>
                            </Button>
                        </Box>
                    </Box>
                </Box>
            ) },
        { label: "Ürün Bilgileri",
            content:
                <ProductDescription name={product.name} size={product.size} brand={product.brand} wallpaperType={product.wallpaperType} /> },
        { label: "Teslimat", content: <Typography variant="body2" sx={{fontWeight:500}}>
                 Siparişinizin ne zaman teslim edileceği, bulunduğunuz bölgeye göre değişiklik
                gösterebilir.Tüm siparişler sipariş tarihinden itibaren 48 saat içinde gönderilir.
                Gönderimden sonraki ortalama teslimat süresi 3-6 iş günüdür. Duvar kağıdından memnun kalmazsanız
                lütfen siparişinizi aldıktan hemen sonra bizimle iletişime geçin.
            </Typography> }
    ];

    return (
        <Box sx={{ mt: 20 }}>
            <Box sx={{ display: "flex", justifyContent: "center", border: "1px solid #ddd", borderRadius: 1 }}>
                {tabData.map((tab, index) => (
                    <Button key={index} onClick={() => setSelectedTab(index)} sx={{
                        flexGrow: 1,
                        backgroundColor: selectedTab === index ? "#9F1127" : "#ddd",
                        color: selectedTab === index ? "white" : "black",
                        fontSize: "21px",
                        fontWeight: "bold",
                        textTransform: "none",
                        borderRadius: 0
                    }}>
                        {tab.label}
                    </Button>
                ))}
            </Box>
            <Box sx={{ p: 2 }}>{tabData[selectedTab].content}</Box>
        </Box>
    );
};

export default ProductTabs;