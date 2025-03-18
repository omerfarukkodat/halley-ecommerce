"use client";

import React, {useEffect, useState} from 'react';
import {
    Container,
    Typography,
    CircularProgress,
    Card,
    CardContent,
    Button,
    Chip,
    List,
    ListItem,
    Divider,
    Box,
    useTheme,
    styled
} from '@mui/material';
import {useRouter} from 'next/navigation';
import {getOrderById} from "@/services/orderService";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

const StyledCard = styled(Card)(({theme}) => ({
    borderRadius: '16px',
    boxShadow: '0 8px 32px rgba(0, 0, 0, 0.1)',
    backgroundColor: theme.palette.background.paper,
    '&:hover': {
        boxShadow: '0 12px 40px rgba(0, 0, 0, 0.2)',
    },
}));

const StatusChip = styled(Chip)(({theme, status}) => ({
    fontWeight: 700,
    fontSize: '0.875rem',
    padding: '6px 12px',
}));

const OrderDetailPage = ({params}) => {
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const theme = useTheme();
    const {orderId} = React.use(params);

    useEffect(() => {
        const fetchOrderDetails = async () => {
            try {
                const data = await getOrderById(orderId);
                setOrder(data);
            } catch (error) {
                console.error('Sipariş detayları alınırken hata oluştu:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [orderId]);

    if (loading) {
        return (
            <Container style={{display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                <CircularProgress size={60} thickness={4} sx={{color: '#0F3460'}}/>
            </Container>
        );
    }

    if (!order) {
        return (
            <Container>
                <Typography variant="h6" sx={{color: theme.palette.text.secondary,
                    textAlign: 'center', mt: 4}}>
                    Sipariş bulunamadı.
                </Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ mb: 6}}>
            <Button
                variant="contained"
                startIcon={<ArrowBackIcon/>}
                onClick={() => router.push('/uyelik/siparislerim')}
                sx={{
                    backgroundColor: '#0F3460',
                    color: '#fff',
                    borderRadius: '8px',
                    padding: '10px 20px',
                    mb: 4,
                    '&:hover': {
                        backgroundColor: '#0A2640',
                    },
                }}
            >
                Siparişlerime Dön
            </Button>

            <Typography variant="h4" sx={{
                fontWeight: 700,
                color: '#0F3460',
                textAlign: 'center',
                mb: 6,
                fontSize: {xs: '1.75rem', sm: '2.125rem'},
            }}>
                Sipariş Detayları
            </Typography>

            <StyledCard>
                <CardContent >
                    <Box sx={{mb: 4}}>
                        <Typography variant="h6" sx={{fontWeight: 700, color: theme.palette.text.primary}}>
                            Sipariş No: #{order.id}
                        </Typography>
                        <Typography variant="body1" sx={{color: theme.palette.text.secondary, mt: 1}}>
                            Sipariş Tarihi: {order.createdAt}
                        </Typography>
                        <Divider sx={{mt: 3, mb: 3}}/>
                    </Box>

                    <Box sx={{mb: 4}}>
                        <Typography variant="h6" sx={{fontWeight: 700, color: theme.palette.text.primary, mb: 2}}>
                            Teslimat Adresi
                        </Typography>
                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                            {order.address.generalAddress}
                        </Typography>
                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                            {order.address.neighborhood} {order.address.district}/{order.address.city}
                        </Typography>
                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                            {order.address.zipCode}
                        </Typography>
                        <Divider sx={{mt: 3, mb: 3}}/>
                    </Box>
                    <Typography variant="h6" sx={{fontWeight: 700, color: theme.palette.text.primary, mb: 4}}>
                        Sipariş Kalemleri
                    </Typography>
                    <List>
                        {order.orderItems.map((item) => (
                            <ListItem key={item.product.id} sx={{padding: 0, mb: 3}}>
                                <Box onClick={() => router
                                    .push(`/urun/${item.product.slug}-${item.product.id}`)}
                                     sx={{
                                         display: 'flex',
                                         alignItems: 'center',
                                         gap: 3,
                                         width: '100%',
                                         cursor: 'pointer',
                                         '&:hover': {
                                             backgroundColor: theme.palette.action.hover,
                                             borderRadius: '8px',
                                         },
                                     }}>
                                    <img
                                        src={item.product.imageUrls[0]}
                                        alt={item.product.name}
                                        style={{
                                            width: 100,
                                            height: 100,
                                            borderRadius: '8px',
                                            objectFit: 'cover',
                                        }}
                                    />
                                    <Box sx={{flexGrow: 1}}>
                                        <Typography
                                            variant="body1"
                                            sx={{
                                                fontWeight: 600,
                                                color: theme.palette.text.primary,
                                                '&:hover': {
                                                    color: '#ff7f00',
                                                },
                                            }}>
                                            {item.product.name}
                                        </Typography>
                                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                                            Adet: {item.quantity}
                                        </Typography>
                                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                                            Fiyat: {item.product.discountedPrice.toLocaleString("tr-TR", {
                                            minimumFractionDigits: 2,
                                            maximumFractionDigits: 2
                                        })} TL
                                        </Typography>
                                    </Box>
                                </Box>
                                <Divider sx={{mt: 3, mb: 3}}/>
                            </ListItem>
                        ))}
                    </List>
                    <Divider sx={{mt: 3, mb: 3}}/>
                    <Box sx={{mb: 4}}>
                        <Typography variant="h6" sx={{fontWeight: 700, color: theme.palette.text.primary, mb: 3}}>
                            Sipariş Özeti
                        </Typography>
                        <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 2}}>
                            <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                                {order.orderItems.length} Ürün
                            </Typography>
                            <Typography variant="body1" sx={{fontWeight: 600, color: theme.palette.text.primary}}>
                                {order.totalPrice.toLocaleString("tr-TR", {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                })} TL
                            </Typography>
                        </Box>
                        <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 2}}>
                            <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                                Teslimat Ücreti
                            </Typography>
                            <Typography variant="body1" sx={{fontWeight: 600, color: theme.palette.text.primary}}>
                                {order.shippingCost === 0
                                    ? "Ücretsiz"
                                    : `${order.shippingCost.toLocaleString("tr-TR", {
                                        minimumFractionDigits: 2,
                                        maximumFractionDigits: 2
                                    })} TL`}
                            </Typography>
                        </Box>
                        <Divider sx={{my: 3}}/>
                        <Box sx={{display: 'flex', justifyContent: 'space-between'}}>
                            <Typography variant="body1" sx={{fontWeight: 700, color: theme.palette.text.primary}}>
                                Toplam Tutar
                            </Typography>
                            <Typography variant="h6" sx={{fontWeight: 700, color: theme.palette.text.primary}}>
                                {order.finalPrice.toLocaleString("tr-TR", {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                })} TL
                            </Typography>
                        </Box>
                        <Divider sx={{mt: 3, mb: 3}}/>
                    </Box>

                    <Box sx={{mb: 4}}>
                        <Typography variant="body1" sx={{color: theme.palette.text.secondary}}>
                            Durum:
                        </Typography>
                        <StatusChip
                            label={order.status}
                            status={order.status}
                            sx={{mt: 1, backgroundColor: "#0F3460", color: "#ffffff"}}
                        />
                    </Box>

                    <Divider sx={{my: 4}}/>


                </CardContent>
            </StyledCard>
        </Container>
    );
};

export default OrderDetailPage;