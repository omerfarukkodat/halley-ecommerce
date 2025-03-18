"use client";
import React, { useEffect, useState } from "react";
import {
    Typography,
    Paper,
    Box,
    Divider,
    Button,
    Grid,
    Container,
    useTheme, CircularProgress,
} from "@mui/material";
import Head from "next/head";
import blogService from "@/services/blogService";
import {useParams, useRouter} from "next/navigation";
import DOMPurify from "dompurify";

const BlogPostPage = () => {
    const { slug } = useParams();
    const [blogPost, setBlogPost] = useState(null);
    const [lastAddedBlogPosts, setLastAddedBlogPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const theme = useTheme();
    const  router = useRouter();


    const handleBlogPostClick = (event, slug) => {
        event.preventDefault();
        router.push(`/blog/${slug}`);
    }

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0'); // Gün
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear(); // Yıl
        return `${day}.${month}.${year}`;
    };

    useEffect(() => {
        const fetchBlogPost = async () => {
            try {
                const response = await blogService.getBlogPostBySlug(slug);
                setBlogPost(response);

                const lastAddedResponse = await blogService.getAll({ page: 0, size: 4, sortBy: "createdDate", sortDirection: "desc" });
                setLastAddedBlogPosts(lastAddedResponse.content);
            } catch (err) {
                setError(err.message || "Bir hata oluştu.");
            } finally {
                setLoading(false);
            }
        };

        fetchBlogPost();
    }, [slug]);

    if (loading) {
        return <CircularProgress size={16} sx={{ color: '#ffffff' }} />
    }

    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

    if (!blogPost) {
        return <Typography>Blog yazısı bulunamadı.</Typography>;
    }

    const cleanContent = DOMPurify.sanitize(blogPost.content);

    return (
        <>
            <Head>
                <title>{blogPost.title} | HalleyWall</title>
                <meta name="description" content={blogPost.content.substring(0, 150)} />
                <meta name="keywords" content="duvar kağıdı, blog, dekorasyon" />
                <meta name="author" content="HalleyWall" />
                <meta property="og:title" content={blogPost.title} />
                <meta property="og:description" content={blogPost.content.substring(0, 150)} />
                <meta property="og:image" content={blogPost.imageUrl} />
                <meta property="og:url" content={`https://www.halleywall.com/blog/${blogPost.slug}`} />
                <meta name="twitter:card" content="summary_large_image" />
                <meta name="twitter:title" content={blogPost.title} />
                <meta name="twitter:description" content={blogPost.content.substring(0, 150)} />
                <meta name="twitter:image" content={blogPost.imageUrl} />
            </Head>
            <Container maxWidth="xl" sx={{ py: 4 }}>
                <Grid container spacing={4}>
                    {/* Sol Taraf: Mevcut Blog Yazısı */}
                    <Grid item xs={12} md={8}>
                        <Paper elevation={0}>
                            <Box>
                                {/* Başlık */}
                                <Typography
                                    variant="h4"
                                    gutterBottom
                                    sx={{
                                        fontWeight: 400,
                                        color: "#000000",
                                        textAlign: "center",
                                        mb: 4,
                                    }}
                                >
                                    {blogPost.title}
                                </Typography>
                                <Divider sx={{ my: 4, borderColor: "gray" }} />
                                <Typography
                                    variant="body2"
                                    gutterBottom
                                    sx={{
                                        fontWeight: 500,
                                        fontSize: "16px",
                                        color: "#000000",
                                        textAlign: "left",
                                        mt: 5,
                                        mb: 4,
                                    }}
                                >
                                    {formatDate(blogPost.createdDate)}
                                </Typography>

                                {/* Blog Resmi */}
                                {blogPost.imageUrl && (
                                    <Box
                                        sx={{
                                            width: "100%",
                                            maxWidth: { xs: "100%", sm: "80%", md: "60%", lg: "70%", xl: "80%" },
                                            height: "auto",
                                            margin: "0 auto",
                                            display: "block",
                                            borderRadius: "4px",
                                            overflow: "hidden",
                                        }}
                                    >
                                        <img
                                            src={blogPost.imageUrl}
                                            alt={blogPost.title}
                                            style={{
                                                width: "100%",
                                                height: "auto",
                                                objectFit: "cover",
                                            }}
                                        />
                                    </Box>
                                )}
                            </Box>
                            <Typography
                                paragraph
                                sx={{
                                    mt: 10,
                                    lineHeight: 1.8,
                                    mb: 4,
                                    fontWeight: 500,
                                    color: "#000000",
                                }}
                                dangerouslySetInnerHTML={{ __html: cleanContent }}
                            />
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Typography variant="h6" sx={{ fontWeight: 600, mb: 4, textAlign: "center" }}>
                            Son Eklenen Yazılar
                        </Typography>
                        <Divider sx={{ my: 4, borderColor: "gray" }} />
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 4 }}>
                            {lastAddedBlogPosts.map((post) => (
                                <Paper key={post.id} elevation={3}>
                                    {post.imageUrl && (
                                        <img
                                            src={post.imageUrl}
                                            alt={post.title}
                                            style={{
                                                width: "100%",
                                                height: "250px",
                                                objectFit: "fill",
                                                borderRadius: "4px",
                                                marginBottom: "16px",
                                            }}
                                        />
                                    )}
                                    <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, fontSize: "1.1rem" }}>
                                        {post.title}
                                    </Typography>
                                    <Divider sx={{ mb: 2 }} />
                                    <Typography
                                        variant="body2"
                                        sx={{
                                            fontWeight: 500,
                                            display: "-webkit-box",
                                            WebkitBoxOrient: "vertical",
                                            WebkitLineClamp: 3,
                                            overflow: "hidden",
                                            textOverflow: "ellipsis",
                                        }}
                                    >
                                        {post.content.split("\n").slice(0, 3).join("\n")}
                                    </Typography>
                                    <Button
                                        onClick={(event) => handleBlogPostClick(event, post.slug)}                                        variant="contained"
                                        href={`/blog/${post.slug}`}
                                        sx={{
                                            backgroundColor: "#586277",
                                            alignSelf: "flex-start",
                                            width: "100%",
                                            mt: 2,
                                            "&:hover": {
                                                backgroundColor: "#0F3476",
                                            },
                                        }}
                                    >
                                        Devamını Oku
                                    </Button>
                                </Paper>
                            ))}
                        </Box>
                    </Grid>
                </Grid>
            </Container>
        </>
    );
};

export default BlogPostPage;