'use client';

import React, { useState } from 'react';
import {
    Container,
    Typography,
    TextField,
    Button,
    MenuItem,
    Snackbar,
    Alert,
} from '@mui/material';
import { createAddress } from '/src/services/addressService';

const AddressForm = ({ onSuccess, onCancel }) => {
    const [formData, setFormData] = useState({
        city: '',
        district: '',
        neighborhood: '',
        generalAddress: '',
        zipCode: '',
        addressType: 'HOME',
    });
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const newAddress = await createAddress(formData);
            setSnackbarMessage('Adres başarıyla eklendi.');
            setSnackbarSeverity('success');
            setOpenSnackbar(true);

            // Başarılı olduğunda onSuccess callback'ini çağır
            if (onSuccess) {
                onSuccess(newAddress);
            }
        } catch (error) {
            console.error('Adres eklenirken hata oluştu:', error);
            setSnackbarMessage('Adres eklenirken hata oluştu.');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" gutterBottom sx={{
                borderBottom: "1px solid #ddd",
                fontWeight: 500,
                color: "#0F3460"
            }}>
                Yeni Adres Ekle
            </Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="Şehir"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="İlçe"
                    name="district"
                    value={formData.district}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Mahalle"
                    name="neighborhood"
                    value={formData.neighborhood}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Genel Adres"
                    name="generalAddress"
                    value={formData.generalAddress}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Posta Kodu"
                    name="zipCode"
                    value={formData.zipCode}
                    onChange={handleChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    select
                    label="Adres Türü"
                    name="addressType"
                    value={formData.addressType}
                    onChange={handleChange}
                    margin="normal"
                    required
                >
                    <MenuItem value="HOME">Ev</MenuItem>
                    <MenuItem value="WORK">İş</MenuItem>
                    <MenuItem value="OTHER">Diğer</MenuItem>
                </TextField>
                <Button type="submit" variant="contained" sx={{ mt: 3, backgroundColor: "#0F3460", mr: 2 }}>
                    Kaydet
                </Button>
                <Button type="button" variant="outlined" sx={{ mt: 3 }} onClick={onCancel}>
                    İptal
                </Button>
            </form>
            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Container>
    );
};

export default AddressForm;