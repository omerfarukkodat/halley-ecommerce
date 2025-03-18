"use client";
import React, {useState, useMemo, useEffect} from "react";
import {
    Box,
    List,
    Divider,
    Button,
    CircularProgress,
    Card,
    CardContent,
    Backdrop, Typography
} from "@mui/material";
import CartItem from "./CartItem";
import {
    getCart,
    clearCart,
    removeCartItem,
    decreaseProductQuantity,
    increaseProductQuantity,
} from "@/services/cartService";
import {useCart} from "@/context/CartContext";

const MemoizedCartItem = React.memo(CartItem);

const CartComponent = ({ cart, setCart, fetchCart}) => {
    const [actionLoading, setActionLoading] = useState(null);
    const [showOverlay, setShowOverlay] = useState(false);
    const {updateCartCount} = useCart();




    const handleClearCart = async () => {
        setShowOverlay(true);
        setActionLoading("clearCart");
        try {
            await clearCart();
            await fetchCart();
            const cart = await getCart();
            updateCartCount(cart.items.length);
        } catch (error) {
            console.error("Error clearing cart:", error);
        } finally {
            setActionLoading(null);
            setShowOverlay(false);
        }
    };

    const handleRemoveItem = async (productId) => {
        setShowOverlay(true);
        setActionLoading(productId);
        setCart(prevCart => ({
            ...prevCart,
            items: prevCart.items.filter(item => item.id !== productId)
        }));
        try {
            await removeCartItem(productId);
            await fetchCart();
            const cart = await getCart();
            updateCartCount(cart.items.length);
        } catch (error) {
            console.error("Error removing item:", error);
        } finally {

            setActionLoading(null);
            setShowOverlay(false);
        }
    };

    const handleDecreaseQuantity = async (productId) => {
        setShowOverlay(true);
        setActionLoading(productId);
        setCart(prevCart => ({
            ...prevCart,
            items: prevCart.items.map(item =>
                item.id === productId ? {...item, quantity: item.quantity - 1} : item
            )
        }));
        try {
            await decreaseProductQuantity(productId);
            setTimeout(() => {
            }, 1000);
            const cart = await getCart();
            updateCartCount(cart.items.length);
            await fetchCart();
        } catch (error) {
            console.error("Error decreasing quantity:", error);
        } finally {

            setActionLoading(null);
            setShowOverlay(false);
        }
    };

    const handleIncreaseQuantity = async (productId) => {
        setShowOverlay(true);
        setActionLoading(productId);
        setCart(prevCart => ({
            ...prevCart,
            items: prevCart.items.map(item =>
                item.id === productId ? {...item, quantity: item.quantity + 1} : item
            )
        }));
        try {
            await increaseProductQuantity(productId);
            setTimeout(() => {
            }, 2000);
            await fetchCart();
            const cart = await getCart();
            updateCartCount(cart.items.length);
        } catch (error) {
            console.error("Error increasing quantity:", error);
        } finally {
            setActionLoading(null);
            setShowOverlay(false);
        }
    };

    const cartItems = useMemo(() => {
        return cart?.items ? [...cart.items].sort((a, b) => a.id - b.id) : [];
    }, [cart]);





    return (
        <Box style={{
            maxWidth: "800px",
            width: "100%"
        }}>

            <Typography variant="h4" sx={{color:"#0F3476",mt:2, textAlign:"center", fontWeight: 700, mb: 4 }}>
                Sepetim
            </Typography>
            <Divider sx={{ mb: 4 }} />

            <Backdrop open={showOverlay} sx={{color: "#fff", zIndex: 1700, transitionDuration: '1500ms'}}>
                <CircularProgress color="inherit"/>
            </Backdrop>
            <Box  display="flex" justifyContent="flex-end" >
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleClearCart}
                    disabled={!cartItems.length || actionLoading === "clearCart"}
                    sx={{
                        backgroundColor: "#0F3460",
                        textTransform: "none",
                    }}
                >
                    Sepeti Boşalt
                </Button>
            </Box>

            <Card sx={{maxWidth: 800, width: "100%", padding: 0}}>
                <CardContent sx={{padding: 0}}>

                    {cartItems.length > 0 ? (
                        <List>
                            {cartItems.map((item) => (
                                <React.Fragment key={item.id}>
                                    <MemoizedCartItem
                                        item={item}
                                        onIncrease={handleIncreaseQuantity}
                                        onDecrease={handleDecreaseQuantity}
                                        onRemove={handleRemoveItem}
                                        isLoading={actionLoading === item.id}
                                    />
                                    <Divider/>
                                </React.Fragment>
                            ))}
                        </List>
                    ) : null}
                </CardContent>
            </Card>
        </Box>

    );
};

export default CartComponent;
