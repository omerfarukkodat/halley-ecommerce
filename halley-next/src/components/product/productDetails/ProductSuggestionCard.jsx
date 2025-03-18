import React, { useState, useEffect } from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Grid,
    IconButton, Snackbar,
    Typography,
} from "@mui/material";
import { Add, Delete, Remove } from "@mui/icons-material";
import { addCart, decreaseProductQuantity, getCart, removeCartItem } from "@/services/cartService";
import { useCart } from "@/context/CartContext";
import {useRouter} from "next/navigation";
import Divider from "@mui/material/Divider";

const RelatedProductComponent = ({ product }) => {
    const [loading, setLoading] = useState(false);
    const { cart, updateCartCount, updateCart } = useCart();
    const [snackBarOpen, setSnackBarOpen] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState("");
    const [success, setSuccess] = useState(false);
    const isOutOfStock = product.stock === 0;
    const router = useRouter();

    const handleProductClick = () => {
        router.push(`/urun/${product.slug}-${product.id}`)
    }


    const cartItems = cart?.items || [];
    const cartItem = cartItems.find(item => item.product.id === product.id);
    const productQuantity = cartItem ? cartItem.quantity : 0;


    useEffect(() => {

    }, [cart]);

    const handleAddToCart = async () => {
        if (isOutOfStock) return;
        try {
            setLoading(true);
            await addCart(product.id);
            const cart = await getCart();
            updateCart(cart);
            updateCartCount(cart.items.length);
            setLoading(false);
            setSuccess(true);
            setSnackBarMessage("Ürün sepetine eklendi !");
            setSnackBarOpen(true);

            setTimeout(() => {
                setSuccess(false);
                setSnackBarOpen(false);
            }, 700);
        } catch (error) {
            setLoading(false);
            setSnackBarMessage("Ürün eklenirken bir hata oluştu.");
            setSnackBarOpen(true);
        }
    };


    const handleDecreaseFromCart = async () => {
        try {
            setLoading(true);
            await decreaseProductQuantity(product.id);
            const cart = await getCart();
            updateCart(cart);
            updateCartCount(cart.items.length);
            setLoading(false);
            setSuccess(true);
            setSnackBarMessage("Ürün miktarı azaltıldı.")
            setSnackBarOpen(true);


            setTimeout(() => {
                setSuccess(false);
                setSnackBarOpen(false);
            }, 700);
        } catch (error) {
            setLoading(false);
            setSnackBarMessage("Ürün eklenirken bir hata oluştu.");
            setSnackBarOpen(true);
        }
    };

    const handleDeleteFromCart = async () => {
        try {
            setLoading(true);
            await removeCartItem(product.id);
            const cart = await getCart();
            updateCart(cart);
            updateCartCount(cart.items.length);
            setLoading(false);
            setSuccess(true);
            setSnackBarMessage("Ürün sepetinden silindi.")
            setSnackBarOpen(true);


            setTimeout(() => {
                setSuccess(false);
                setSnackBarOpen(false);
            }, 700);
        } catch (error) {
            setLoading(false);
            setSnackBarMessage("Ürün eklenirken bir hata oluştu.");
            setSnackBarOpen(true);
        }
    };

    if (!product || !product.imageUrls || !Array.isArray(product.imageUrls)) {
        return null;
    }

    return (
        <Box>

        <Card sx={{ borderRadius: 2, boxShadow: 3, mt: 4 }}>
            <CardContent sx={{
                border: "1px solid gray",
                borderRadius: 2,
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",

            }}>
                <Typography variant="h5"  sx={{textAlign:"center", fontWeight: 700, mb: 2 }}>
                    Ayrıca İhtiyacınız Olabilir
                </Typography>
                <Divider sx={{mt: 1 , mb:3 , borderColor: "gray" }} />
                <Grid container spacing={2}>
                    <Grid item  xs={12} sm={8} sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <Box sx={{ display: 'flex', gap: 1 }}>
                            {product.imageUrls.map((image, index) => (
                                <CardMedia
                                    onClick={() => handleProductClick()}
                                    key={index}
                                    component="img"
                                    image={image}
                                    alt={product.name}
                                    sx={{
                                        cursor: "pointer",
                                        width: 100,
                                        height: 100,
                                        borderRadius: 1,

                                    }}
                                />
                            ))}

                        </Box>
                        <Typography
                            onClick={() => handleProductClick()}
                            variant="h6" sx={{ fontWeight: 600, cursor: "pointer" }}>
                            {product.name}
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={12} >

                        <Typography variant="body1" color="text.secondary" sx={{display: 'flex', mb: 2 }}>
                            {product.description}
                        </Typography>
                    {product.discountedPrice && (
                        <Typography
                            sx={{
                                color: "#202020",
                                fontSize: "22px",
                                fontWeight: "bold",
                                mb: 2,
                                borderBottom: "1px solid gray",
                            }}
                        >
                            {product.discountedPrice.toLocaleString('tr-TR', {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2,
                            })} TL
                        </Typography>
                    )}
                        <Box>
                            <Typography sx={{
                                fontWeight: "bold",
                                color: "#292A2C",
                                fontSize: "14px",
                                mt: 2,
                                mb: 1,
                            }}>
                                Adet
                            </Typography>
                        </Box>
                        {productQuantity > 0 && (<Box sx={{
                            display: "flex",
                            width: "100%",
                            maxWidth: "180px",
                            alignItems: "center",
                            justifyContent: "space-between",
                            border: "1px solid #e0e0e0",
                            borderRadius: 1,
                            p: 1,
                            backgroundColor: "#f5f5f5",

                        }}>


                            <Box>
                                <IconButton onClick={handleDeleteFromCart} disabled={productQuantity === 0}>
                                    <Delete/>
                                </IconButton>
                            </Box>
                            <Box>
                                <IconButton onClick={handleDecreaseFromCart}
                                            disabled={productQuantity <= 1}>
                                    <Remove sx={{ color: productQuantity === 1 ? "#ddd" : "#000000" }} />
                                </IconButton>
                            </Box>
                            <Box>
                                <Typography sx={{fontWeight: "bold"}}>{productQuantity}</Typography>
                            </Box>
                            <Box>
                                <IconButton onClick={handleAddToCart}
                                            disabled={productQuantity >= product.stock}>
                                    <Add sx={{color: "black"}}/>
                                </IconButton>
                            </Box>
                        </Box>)}
                        {productQuantity === 0 && (<Button
                            onClick={handleAddToCart}
                            variant="contained"
                            color="primary"
                            size="large"
                            sx={{
                                backgroundColor: "#0f3460",
                                justifyContent: "center",
                                textTransform: "none",
                            }}
                        >
                            Sepete Ekle
                        </Button>)}
                    </Grid>
                </Grid>
            </CardContent>

        </Card>
    <Snackbar
        open={snackBarOpen}
        autoHideDuration={1500}
        onClose={() => setSnackBarOpen(false)}
        message={snackBarMessage}
        anchorOrigin={{vertical: 'bottom', horizontal: 'right'}}
        sx={{
            '& .MuiSnackbarContent-root': {
                backgroundColor: "#0F3460",
                color: "#ffffff",
                borderRadius: 2,
                fontFamily:"Quicksand"
            },
            position: 'fixed',
            bottom: 16,
            right: 16,
        }}
    />
        </Box>

    );
};

export default RelatedProductComponent;