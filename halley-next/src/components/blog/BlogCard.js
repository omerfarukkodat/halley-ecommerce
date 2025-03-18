// components/BlogCard.js
import React from "react";
import { Typography, Paper, Box, Divider, Button } from "@mui/material";

const BlogCard = ({ post }) => {
    const contentPreview = post.content.split("\n").slice(0, 3).join("\n");

    return (
        <Paper elevation={3} sx={{ height: "100%", display: "flex", flexDirection: "column", p: 2 }}>
            {post.imageUrl && (
                <img
                    src={post.imageUrl}
                    alt={post.title}
                    style={{
                        width: "100%",
                        height: "200px",
                        objectFit: "cover",
                        borderRadius: "4px",
                        marginBottom: "16px",
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
                    mb: 2,
                    display: "-webkit-box",
                    WebkitBoxOrient: "vertical",
                    WebkitLineClamp: 4,
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                }}
            >
                {contentPreview}
            </Typography>
            <Button
                variant="contained"
                href={`/blog/${post.slug}`}
                sx={{ backgroundColor: "#5899dd", alignSelf: "flex-start", width: "100%" }}
            >
                Devamını Oku
            </Button>
        </Paper>
    );
};

export default BlogCard;