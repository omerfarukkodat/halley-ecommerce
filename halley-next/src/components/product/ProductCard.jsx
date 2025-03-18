import React, { useState } from 'react';
import { Box, Typography, Button, Chip, Snackbar, CircularProgress } from '@mui/material';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import {addCart, getCart} from "@/services/cartService";
import { motion } from 'framer-motion';
import {useCart} from "@/context/CartContext";


const ProductCard = ({ product }) => {
    const router = useRouter();
    const [snackBarOpen, setSnackBarOpen] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const { updateCartCount } = useCart();
    const isOutOfStock = product.stock === 0;





    const productId = product.id;
    const slug = product.slug;
    const productUrl = `/urun/${slug}-${productId}`;


    const handleClick = (event) => {
        event.preventDefault();
        router.push(`${productUrl}`);
    };

    const isDiscounted = product.originalPrice !== product.discountedPrice;

    const handleAddToCart = async () => {
        if (isOutOfStock) return;

        try {
            setLoading(true);
            await addCart(product.id);
            setLoading(false);
            setSuccess(true);
            setSnackBarMessage("Ürün sepetine eklendi!");
            setSnackBarOpen(true);
            const cart = await getCart();
            updateCartCount(cart.items.length);

            setTimeout(() => {
                setSuccess(false);
                setSnackBarOpen(false);
            }, 1000);        } catch (error) {
            setLoading(false);
            setSnackBarMessage("Ürün eklenirken bir hata oluştu.");
            setSnackBarOpen(true);
        }
    };

    return (
        <Box

            sx={{
                borderRadius: 1,
                border: 'solid 1px #e0e0e0',
                maxWidth: { xs: '100%', sm: '350px', md: '400px' },
                boxShadow: '0 4px 10px rgba(0, 0, 0, 0.1)',
                display: 'flex',
                flexDirection: 'column',
                position: 'relative',
                minHeight: { xs: '350px', sm: '350px', md: '350px', lg: '350px' , xl:'380px' },
                transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
            }}

        >
            {isOutOfStock && (
                <Box
                    sx={{
                        position: 'absolute',
                        top: '30%',
                        left: '50%',
                        transform: 'translate(-50%, -50%)',
                        backgroundColor: "#0F3460",
                        color: 'white',
                        borderRadius: '4px',
                        zIndex: 1,
                        textAlign: 'center',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        width: '100%',
                        height: 44
                    }}
                >
                    <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
                        Stokta Yok
                    </Typography>
                </Box>
            )}

            <Box
                component="a"
                href={productUrl}

                sx={{
                    margin: '0px',
                    gap:0,
                    borderRadius: 1,
                    width: '100%',
                    minHeight: { xs: '230px', sm: '320px', md: '320px', lg: '320px' },
                    position: 'relative',
                    overflow: 'hidden',
                    cursor: 'pointer',
                }}
                onClick={(event) => handleClick(event)}

            >
                <Image
                    src={product.imageUrls[0]}
                    alt={product.name}
                    layout="fill"
                    objectFit="cover"
                />
            </Box>


            {isDiscounted && (
                <motion.div
                    initial={{ scale: 1, opacity: 1 }}
                    animate={{ scale: [1, 1.1, 1], opacity: [1, 0.8, 1] }}
                    transition={{ duration: 1.5, repeat: Infinity }}
                    style={{
                        position: 'absolute',
                        top: 6,
                        left: 6,
                    }}
                >
                    <Chip
                        label={`${((1 - product.discountedPrice / product.originalPrice) * 100)
                            .toFixed(0)}% İndirim`}
                        size="small"
                        sx={{
                            px: 2,
                            color: "white",
                            backgroundColor: "#0F3460",
                        }}
                    />
                </motion.div>
            )}




            <Box sx={{ mt: 2, display: 'flex', flexDirection: 'column', flex: 1 }}>
                <Typography
                    onClick={(event) => handleClick(event)}
                    component="a"
                    href={productUrl}
                    variant="body1"
                    sx={{
                        color: 'black',
                        maxWidth: '100%',
                        display: '-webkit-box',
                        WebkitBoxOrient: 'vertical',
                        overflow: 'hidden',
                        WebkitLineClamp: 2,
                        lineHeight: 1.5,
                        height: '3rem',
                        cursor: 'pointer',
                        textDecoration: 'none',
                        transition: 'color 0.3s ease',
                        "&:hover": {
                            color: '#ff7f00',
                        },
                    }}
                >
                    {product.name}
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, justifyItems:"center",  }}>

                    {isDiscounted && (
                        <Typography
                            sx={{
                                textDecoration: 'line-through',
                                color: "#757575",
                                fontSize: "16px",
                                fontWeight: 500,
                        }}
                        >
                            {product.originalPrice.toLocaleString('tr-TR', {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2,
                            })} TL
                        </Typography>
                    )}
                    <Typography

                        sx={{
                            color: "#0F3460",
                            fontSize: "16px",
                            fontWeight: 600,
                        }}
                    >
                        {product.discountedPrice.toLocaleString('tr-TR', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                        })} TL
                    </Typography>
                </Box>
            </Box>

            <Button
                onClick={handleAddToCart}
                disabled={loading || success}
                sx={{
                    cursor: 'pointer',
                    position: 'absolute',
                    bottom: '5px',
                    right: '3px',
                    color: '#ffffff',
                    backgroundColor: success ? '#0F3460' : '#0F3460',
                    borderRadius: '2px',
                    padding: '0px 4px',
                    minWidth: '28px',
                    height: '28px',
                    fontSize: '0.75rem',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}
            >
                {loading ? (
                    <CircularProgress size={16} sx={{ color: '#ffffff' }} />
                ) : success ? (
                    <Typography sx={{ color: 'white', textDecoration: 'none' }}>
                        ✓
                    </Typography>

                ) : (
                    <Typography variant="h6" sx={{ fontWeight: 'semibold', color: 'white' }}>
                        +
                    </Typography>
                )}
            </Button>

            <Snackbar
                open={snackBarOpen}
                autoHideDuration={1000}
                onClose={() => setSnackBarOpen(false)}
                message={snackBarMessage}
                anchorOrigin={{vertical: 'top', horizontal: 'center'}}
                sx={{
                    '& .MuiSnackbarContent-root': {
                        backgroundColor: "#0F3460",
                        color: "#ffffff",
                        borderRadius: 2,
                    },
                }}
            />
        </Box>
    );
};

export default ProductCard;
