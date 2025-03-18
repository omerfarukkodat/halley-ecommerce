"use client";

import React, { useEffect, useState } from 'react';
import {
    Container,
    Typography,
    CircularProgress,
    Grid,
    Button,
    Box
} from '@mui/material';
import { getAllAddresses, deleteAddressById, updateAddressById } from "@/services/addressService";
import { useRouter } from 'next/navigation';
import AddIcon from "@mui/icons-material/Add";
import AddressCard from "@/components/address/AddressCard";
import AddressDialog from "@/components/address/AddressDialog";
import AddressSnackbar from "@/components/address/AddressSnackbar";

const AddressesPage = () => {
    const [addresses, setAddresses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [openDialog, setOpenDialog] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [selectedAddress, setSelectedAddress] = useState(null);
    const router = useRouter();

    const addressTypeMap = {
        HOME: "Ev Adresim",
        WORK: "İş Adresim",
        OTHER: "Diğer"
    };

    useEffect(() => {
        const fetchAddresses = async () => {
            try {
                const data = await getAllAddresses();
                setAddresses(data);
            } catch (error) {
                console.error('Adresler alınırken hata oluştu:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchAddresses();
    }, []);

    const handleDeleteAddress = async (addressId) => {
        try {
            await deleteAddressById(addressId);
            setAddresses(addresses.filter((address) => address.id !== addressId));
            setSnackbarMessage('Adres başarıyla silindi.');
            setSnackbarSeverity('success');
            setOpenSnackbar(true);
        } catch (error) {
            console.error('Adres silinirken hata oluştu:', error);
            setSnackbarMessage('Adres silinirken hata oluştu.');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    const handleEditAddress = (address) => {
        setSelectedAddress(address);
        setOpenDialog(true);
    };

    const handleUpdateAddress = async () => {
        try {
            await updateAddressById(selectedAddress.id, selectedAddress);
            setAddresses(prevAddresses =>
                prevAddresses.map((address) =>
                    address.id === selectedAddress.id ? { ...address, ...selectedAddress } : address
                )
            );
            setSnackbarMessage('Adres başarıyla güncellendi.');
            setSnackbarSeverity('success');
            setOpenSnackbar(true);
            setOpenDialog(false);
        } catch (error) {
            console.error('Adres güncellenirken hata oluştu:', error);
            setSnackbarMessage('Adres güncellenirken hata oluştu.');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSelectedAddress(null);
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    if (loading) {
        return (
            <Container style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ mt: 1, mb: 4 }}>
            <Typography variant="body1" gutterBottom sx={{
                fontWeight: 600,
                fontSize: "26px",
                color: "#0F3460"
            }}>
                Adreslerim
            </Typography>
            <Box sx={{ borderColor: "#0F3460" }}>
                <Button
                    variant="contained"
                    onClick={() => router.push('/uyelik/adreslerim/adresEkle')}
                    sx={{
                        mb: 3, color: "#0F3460", fontWeight: 600, backgroundColor: "#ffffff",
                        border: "1px solid #0F3460",
                        ":hover": {
                            backgroundColor: "#b0b3b0",
                        }
                    }}
                >
                    <AddIcon sx={{ mr: 0.5, ml: 0 }} />
                    Yeni Teslimat Adresi Ekle
                </Button>
            </Box>

            {addresses.length === 0 ? (
                <Typography variant="body1">Henüz adresiniz bulunmamaktadır.</Typography>
            ) : (
                <Grid container spacing={3}>
                    {addresses.map((address, index) => (
                        <Grid item xs={12} sm={6} md={4} key={address?.id || index}>
                            <AddressCard
                                address={address}
                                addressTypeMap={addressTypeMap}
                                onEdit={handleEditAddress}
                                onDelete={handleDeleteAddress}
                            />
                        </Grid>
                    ))}
                </Grid>
            )}

            <AddressDialog
                open={openDialog}
                onClose={handleCloseDialog}
                selectedAddress={selectedAddress}
                onUpdate={handleUpdateAddress}
                onChange={setSelectedAddress}
            />

            <AddressSnackbar
                open={openSnackbar}
                onClose={handleCloseSnackbar}
                message={snackbarMessage}
                severity={snackbarSeverity}
            />
        </Container>
    );
};

export default AddressesPage;