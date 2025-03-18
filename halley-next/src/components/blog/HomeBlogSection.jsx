"use client";
import React, { useEffect, useState } from "react";
import { Typography, Paper, Box, Divider, Button, Grid, Container, CircularProgress } from "@mui/material";
import { blogService } from "@/services/blogService";
import { useRouter } from "next/navigation";

const HomeBlogSection = () => {
    const [blogPosts, setBlogPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const router = useRouter();

    const handleBlogPostClick = (event, slug) => {
        event.preventDefault();
        router.push(`/blog/${slug}`);
    }

    const handleBlogClick = (event) => {
        event.preventDefault();
        router.push("/blog");
    }

    useEffect(() => {
        const fetchBlogPosts = async () => {
            try {
                const response = await blogService.getAll({ page: 0, size: 4, sortBy: "createdDate",
                    sortDirection: "desc" });
                setBlogPosts(response.content);
            } catch (err) {
                setError(err.message || "Bir hata oluştu.");
            } finally {
                setLoading(false);
            }
        };

        fetchBlogPosts();
    }, []);

    if (loading) {
        return <CircularProgress size={16} sx={{ color: '#ffffff' }} />;
    }

    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

    return (
        <Container maxWidth="lg" sx={{ py: 4, mt: 7 }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, textAlign: "center", mb: 4 }}>
                Halley Blog
            </Typography>

            <Grid container spacing={2}>
                {blogPosts.map((post) => (
                    <Grid item xs={12} sm={6} md={3} key={post.id}>
                        <Paper elevation={1} sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
                            {post.imageUrl && (
                                <img
                                    src={post.imageUrl}
                                    alt={post.title}
                                    style={{
                                        width: "100%",
                                        height: "250px",
                                        objectFit: "cover",
                                        marginBottom: "20px",
                                    }}
                                />
                            )}
                            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, fontSize: 17 }}>
                                {post.title}
                            </Typography>
                            <Divider sx={{ mb: 2 }} />

                            <Typography
                                variant="body2"
                                sx={{
                                    fontWeight: 500,
                                    flexGrow: 1,
                                    mb: 3,
                                    display: "-webkit-box",
                                    WebkitBoxOrient: "vertical",
                                    WebkitLineClamp: 4,
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                }}
                            >
                                {post.content.split("\n").slice(0, 3).join("\n")}
                            </Typography>
                            <Button
                                onClick={(event) => handleBlogPostClick(event, post.slug)}
                                href={`/blog/${post.slug}`}
                                variant="contained"
                                sx={{
                                    backgroundColor: "#586277",
                                    alignSelf: "flex-start",
                                    width: "100%",
                                    "&:hover": {
                                        backgroundColor: "#0F3476",
                                    },
                                }}
                            >
                                Devamını Oku
                            </Button>
                        </Paper>
                    </Grid>
                ))}
            </Grid>

            <Box display="flex" justifyContent="center" mt={4}>
                <Button
                    onClick={handleBlogClick}
                    href="/blog"
                    variant="contained"
                    sx={{
                        backgroundColor: "#586277",
                        borderRadius: "20px",
                        fontSize: "1rem",
                        padding: "10px 30px",
                        "&:hover": {
                            backgroundColor: "#0F3476",
                        },
                    }}
                >
                    Tüm Blog Paylaşımları
                </Button>
            </Box>
        </Container>
    );
};

export default HomeBlogSection;