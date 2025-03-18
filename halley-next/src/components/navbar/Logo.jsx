"use client";

import React, { useState, useEffect } from "react";
import { Box, createTheme, Typography, Skeleton, useMediaQuery } from "@mui/material";
import { useRouter } from "next/navigation";

const theme = createTheme({
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

const Logo = () => {
    const [loading, setLoading] = useState(true); // Loading state
    const router = useRouter();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    useEffect(() => {
        // Simulate loading for 2 seconds
        setTimeout(() => setLoading(false)); // Adjust the time as necessary
    }, []);

    return (
        <Box
            sx={{
                alignItems: "center",
                display: "flex",
                textAlign: "center",
                justifyContent: isMobile ? "center" : "flex-start",
                marginLeft: isMobile ? 1 : 0,
            }}
        >
            {loading ? (
                <Skeleton variant="rectangular" width={isMobile ? 120 : 160} height={56.4} />
            ) : (
                <Typography
                    variant="h6"
                    onClick={() => router.push("/")}
                    sx={{ cursor: "pointer", flexGrow: 1, display: "flex", justifyContent: "center" }}
                >
                    <img
                        src="https://res.cloudinary.com/dxhftwif6/image/upload/v1737228180/Untitled_10_abdw8v.png"
                        alt="Logo"
                        style={{ height: isMobile ? 56.4 : 56.4 }}
                    />
                </Typography>
            )}
        </Box>
    );
};

export default Logo;
