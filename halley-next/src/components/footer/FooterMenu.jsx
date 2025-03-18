"use client";

import React from 'react';
import Container from '@mui/material/Container';
import Grid2 from '@mui/material/Grid2';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import Link from '@mui/material/Link';
import Box from '@mui/material/Box';
import {useCategory} from "@/context/CategoryContext";
import {useRouter} from "next/navigation";



const FooterMenu = () => {
    const {categories} = useCategory();
    const router = useRouter();

    const handleRedirect = (event,slug) => {
        router.push(`/kategori/${slug}`);
    }

    const handleStaticRedirect = (event,slug) => {
        router.push(`/${slug}`);
    }

    const createSlug = (text) => {
        return text
            .toLowerCase()
            .replace(/ğ/g, 'g')
            .replace(/ü/g, 'u')
            .replace(/ş/g, 's')
            .replace(/ı/g, 'i')
            .replace(/ö/g, 'o')
            .replace(/ç/g, 'c')
            .replace(/ /g, '-')
            .replace(/[^a-z0-9-]/g, '');
    };




    return (
        <Box>

        <Box
            component="footer"
            sx={{
                position: "static",
                bottom: 0,
                right: 0,
                borderColor: "#ededed",
                backgroundColor: '#5899dd',
                color: 'white',
                pt: 6,
                pb: 4,
                width: '100%',
            }}
        >
            <Container maxWidth="lg" disableGutters>
                <Grid2 container spacing={4}>
                    <Grid2 xs={12} lg={4}
                           display="table"
                           flexDirection="column"
                           alignItems={{xs: 'left', lg: 'flex-start'}}
                           style={{marginLeft: '10px'}}
                    >
                        <Box
                            component="img"
                            src="https://res.cloudinary.com/dxhftwif6/image/upload/v1737228180/Untitled_10_abdw8v.png"
                            alt="Logo"
                            sx={{width: 144, height: 56.4, mb: 2,}}
                        />
                        <Typography color="#ffffff" textAlign={{xs: 'left', lg: 'left'}}>
                            Halley Dekorasyon<br/>
                            Kapaklı , Tekirdağ
                        </Typography>
                        <Divider sx={{width: '100%', my: 3, borderColor: '#ededed'}}/>
                        <Typography sx={{color: "#ffffff", fontWeight: "bold", mb: 1}}>Bizi Takip Edin!</Typography>
                        <Box display="flex" justifyContent={{xs: 'left', lg: 'flex-start'}} gap={2}>

                            <Link href="https://www.instagram.com" target="_blank" color="inherit">
                                <Box
                                    component="img"
                                    src="https://res.cloudinary.com/dxhftwif6/image/upload/v1730384335/4102579_applications_instagram_media_social_icon.png"
                                    alt="Instagram"
                                    sx={{width: 30, height: 30, '&:hover': {opacity: 0.7}}}
                                />
                            </Link>
                            <Link href="https://www.facebook.com" target="_blank" color="inherit">
                                <Box
                                    component="img"
                                    src="https://res.cloudinary.com/dxhftwif6/image/upload/v1730384334/4102573_applications_facebook_media_social_icon.png"
                                    alt="Facebook"
                                    sx={{width: 30, height: 30, '&:hover': {opacity: 0.7}}}
                                />
                            </Link>
                        </Box>
                    </Grid2>

                    <Grid2 xs={12} lg={8} style={{marginLeft: '10px'}}>
                        <Grid2 container spacing={4}>
                            <Grid2 xs={12} sm={4}>
                                <Typography variant="h6" gutterBottom sx={{color: '#ffffff', fontWeight: 'semibold'}}>
                                    Kategoriler
                                </Typography>
                                {categories.length > 0 ? (
                                    categories.map((category) => (
                                        <Typography
                                            onClick={(event) =>
                                                handleRedirect(event, category.slug)}
                                            href={`/kategori/${category.slug}`}
                                            component="a"
                                            underline="hover"
                                            key={category.categoryId}
                                            color="#ffffff"
                                            display="block"
                                            sx={{
                                                fontFamily: "Quicksand",

                                                textDecoration: 'none',

                                                '&:visited': {
                                                    color: "#ffffff",
                                                },
                                                mb: 1,
                                                '&:hover': {
                                                    color: '#000000'
                                                },

                                            }}
                                        >
                                            {category.categoryName}
                                        </Typography>
                                    ))
                                ) : (
                                    <Typography color="black">
                                        Kategoriler mevcut değil.
                                    </Typography>
                                )}
                            </Grid2>

                            <Grid2 xs={12} sm={4}>
                                <Typography variant="h6" gutterBottom sx={{color: '#ffffff', fontWeight: 'semibold'}}>
                                    Hakkımızda
                                </Typography>
                                {['İade ve Değişim Bilgileri', 'Kargo Bilgileri', 'Mesafeli Satış Sözleşmesi',
                                    'Uygulama Talimatları', 'Site Haritası'].map((text) => {
                                    const slug = createSlug(text);
                                    return (
                                        <Typography
                                            component="a"
                                            href={`/${slug}`}
                                            key={text}
                                            underline="hover"
                                            color="#ffffff"
                                            display="block"
                                            onClick={(event) =>
                                                handleStaticRedirect(event, slug)}
                                            sx={{
                                                    '&:hover': {
                                                        color: '#000000'
                                                    },
                                                    fontFamily: "Quicksand",
                                                    mb: 1,
                                                textDecoration: 'none',

                                                '&:visited': {
                                                    color: "#ffffff",
                                                },
                                                }}
                                            >
                                                {text}
                                        </Typography>
                                    );
                                })}
                            </Grid2>
                            <Grid2 xs={12} sm={4}>
                                <Typography variant="h6" gutterBottom sx={{color: '#ffffff', fontWeight: 'semibold'}}>
                                    Yardım
                                </Typography>
                                {['İletişim',
                                    'KVKK',
                                    'Kullanım Koşulları ve Gizlilik',
                                    'Çerez Aydınlatma Metni',
                                    'Sıkça Sorulan Sorular',
                                ].map((text) => {
                                    const slug = createSlug(text);
                                    return (
                                        <Typography
                                            component="a"
                                            href={`/${slug}`}
                                            key={text}
                                            underline="hover"
                                            color="#ffffff"
                                            display="block"
                                            onClick={(event) =>
                                                handleStaticRedirect(event, slug)}
                                            sx={{
                                                '&:hover': {
                                                    color: '#000000'
                                                },
                                                fontFamily: "Quicksand",
                                                mb: 1,
                                                textDecoration: 'none',

                                                '&:visited': {
                                                    color: "#ffffff",
                                                },
                                            }}
                                        >
                                            {text}
                                        </Typography>
                                    );
                                })}
                            </Grid2>
                        </Grid2>
                    </Grid2>
                </Grid2>
            </Container>

        </Box>
    <Box sx={{ textAlign: 'center', py: 2,bottom:0, backgroundColor: '#f5f5f5' }}>
        <Typography variant="body1" color="textSecondary">
            © 2025 HalleyWall. Tüm hakları saklıdır.
        </Typography>
    </Box>
        </Box>

    );
};

export default FooterMenu;