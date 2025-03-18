import {
    addCart, decreaseProductQuantity, getCart, removeCartItem
} from "@/services/cartService";
import React, {useEffect, useState} from "react";
import {useCart} from "@/context/CartContext";
import Container from "@mui/material/Container";
import {
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Dialog, DialogActions, DialogContent,
    Grid,
    IconButton,
    Snackbar, useMediaQuery, useTheme
} from "@mui/material";
import Typography from "@mui/material/Typography";
import {Add, Delete, Remove} from "@mui/icons-material";
import ProductDetailsTab from "@/components/product/productDetails/ProductDetailsTab";
import SimilarProducts from "@/components/product/productDetails/SimilarProducts";
import {CloseIcon} from "next/dist/client/components/react-dev-overlay/internal/icons/CloseIcon";
import ProductBreadcrumb from "@/components/product/productDetails/ProductBreadcrumb";
import {getProductById} from "@/services/productService";
import RelatedProductComponent from "@/components/product/productDetails/ProductSuggestionCard";
import dayjs from 'dayjs';
import 'dayjs/locale/tr';
import LocalShippingOutlinedIcon from '@mui/icons-material/LocalShippingOutlined';
import Divider from "@mui/material/Divider";



export const ProductDetails = ({product, productQuantity}) => {
    const [snackBarOpen, setSnackBarOpen] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const {cart, updateCartCount, updateCart} = useCart();
    const [selectedImage, setSelectedImage] = useState(product.imageUrls[0]);
    const [open, setOpen] = useState(false);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
    const isDiscounted = product.originalPrice !== product.discountedPrice;
    const isOutOfStock = product.stock === 0;
    const [relatedProduct, setRelatedProduct] = useState([]);

    dayjs.locale('tr');
    const today = dayjs();
    const deliveryStart = today.add(2, 'day').format("DD MMMM YYYY");
    const deliveryEnd = today.add(5, 'day').format("DD MMMM YYYY");


    useEffect(() => {
        const fetchRelatedProduct = async () => {
            try {
                const response = await getProductById("250g-tutkal-t-02", 9);
                setRelatedProduct(response.data);
            } catch (error) {
                console.error("İlgili ürün yüklenirken bir hata oluştu:", error);
            }
        };

        fetchRelatedProduct();
    }, []);

    const shouldShowRelatedProduct = relatedProduct && relatedProduct.id !== product.id;







    useEffect(() => {
    }, [cart]);


    const handleImageClick = (image) => {
        setSelectedImage(image);
    };

    const handleModalOpen = () => {
        setOpen(true);
    };

    const handleModalClose = () => {
        setOpen(false);
    };


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


    return (
        <Container maxWidth="xl" sx={{mb:5,mt:3 }}>
            <ProductBreadcrumb product={product} />
            <Grid container spacing={4}>
                <Grid item xs={12} md={6}>
                    <Card sx={{ borderRadius: 2, boxShadow: 3 }}>
                        <CardMedia
                            component="img"
                            image={selectedImage}
                            alt={product.name}
                            sx={{
                                width: '100%',
                                height: 'auto',
                                cursor: 'pointer',
                                borderBottom: '1px solid #e0e0e0',
                                mb: 2,
                            }}
                            onClick={handleModalOpen}
                        />
                        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 1, p: 1 }}>
                            {product.imageUrls.map((image, index) => (
                                <IconButton
                                    key={index}
                                    onClick={() => handleImageClick(image)}
                                    sx={{
                                        border: '1px solid #e0e0e0',
                                        borderRadius: 1,
                                        overflow: 'hidden',
                                        '&:hover': { opacity: 0.8 },
                                    }}
                                >
                                    <CardMedia
                                        component="img"
                                        image={image}
                                        alt={`product thumbnail ${index}`}
                                        sx={{
                                            width: '120px',
                                            height: '120px',
                                            objectFit: 'cover',
                                        }}
                                    />
                                </IconButton>
                            ))}
                        </Box>
                    </Card>

                    <Dialog
                        open={open}
                        onClose={handleModalClose}
                        fullScreen={isMobile}
                        maxWidth="md"
                        fullWidth
                        sx={{
                            '& .MuiDialog-paper': {
                                backgroundColor: 'black',
                                margin: 0,
                                padding: 0,
                                display: 'flex',
                                justifyContent: 'center',
                                alignItems: 'center',
                            },
                        }}
                    >
                        <DialogContent sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                            <CardMedia
                                component="img"
                                image={selectedImage}
                                alt="Enlarged product image"
                                sx={{
                                    width: '100%',
                                    height: 'auto',
                                    maxHeight: '90vh',
                                    objectFit: 'contain',
                                }}
                            />
                        </DialogContent>
                        <DialogActions sx={{ position: 'absolute', top: 0, right: 0 }}>
                            <IconButton onClick={handleModalClose} sx={{ color: 'white' }}>
                                <CloseIcon />
                            </IconButton>
                        </DialogActions>
                    </Dialog>
                </Grid>

                <Grid item xs={12} md={6}>
                    <CardContent sx={{
                        border: "1px solid gray",
                        borderRadius: 2,
                        background: "linear-gradient(165deg, #ffffff, #fff0f5)",

                    }}>
                        <Typography sx={{
                            fontSize: "28px",
                            fontWeight: 700,
                            color: "#292A2C",
                            textAlign:"center",
                            mt: 1 ,
                            mb:5,
                            borderBottom: "1px solid gray"

                        }} gutterBottom>
                            {product.name}
                        </Typography>






                        <Typography variant="body1" color="text.secondary" gutterBottom>
                            Stok Sayısı:
                            <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                {` ${product.stock} Adet`}
                            </Typography>
                        </Typography>

                        <Typography variant="body1" color="text.secondary" gutterBottom>
                            Ürün Kodu:
                            <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                {` ${product.productCode}`}
                            </Typography>
                        </Typography>

                        {product.wallpaperType && (
                            <Typography variant="body1" color="text.secondary" gutterBottom>
                                Kağıt Tipi:
                                <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                    {` ${product.wallpaperType}`}
                                </Typography>
                            </Typography>
                        )}

                        <Typography variant="body1" color="text.secondary" gutterBottom sx={{
                            fontWeight: "bold",
                            borderBottom: "1px solid gray",
                        }} >
                            Marka:
                            <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                {` ${product.brand}`}
                            </Typography>
                        </Typography>

                        {product.colourName && product.colourName.length > 0 && (
                            <Typography variant="body1" color="text.secondary" gutterBottom>
                                Renk:
                                <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                    {` ${product.colourName.join(", ")}`}
                                </Typography>
                            </Typography>
                        )}

                        {product.designName && product.designName.length > 0 && (
                            <Typography variant="body1" color="text.secondary" gutterBottom>
                                Desen:
                                <Typography component="span" sx={{ color: "#0f3460", fontWeight: "bold" }}>
                                    {` ${product.designName.join(", ")}`}
                                </Typography>
                            </Typography>
                        )}
                        <Box sx={{ display: 'row', alignItems: 'center', mb: 2 }}>
                            {isDiscounted && (
                                <Typography
                                    sx={{
                                        mr: 2,
                                        color: "#707070",
                                        fontSize: "18px",
                                        fontWeight: 500,
                                    }}
                                >
                                    {product.originalPrice.toLocaleString('tr-TR', {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2,
                                    })} TL
                                </Typography>
                            )}

                            {isDiscounted && (
                                <Typography
                                    sx={{
                                        fontSize: "14px",
                                        fontWeight: 700,
                                        color: "#0f3460",
                                        mt: 1,
                                        mb: 1,
                                    }}
                                >
                                    SEPETTE
                                </Typography>
                            )}

                            {product.discountedPrice && (
                                <Typography
                                    sx={{
                                        color: "#202020",
                                        fontSize: "22px",
                                        fontWeight: "bold",
                                        borderBottom: "1px solid gray",
                                        mt:2,
                                        mb:2
                                    }}
                                >
                                    {product.discountedPrice.toLocaleString('tr-TR', {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2,
                                    })} TL
                                </Typography>
                            )}
                        </Box>


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
                        {productQuantity > 0 && (
                            <Box sx={{
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
                                <IconButton onClick={handleDeleteFromCart} disabled={productQuantity === 0}>
                                    <Delete/>
                                </IconButton>
                                <IconButton onClick={handleDecreaseFromCart} disabled={productQuantity <= 1}>
                                    <Remove sx={{ color: productQuantity === 1 ? "#ddd" : "#000000" }} />
                                </IconButton>
                                <Typography sx={{ fontWeight: "bold" }}>{productQuantity}</Typography>
                                <IconButton onClick={handleAddToCart} disabled={productQuantity >= product.stock}>
                                    <Add sx={{ color: "black" }} />
                                </IconButton>
                            </Box>
                        )}
                        {productQuantity === 0 && (
                            <Button
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
                            </Button>
                        )}
                        <Divider orientation="vertical" sx={{borderBottom: "1px solid gray",mt:4}} />

                        <Typography
                            sx={{
                                mt: 2,
                                mb: 2,
                                display: "flex",
                                alignItems: "center"
                            }}
                            color="text.secondary"
                            variant="body1"
                            gutterBottom
                        >
                            <LocalShippingOutlinedIcon sx={{ color: "#0f3460", mr: 1 }} />
                            Tahmini teslim tarihi:
                            <Typography component="span" sx={{ fontWeight: "bold", ml: 1, color:"#0f3460" }}>
                                {deliveryStart} - {deliveryEnd}
                            </Typography>
                        </Typography>
                    </CardContent>

                    {shouldShowRelatedProduct && product.wallpaperType !== 'folyo'  && product.wallpaperType !== null &&
                        (
                        <Grid item xs={12} sx={{ mt: 1 }}>
                            <RelatedProductComponent product={relatedProduct} />
                        </Grid>
                    )}

                </Grid>
            </Grid>

            <Snackbar
                open={snackBarOpen}
                autoHideDuration={1500}
                onClose={() => setSnackBarOpen(false)}
                message={snackBarMessage}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                sx={{
                    '& .MuiSnackbarContent-root': {
                        backgroundColor: "#0F3460",
                        color: "#ffffff",
                        borderRadius: 2,
                    },
                }}
            />
            <ProductDetailsTab product={product} />
            <SimilarProducts productId={product.id} initialPage={0} size={10} />
        </Container>
    );
}


