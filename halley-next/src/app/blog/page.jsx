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
    Pagination,
    PaginationItem,
    CircularProgress
} from "@mui/material";
import { blogService } from "@/services/blogService";
import { useRouter, useSearchParams } from "next/navigation";

const BlogPage = () => {
    const [blogPosts, setBlogPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [totalPages, setTotalPages] = useState(0);
    const searchParams = useSearchParams();
    const page = parseInt(searchParams.get("page") || 0);
    const router = useRouter();

    const handlePageChange = (event, value) => {
        window.location.href = `/blog?page=${value - 1}`;
    };

    const handleBlogPostClick = (event, slug) => {
        event.preventDefault();
        router.push(`/blog/${slug}`);
    }

    useEffect(() => {
        const fetchBlogPosts = async () => {
            try {
                const response = await blogService.getAll({ page, size: 10, sortBy: "createdDate", sortDirection: "desc" });
                setBlogPosts(response.content);
                setTotalPages(response.totalPages);
            } catch (err) {
                setError(err.message || "Bir hata oluştu.");
            } finally {
                setLoading(false);
            }
        };

        fetchBlogPosts();
    }, [page]);

    if (loading) {
        return <CircularProgress size={16} sx={{ color: '#ffffff' }} />;
    }

    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

    return (
        <Container maxWidth="xl" sx={{ py: 4, minHeight: "800px", background: "linear-gradient(165deg, #ffffff, #fff0f5)" }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, textAlign: "center", mb: 8 }}>
                Halley Blog
            </Typography>
            <Divider sx={{ mb: 4, borderColor: "gray" }} />
            <Grid container spacing={4}>
                {blogPosts.map((post) => (
                    <Grid item xs={12} sm={6} md={4} lg={3} key={post.id}>
                        <BlogCard post={post} handleBlogPostClick={handleBlogPostClick} />
                    </Grid>
                ))}
            </Grid>
            <Box display="flex" justifyContent="center" mt={4}>
                <Pagination
                    count={totalPages}
                    page={page + 1}
                    onChange={handlePageChange}
                    shape="rounded"
                    size="large"
                    showFirstButton
                    showLastButton
                    boundaryCount={1}
                    siblingCount={1}
                    renderItem={(item) => {
                        const currentPage = page;
                        const totalPages = item.count;
                        let pageNumbers = [];

                        if (totalPages <= 3) {
                            pageNumbers = [1, 2, 3].slice(0, totalPages);
                        } else {
                            if (currentPage < 2) {
                                pageNumbers = [1, 2, 3];
                            } else if (currentPage > totalPages - 3) {
                                pageNumbers = [totalPages - 2, totalPages - 1, totalPages];
                            } else {
                                pageNumbers = [currentPage, currentPage + 1, currentPage + 2];
                            }
                        }

                        if (item.type === 'first' || item.type === 'last' || pageNumbers.includes(item.page)) {
                            return (
                                <PaginationItem
                                    {...item}
                                    sx={{
                                        color: "#7F8083",
                                        backgroundColor: "#ddd",
                                        borderRadius: 20,
                                        alignItems: "center",
                                        margin: "0 4px",
                                        mt: 11,
                                        fontSize: "1.2rem",
                                        "&.Mui-selected": {
                                            backgroundColor: "#0F3476",
                                            color: "white",
                                            "&:hover": {
                                                backgroundColor: "#0F3476",
                                            },
                                        },
                                        "&:hover": {
                                            backgroundColor: "#ccc",
                                        },
                                    }}
                                />
                            );
                        }
                        return null;
                    }}
                />
            </Box>
        </Container>
    );
};

const BlogCard = ({ post, handleBlogPostClick }) => {
    const contentPreview = post.content.split("\n").slice(0, 3).join("\n");

    return (
        <Paper elevation={3} sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
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
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
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
                }}>
                {contentPreview}
            </Typography>
            <Button
                onClick={(event) => handleBlogPostClick(event, post.slug)}
                variant="contained"
                href={`/blog/${post.slug}`}
                sx={{ backgroundColor: "#586277", alignSelf: "flex-start", width: "100%"
            , "&:hover": {
                        backgroundColor: "#0F3476",
                    },
                }}
            >
                Devamını Oku
            </Button>
        </Paper>
    );
};

export default BlogPage;