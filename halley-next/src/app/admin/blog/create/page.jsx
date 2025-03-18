"use client";
import React, { useState, useEffect } from "react";
import {
    Typography,
    Paper,
    TextField,
    Button,
    Box,
    Divider,
} from "@mui/material";
import { useRouter } from "next/navigation";
import blogService from "@/services/blogService";
import { useAuth } from "@/context/AuthContext";

const CreateBlogPage = () => {
    const router = useRouter();
    const { isAdminUser, loading, checkAdminStatus } = useAuth();
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [imageUrl, setImageUrl] = useState("");

    useEffect(() => {
        checkAdminStatus();
    }, []);

    useEffect(() => {
        if (!loading && !isAdminUser) {
            router.push("/");
        }
    }, [loading, isAdminUser, router]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const blogPostDto = {
            title,
            content,
            imageUrl,
        };

        try {
            const response = await blogService.addBlogPost(blogPostDto);
            if (response) {
                router.push("/admin/dashboard");
            }
        } catch (error) {
            console.error("Blog post creation failed:", error);
        }
    };

    if (loading || !isAdminUser) {
        return null;
    }

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h4" gutterBottom>
                Create New Blog Post
            </Typography>
            <Paper elevation={3} sx={{ p: 4 }}>
                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        margin="normal"
                        multiline
                        rows={4}
                        required
                    />
                    <TextField
                        fullWidth
                        label="Image URL"
                        value={imageUrl}
                        onChange={(e) => setImageUrl(e.target.value)}
                        margin="normal"
                    />
                    <Divider sx={{ my: 3 }} />
                    <Button type="submit" variant="contained" color="primary">
                        Create Blog Post
                    </Button>
                </form>
            </Paper>
        </Box>
    );
};

export default CreateBlogPage;