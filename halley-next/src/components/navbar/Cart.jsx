"use client";

import {Badge, Box, createTheme, IconButton, Skeleton, ThemeProvider, Typography, useMediaQuery} from "@mui/material";
import {useRouter} from "next/navigation";
import {ShoppingCart} from "@mui/icons-material";
import {useEffect, useState} from "react";
import {useCart} from "@/context/CartContext";

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

const Cart = () => {
    const router = useRouter();
    const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
    const {cartItemCount,cart} = useCart();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCart = async () => {
            try {
                await cart;
                setLoading(false); // Set loading to false after fetching
            } catch (error) {
                console.log("Error fetching cart data size:", error);
                setLoading(false); // Set loading to false on error
            }
        };
        fetchCart();
    }, []);

    return (
        <ThemeProvider theme={createTheme()}>
            {!isMobile && (
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        marginRight: "4px",
                        marginLeft: "8px",

                        cursor: "pointer",
                    }}
                    onClick={() => router.push("/sepetim")}
                >
                    {loading ? null : (
                        <>
                            <IconButton onClick={() => router.push("/sepetim")}>
                                <Badge badgeContent={cartItemCount} color="error">
                                    <ShoppingCart sx={{color: "#ffffff"}}/>
                                </Badge>
                            </IconButton>
                            <Typography
                                sx={{
                                    marginRight: "4px",
                                    color: "#ffffff",
                                    fontFamily:"Quicksand",
                                    "&:hover": {
                                        textDecoration: "underline"
                                        , color: "#ff7f00"
                                    },
                                }}
                            >
                                Sepetim
                            </Typography>
                        </>
                    )}
                </Box>
            )}

            {isMobile && (
                <Box sx={{display: "flex", alignItems: "center"}}>
                    {loading ? (
                        <Skeleton variant="circular" width={30} height={30}/>
                    ) : (
                        <IconButton onClick={() => router.push("/sepetim")}>
                            <Badge badgeContent={cartItemCount} color="error">
                                <ShoppingCart sx={{color: "#ffffff"}}/>
                            </Badge>
                        </IconButton>
                    )}
                </Box>
            )}
        </ThemeProvider>
    );
};

export default Cart;
