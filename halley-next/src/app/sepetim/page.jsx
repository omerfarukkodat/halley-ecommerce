"use client";

import { Box, Typography, Button, Grid, CircularProgress, Paper } from "@mui/material";
import CartComponent from "@/components/cart/Cart";
import CartSummary from "@/components/cart/CartSummary";
import React, { useEffect, useState } from "react";
import CartSuggestionProducts from "@/components/cart/CartSuggestionProducts";
import { getCart, getCartSummary } from "@/services/cartService";
import { useCart } from "@/context/CartContext";
import { useRouter } from "next/navigation";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import dayjs from "dayjs";
import 'dayjs/locale/tr';

const CartPage = () => {
    const [cartChanged, setCartChanged] = useState(false);
    const [isTokenAvailable, setIsTokenAvailable] = useState(null);
    const [cartSummary, setCartSummary] = useState(null);
    const [cart, setCart] = useState({ items: [] });
    const [isLoading, setIsLoading] = useState(true);
    const router = useRouter();
    dayjs.locale('tr');
    const today = dayjs();
    const deliveryStart = today.add(2, 'day').format("DD MMMM YYYY");
    const deliveryEnd = today.add(5, 'day').format("DD MMMM YYYY");

    const { updateCart } = useCart();

    const fetchCart = async () => {
        try {
            const data = await getCart();
            setCart(data);
            updateCart(data);
        } catch (error) {
            console.error("Error fetching cart:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        const fetchCartSummary = async () => {
            try {
                const data = await getCartSummary();
                setCartSummary(data);
            } catch (err) {
                console.error("Error fetching cart summary:", err);
            }
        };
        fetchCartSummary();
    }, [cart]);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem("token");
            setIsTokenAvailable(!!token);
        };

        checkAuth();
        fetchCart();
    }, []);

    const handleCartChange = () => {
        setCartChanged(!cartChanged);
    };

    if (isLoading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress size={60} thickness={4} sx={{ color: "#0F3460" }} />
            </Box>
        );
    }

    if (!cart || cart.items.length === 0) {
        return (
            <Box textAlign="center" >
                <Box
                    component="img"
                    src="https://res.cloudinary.com/dxhftwif6/image/upload/v1740949869/empty-cart_gmshdq.svg"
                    alt="Empty Cart"
                    sx={{
                        maxWidth: "100%",
                        height: "auto",
                        maxHeight:"300px",
                        width: { xs: "190px", sm: "250px", md: "310px" , lg:"370px" , xl:"430px" },
                        mb: 4,
                    }}
                />
                <Typography variant="h4" fontWeight={700} color="text.primary" gutterBottom>
                    Sepetinde hiç ürün bulunmuyor!
                </Typography>
                <Typography variant="body1" color="text.secondary" mb={4}>
                    Alışverişe başlamak için aşağıdaki butonu kullanabilirsiniz.
                </Typography>
                <Button
                    onClick={() => router.push("/")}
                    variant="contained"
                    sx={{
                        backgroundColor: "#0F3460",
                        color: "white",
                        mb:5,
                        textTransform: "none",
                        padding: "12px 24px",
                        fontSize: "1rem",
                        '&:hover': {
                            backgroundColor: "#0A2640",
                        },
                    }}
                >
                    Alışverişe Başla
                </Button>
            </Box>
        );
    }

    return (
        <Box display="flex" flexDirection="column">




            <Grid container spacing={3}>
                <Grid item xs={12} md={8}>
                    <CartComponent onCartChange={handleCartChange} cart={cart} setCart={setCart} fetchCart={fetchCart} />
                </Grid>
                <Grid item xs={12} md={4}>
                    <Paper elevation={2} sx={{p:2, top:1,mb:2,  backgroundColor: "#ffffff" }}>
                        <Typography
                            sx={{
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                            }}
                            color="text.secondary"
                            variant="body2"
                            gutterBottom
                        >
                            <LocalShippingOutlinedIcon sx={{ color: "#0f3460" }} />
                            <Typography component="span" sx={{ml:1, fontWeight: 500, color: "#0f3460" }}>
                                Tahmini teslim tarihi:
                            </Typography>
                            <Typography component="span" sx={{ml:1, fontWeight: 500, color: "#0f3460" }}>
                                {deliveryStart} - {deliveryEnd}
                            </Typography>
                        </Typography>
                    </Paper>

                    <CartSummary cartSummary={cartSummary} />
                </Grid>
            </Grid>
            {isTokenAvailable === false && (
                <Paper elevation={1} sx={{mt:5,background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                    textAlign: "center", p: 3, mb: 4 }}>
                    <Typography variant="h5" fontWeight={700}>
                        Hesabınız var mı?
                    </Typography>
                    <Typography color="text.secondary" mt={1}>
                        Daha iyi bir deneyim için giriş yapın!
                    </Typography>
                    <Button
                        variant="contained"
                        sx={{ mt: 2, backgroundColor: "#0F3460" }}
                        href="/giris"
                    >
                        Giriş Yap
                    </Button>
                </Paper>
            )}

            <Box mt={6}  mb={6}>
                <CartSuggestionProducts />
            </Box>
        </Box>
    );
};

export default CartPage;