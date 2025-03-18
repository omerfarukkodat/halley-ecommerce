"use client";

import React, { useEffect, useState } from 'react';
import {
    Container,
    Typography,
    CircularProgress,
    Grid,
    Card,
    CardContent,
    Button,
    Chip,
    Box,
    useTheme,
    styled, CardActions
} from '@mui/material';
import { getAllOrders } from "@/services/orderService";
import { useRouter } from 'next/navigation';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import Divider from "@mui/material/Divider";

const StyledCard = styled(Card)(({ theme }) => ({
    height: '100%',
    display: 'flex',
    flexDirection: 'column',
    backgroundColor: theme.palette.background.paper,
    borderRadius: '12px',
    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
    transition: 'transform 0.2s, box-shadow 0.2s',
    '&:hover': {
        transform: 'translateY(-4px)',
        boxShadow: '0 8px 24px rgba(0, 0, 0, 0.15)',
    },
}));

const StatusChip = styled(Chip)(({ theme, status }) => ({
    fontWeight: 600,
    backgroundColor:
        status === 'DELIVERED' ? theme.palette.success.light :
            status === 'PENDING' ? theme.palette.warning.light :
                status === 'CANCELLED' ? theme.palette.error.light : theme.palette.grey[300],
    color:
        status === 'DELIVERED' ? theme.palette.success.dark :
            status === 'PENDING' ? theme.palette.warning.dark :
                status === 'CANCELLED' ? theme.palette.error.dark : theme.palette.grey[800],
}));

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const theme = useTheme();

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const data = await getAllOrders();
                setOrders(data);
            } catch (error) {
                console.error('Siparişler alınırken hata oluştu:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    const handleViewDetails = (orderId) => {
        router.push(`/uyelik/siparislerim/${orderId}`);
    };

    if (loading) {
        return (
            <Container style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 700, textAlign:"center", color: "#0F3460" }}>
                Siparişlerim
            </Typography>
            {orders.length === 0 ? (
                <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                    Henüz siparişiniz bulunmamaktadır.
                </Typography>
            ) : (
                <Grid container spacing={3}>
                    {orders.map((order) => (
                        <Grid item xs={12} sm={6} md={4} key={order.id}>
                            <StyledCard>
                                <CardContent sx={{ flexGrow: 1, padding: theme.spacing(3) }}>
                                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                        <LocalShippingIcon sx={{ color: theme.palette.primary.main, mr: 1 }} />
                                        <Typography variant="h6"
                                                    sx={{ fontWeight: 600, color: theme.palette.text.primary }}>
                                            Sipariş Numarası: #{order.id}
                                        </Typography>
                                    </Box>
                                    <Divider sx={{ my: 2, backgroundColor: theme.palette.divider }} />

                                    <Box sx={{ mb: 2 }}>
                                        <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                                            Toplam Tutar:
                                        </Typography>
                                        <Typography variant="h6" sx={{ fontWeight: 700, color: theme.palette.text.primary
                                        }}>
                                            {order.finalPrice.toLocaleString("tr-TR", { minimumFractionDigits: 2,
                                                maximumFractionDigits: 2 })} TL
                                        </Typography>
                                    </Box>

                                    <Box sx={{ mb: 2  }}>
                                        <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                                            Durum:
                                        </Typography>
                                        <StatusChip
                                            label={order.status}
                                            status={order.status}
                                            sx={{ mt: 1 ,backgroundColor:"#0F3460" , color: "#ffffff"}}
                                        />
                                    </Box>

                                    <Box>
                                        <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                                            Sipariş Tarihi:
                                        </Typography>
                                        <Typography variant="body1" sx={{  color: theme.palette.text.primary }}>
                                            {order.createdAt}
                                        </Typography>
                                    </Box>
                                </CardContent>
                                <CardActions sx={{ padding: theme.spacing(3), justifyContent: 'flex-end' }}>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        endIcon={<ArrowForwardIcon />}
                                        onClick={() => handleViewDetails(order.id)}
                                        sx={{ fontWeight: 600, backgroundColor:"#0F3460" }}
                                    >
                                        Detayları Gör
                                    </Button>
                                </CardActions>
                            </StyledCard>
                        </Grid>
                    ))}
                </Grid>
            )}
        </Container>
    );
};

export default OrdersPage;