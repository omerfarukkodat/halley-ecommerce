'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import {
    Box,
    CircularProgress,
    Typography,
    Paper,
    Grid,
    List,
    ListItem,
    ListItemText,
    Divider,
    Alert
} from '@mui/material';
import GuestOrderForm from '@/components/order/GuestOrderForm';
import UserOrderForm from '@/components/order/UserOrderForm';
import { useCart } from '@/context/CartContext';
import { getOrderSummary } from '@/services/orderService';

const OrderPage = () => {
    const router = useRouter();
    const { cart } = useCart();
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [orderSummary, setOrderSummary] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        setIsAuthenticated(!!token);
        setLoading(false);
    }, []);

    useEffect(() => {
        if (!loading && (!cart || cart.items.length === 0)) {
            router.push('/');
        }
    }, [cart, loading, router]);

    const handleOrderSuccess = async (orderId) => {
        try {

            const summary = await getOrderSummary(orderId);
            setOrderSummary(summary);
        } catch (err) {
            setError('Sipariş özeti alınamadı. Lütfen daha sonra tekrar deneyin.');
            console.error('Order summary error:', err);
        }
    };

    if (loading || !cart || cart.items.length === 0) {
        return (
            <Box display="flex" justifyContent="center" mt={4}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box mt={4}>
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3, maxWidth: 1200, mx: 'auto' }}>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold' }}>
                Siparişi Tamamla
            </Typography>

            {orderSummary ? (
                <OrderSummaryView summary={orderSummary} />
            ) : isAuthenticated ? (
                <UserOrderForm onSuccess={handleOrderSuccess} />
            ) : (
                <GuestOrderForm onSuccess={handleOrderSuccess} />
            )}
        </Box>
    );
};

const OrderSummaryView = ({ summary }) => (
    <Paper elevation={3} sx={{ p: 3, mt: 2 }}>
        <Typography variant="h5" gutterBottom sx={{ mb: 3 }}>
            Sipariş Özetiniz
        </Typography>

        <Grid container spacing={4}>
            <Grid item xs={12} md={6}>
                <Typography variant="h6" color="text.secondary" gutterBottom>
                    Ödeme Bilgileri
                </Typography>
                <List>
                    <ListItem>
                        <ListItemText
                            primary="Ürün Toplam"
                            secondary={`${summary.totalPrice} TL`}
                        />
                    </ListItem>
                    <Divider />
                    <ListItem>
                        <ListItemText
                            primary="Kargo Ücreti"
                            secondary={`${summary.shippingCost} TL`}
                        />
                    </ListItem>
                    <Divider />
                    <ListItem sx={{ fontWeight: 'bold' }}>
                        <ListItemText
                            primary="Toplam Tutar"
                            secondary={`${summary.finalPrice} TL`}
                        />
                    </ListItem>
                </List>
            </Grid>

            <Grid item xs={12} md={6}>
                <Typography variant="h6" color="text.secondary" gutterBottom>
                    Ödeme Yöntemi
                </Typography>
                <Alert severity="info" sx={{ mb: 2 }}>
                    {summary.paymentMethod === 'WHATSAPP'
                        ? 'WhatsApp üzerinden ödeme talimatları gönderilecektir.'
                        : 'Lütfen ödeme yapmak için aşağıdaki IBAN numarasını kullanın.'}
                </Alert>

                {summary.paymentMethod === 'IBAN_TRANSFER' && (
                    <Box sx={{ backgroundColor: '#f5f5f5', p: 2, borderRadius: 1 }}>
                        <Typography variant="body2">
                            TR00 1234 5678 9012 3456 7890
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                            Lütfen sipariş numarasını açıklamaya eklemeyi unutmayın.
                        </Typography>
                    </Box>
                )}
            </Grid>
        </Grid>
    </Paper>
);

export default OrderPage;