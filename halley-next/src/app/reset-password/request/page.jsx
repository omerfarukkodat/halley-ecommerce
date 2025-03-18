"use client";

import { useEffect, useState } from "react";
import { TextField, Button, Container, Typography, Box, Alert, IconButton, CircularProgress } from "@mui/material";
import userService from "@/services/userService";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

const ResetPasswordRequest = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const [alertSeverity, setAlertSeverity] = useState("success");
    const router = useRouter();
    const { user } = useAuth();

    useEffect(() => {
        if (user) {
            router.push("/");
        }
    }, [user, router]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await userService.resetPasswordRequest(email);
            setMessage(
                `E-postanı gönderdik! ${email} adresine şifre yenileme linkini ilettik. 
                E-postanın gelmesi birkaç dakika sürebilir. Lütfen Spam (Gereksiz) klasörünü de kontrol et.`
            );
            setAlertSeverity("success");
        } catch (error) {
            setMessage(error.message);
            setAlertSeverity("error");
        }
    };

    if (user) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center">
                <CircularProgress size={60} thickness={4} sx={{ color: "#0F3460" }} />
            </Box>
        );
    }

    return (
        <Box
            sx={{
                minHeight: "100vh",
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                backgroundSize: '200% 200%',
                animation: 'moveBackground 10s linear infinite',
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                position: "relative",
                overflow: "hidden",
            }}
        >

            <Container maxWidth="xs" sx={{ position: "relative", zIndex: 2 }}>
                <Box
                    sx={{
                        background: "rgba(255, 255, 255, 0.2)",
                        backdropFilter: "blur(10px)",
                        p: 3,
                        borderRadius: 3,
                        boxShadow: "0px 4px 10px rgba(0,0,0,0.2)",
                        textAlign: "center",
                    }}
                >
                    <Box sx={{ display: "flex", justifyContent: "flex-start" }}>
                        <IconButton
                            onClick={() => router.push("/giris")}
                            sx={{
                                backgroundColor: "#0F3460",
                                color: "#fff",
                                mb: 1,
                                "&:hover": {
                                    backgroundColor: "#0F3460",
                                    color: "#fff",
                                },
                            }}
                        >
                            <ArrowBackIcon sx={{color:"#ffffff"}} />
                        </IconButton>
                    </Box>

                    <Typography sx={{ fontSize: 28, fontWeight: 600, color: "#0F3460" }} variant="body1">
                        Şifreni mi unuttun?
                    </Typography>
                    <Typography variant="body2" sx={{ fontSize: 18, mt: 2, color: "#555" }}>
                        Olur böyle şeyler 😌️ Yeni şifre oluşturmak için e-posta adresini girebilirsin.
                    </Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField
                            fullWidth
                            label="Email"
                            variant="outlined"
                            margin="normal"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            sx={{
                                backgroundColor: "rgba(255, 255, 255, 0.8)",
                                borderRadius: 1,
                            }}
                        />
                        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, backgroundColor: "#0F3460" }}>
                            Devam Et
                        </Button>
                    </form>
                    {message && (
                        <Alert severity={alertSeverity} sx={{ mt: 2 }}>
                            {message}
                        </Alert>
                    )}
                </Box>
            </Container>
        </Box>
    );
};

export default ResetPasswordRequest;