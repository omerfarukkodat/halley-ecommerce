'use client';

import React, { useEffect, useState } from 'react';
import { getAllAddresses } from '@/services/addressService';
import { createOrderFromCart } from '@/services/orderService';
import {
    Button,
    FormControl,

    Radio,
    RadioGroup,
    FormControlLabel,
    CircularProgress,
    Typography,
    Box, Grid
} from '@mui/material';
import AddressCard from '@/components/address/AddressCard';
import {useCart} from "@/context/CartContext";
import Summary from "@/components/order/OrderSummary";

const UserOrderForm = ({onSuccess , cartSummary}) => {
    const [addresses, setAddresses] = useState([]);
    const [selectedAddress, setSelectedAddress] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const {updateCartCount} = useCart();

    useEffect(() => {
        const fetchAddresses = async () => {
            try {
                const allAddresses = await getAllAddresses();
                setAddresses(allAddresses);
                setIsLoading(false);
            } catch (error) {
                setError("Adresleri alırken bir hata oluştu.");
                setIsLoading(false);
            }
        };

        fetchAddresses();
    }, []);

    const handleAddressSelect = (event) => {
        setSelectedAddress(event.target.value);
    };

    const handleOrderCreate = async () => {
        if (!selectedAddress) {
            alert("Lütfen bir adres seçin.");
            return;
        }

        try {
            const orderDto = { addressId: selectedAddress };
            const response = await createOrderFromCart(orderDto);
            alert("Siparişiniz başarıyla oluşturuldu.");
            if (onSuccess) onSuccess(response.orderId);


        } catch (error) {
            alert("Sipariş oluşturulurken bir hata oluştu.");
        }finally {
            updateCartCount(0);

        }
    };

    const addressTypeMap = {
        HOME: "Ev",
        WORK: "İş",
        OTHER: "Diğer"
    };

    return (
        <Box sx={{ padding: 3 }}>
            <Grid container spacing={3}>
                <Grid item xs={12} md={8}>
                    <Typography variant="h5" gutterBottom>
                        Adres Seçin
                    </Typography>

                    {isLoading ? (
                        <CircularProgress />
                    ) : error ? (
                        <Typography color="error">{error}</Typography>
                    ) : (
                        <FormControl component="fieldset" fullWidth>
                            <RadioGroup
                                name="address"
                                value={selectedAddress}
                                onChange={handleAddressSelect}
                            >
                                {addresses.length === 0 ? (
                                    <Typography>Hiç adres bulunamadı. Lütfen adres oluşturmak için tıklayın.</Typography>
                                ) : (
                                    addresses.map((address) => (
                                        <FormControlLabel
                                            key={address.id}
                                            value={address.id}
                                            control={<Radio />}
                                            label={
                                                <AddressCard
                                                    address={address}
                                                    addressTypeMap={addressTypeMap}
                                                    onEdit={() => {}}
                                                    onDelete={() => {}}
                                                />
                                            }
                                        />
                                    ))
                                )}
                            </RadioGroup>
                        </FormControl>
                    )}



                    <Box sx={{ marginTop: 3, textAlign: 'center' }}>
                        <Button variant="contained" color="primary" onClick={handleOrderCreate}>
                            Sipariş Oluştur
                        </Button>
                    </Box>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Summary cartSummary={cartSummary} />
                </Grid>
            </Grid>
        </Box>
    );
};

export default UserOrderForm;