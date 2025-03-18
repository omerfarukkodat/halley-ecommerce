'use client';

import {useEffect, useState} from 'react';
import {Grid, Card, CardMedia, Typography, Box} from '@mui/material';
import categoryService from '@/services/categoryService';
import {useRouter} from "next/navigation";

const CategoryGrid = () => {
    const [categories, setCategories] = useState([]);
    const router = useRouter();

    useEffect(() => {
        categoryService.getMainCategories()
            .then(response => {
                setCategories(response.data);
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
            });
    }, []);

    const handleCategoryClick = (slug) => {
        router.push(`/kategori/${slug}`);

    };

    return (
        <Grid container spacing={4} sx={{ padding: 4 }}>
            {categories.map((category, index) => (
                <Grid item xs={12} sm={index === 0 ? 12 : 6} md={index === 0 ? 8 : 4} key={category.categoryId}>
                    <Card
                        sx={{
                            height: '100%',
                            display: 'flex',
                            flexDirection: 'column',
                            borderRadius: 2,
                            overflow: 'hidden',
                            boxShadow: 3,
                            transition: 'transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out',
                            '&:hover': {
                                transform: 'scale(1.05)',
                                boxShadow: 6,
                            },
                            cursor: 'pointer',
                        }}
                        onClick={() => handleCategoryClick(category.slug)}
                    >
                        <Box sx={{ position: 'relative', height: index === 0 ? 400 : 200 }}>
                            <CardMedia
                                component="img"
                                sx={{
                                    height: '100%',
                                    width: '100%',
                                    objectFit: 'cover',
                                }}
                                image={category.imageUrl}
                                alt={category.categoryName}
                            />
                            <Box
                                sx={{
                                    position: 'absolute',
                                    bottom: 0,
                                    left: 0,
                                    right: 0,
                                    background: 'linear-gradient(to top, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.3) 70%,' +
                                        ' rgba(0,0,0,0) 100%)',
                                    padding: 2,
                                }}
                            >
                                <Typography
                                    variant="h5"
                                    component="div"
                                    sx={{
                                        color: 'white',
                                        fontWeight: 'bold',
                                        textAlign: 'center',
                                    }}
                                >
                                    {category.categoryName}
                                </Typography>
                            </Box>
                        </Box>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default CategoryGrid;
