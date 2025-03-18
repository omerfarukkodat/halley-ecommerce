"use client";

import React, { useState, useEffect } from "react";
import { Box, Typography, TextField, Button, Checkbox, FormControlLabel, Alert, Snackbar,
    Paper, Container, InputAdornment } from "@mui/material";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import PhoneIcon from "@mui/icons-material/Phone";
import EmailIcon from "@mui/icons-material/Email";
import TurkishFlagIcon from "@/components/auth/TurkishFlagIcon";

const ProfilePage = () => {
    const { profile, fetchProfile, user, updateProfile, logout } = useAuth();
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        smsNotification: false,
        callNotification: false,
        emailNotification: false,
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const router = useRouter();

    useEffect(() => {
        if (!user) {
            router.push("/giris");
        } else {
            fetchProfile();
        }
    }, [user]);

    useEffect(() => {
        if (profile) {
            const phoneWithPrefix =
                profile.phone ? profile.phone.replace("+90", "") : "";
            setFormData({
                firstName: profile.firstName || "",
                lastName: profile.lastName || "",
                email: profile.email || "",
                phone: phoneWithPrefix,
                smsNotification: profile.smsNotification || false,
                callNotification: profile.callNotification || false,
                emailNotification: profile.emailNotification || false,
            });
        }
    }, [profile]);

    const handleChange = (e) => {
        const { name, value, checked, type } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handlePhoneChange = (e) => {
        const value = e.target.value.replace(/\D/g, "");
        if (value.length <= 10) {
            setFormData({
                ...formData,
                phone: value,
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const phoneWithPrefix = `+90${formData.phone}`;
            const updatedFormData = { ...formData, phone: phoneWithPrefix };

            await updateProfile(updatedFormData);
            setSuccess(true);
            setError(null);
            if (profile.email !== formData.email) {
                setTimeout(() => {
                    logout();
                }, 2000);
                alert("E-posta adresiniz değiştiği için çıkış yapılıyor. Lütfen yeniden giriş yapın.");
            }
        } catch (error) {
            setError(error.response?.data?.message || "Profil güncellenirken bir hata oluştu.");
        }
    };

    return (
        <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
            <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
                <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", color: "#0F3460",
                    borderBottom: "1px solid #ddd" }}>
                    Üyelik Bilgilerim
                </Typography>

                {error && (
                    <Snackbar anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
                              open={!!error} autoHideDuration={6000} onClose={() => setError(null)}>
                        <Alert
                            sx={{
                                backgroundColor: "#0F3460",
                                color: "#ffffff",
                                borderRadius: 2,
                            }} severity="error" onClose={() => setError(null)}>
                            {error}
                        </Alert>
                    </Snackbar>
                )}

                {success && (
                    <Snackbar
                        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
                        open={success} autoHideDuration={6000} onClose={() => setSuccess(false)}>
                        <Alert sx={{
                            backgroundColor: "#0F3460",
                            color: "#ffffff",
                            borderRadius: 2,
                        }} severity="success" onClose={() => setSuccess(false)}>
                            Profil başarıyla güncellendi!
                        </Alert>
                    </Snackbar>
                )}

                <form onSubmit={handleSubmit}>
                    <Typography variant="h6" gutterBottom sx={{ fontWeight: "bold", color: "#0F3460", mt: 2 }}>
                        İletişim Bilgileri ve İzinlerim
                    </Typography>

                    <TextField
                        fullWidth
                        margin="normal"
                        label="Telefon Numarası"
                        name="phone"
                        value={formData.phone}
                        onChange={handlePhoneChange}
                        required
                        sx={{ backgroundColor: "#f2f2f2", borderRadius: 2 }}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Box sx={{ display: "flex", alignItems: "center" }}>
                                        <TurkishFlagIcon sx={{  mr: 1 }} />
                                        <Typography sx={{mb:0.3 , color:"#0F3460"}}>+90</Typography>
                                    </Box>
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Typography variant="body1" color="textSecondary"
                                gutterBottom sx={{ display: "flex", alignItems: "center",mt:1,mb:2 }}>
                        <PhoneIcon sx={{ color: "#0F3460", mr: 1 }} />
                        İletişim cepte olsun!
                    </Typography>

                    <FormControlLabel
                        sx={{
                            mt:1,mb:2
                        }}
                        control={
                            <Checkbox
                                name="smsNotification"
                                checked={formData.smsNotification}
                                onChange={handleChange}
                                sx={{ color: "#0F3460" }}
                            />
                        }
                        label="Kampanya ve indirimlerden daha hızlı haberdar olabilmek için
                         SMS ile bilgilendirilmek istiyorum."
                    />

                    <FormControlLabel
                        sx={{
                            mt:1,mb:2
                        }}
                        control={
                            <Checkbox
                                name="callNotification"
                                checked={formData.callNotification}
                                onChange={handleChange}
                                sx={{ color: "#0F3460" }}
                            />
                        }
                        label="Kampanyalardan daha kolay haberdar olabilmek için telefonla arama yapılmasını istiyorum."
                    />

                    <TextField
                        fullWidth
                        margin="normal"
                        label="E-Posta"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        sx={{ backgroundColor: "#f2f2f2", borderRadius: 2 , mt:2 ,mb:2 }}
                    />
                    <Typography variant="body1" color="textSecondary" gutterBottom sx={{
                        display: "flex", alignItems: "center", mb:2,mt:1 }}>
                        <EmailIcon sx={{ color: "#0F3460", mr: 1 }} /> {/* E-posta ikonu */}
                        Fırsatları kaçırmanızı istemeyiz!
                    </Typography>

                    <FormControlLabel
                        sx={{
                            mt:1,mb:2
                        }}
                        control={
                            <Checkbox
                                name="emailNotification"
                                checked={formData.emailNotification}
                                onChange={handleChange}
                                sx={{ color: "#0F3460" }}
                            />
                        }
                        label="Kampanya ve indirimlerden daha hızlı haberdar olabilmek için
                         e-posta ile bilgilendirilmek istiyorum."
                    />

                    <Typography variant="h6" gutterBottom sx={{ fontWeight: "bold", color: "#0F3460", mt: 4 }}>
                        Kişisel Bilgilerim
                    </Typography>

                    <TextField
                        fullWidth
                        margin="normal"
                        label="Ad"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                        sx={{ backgroundColor: "#f2f2f2", borderRadius: 2 }}
                    />

                    <TextField
                        fullWidth
                        margin="normal"
                        label="Soyad"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
                        sx={{ backgroundColor: "#f2f2f2", borderRadius: 2 }}
                    />

                    <Box sx={{ mt: 3 }}>
                        <Button type="submit" variant="contained" sx={{ backgroundColor: "#0F3460",
                            "&:hover": {
                            backgroundColor: "#1A5276" } }}>
                            Güncelle
                        </Button>
                    </Box>
                </form>

                <Typography variant="body1" color="textSecondary" sx={{ mt: 4 }}>
                    Üyelik ve KVKK kapsamının detaylarına, Üyelik Sözleşmesi ve halleywall.com Kişisel Verilerin Korunması
                    ve İşlenmesi şartlarının yer aldığı sayfalarımızdan ulaşabilirsiniz.
                </Typography>
            </Paper>
        </Container>
    );
};

export default ProfilePage;