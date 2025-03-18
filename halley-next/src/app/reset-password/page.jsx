"use client";

import { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { TextField, Button, Container, Typography, Box, Snackbar, Alert } from "@mui/material";
import userService from "@/services/userService";

const ResetPasswordForm = () => {
    const searchParams = useSearchParams();
    const token = searchParams.get("token");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");
    const [isTokenValid, setIsTokenValid] = useState(false);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarSeverity, setSnackbarSeverity] = useState("success");
    const router = useRouter();

    useEffect(() => {
        const validateToken = async () => {
            try {
                const response = await userService.validateToken(token);
                if (response.data === true) {
                    setIsTokenValid(true);
                } else {
                    router.push("/hata");
                }
            } catch (error) {
                router.push("/hata");
            }
        };

        if (token) {
            validateToken();
        } else {
            router.push("/hata");
        }
    }, [token, router]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (newPassword !== confirmPassword) {
            setMessage("Parolalar uyuşmuyor!");
            setSnackbarSeverity("error");
            setOpenSnackbar(true);
            return;
        }
        try {
            const passwordResetDto = { newPassword, confirmPassword };
            await userService.resetPassword(token, passwordResetDto);
            setMessage("Şifreniz başarıyla yenilendi.");
            setSnackbarSeverity("success");
            setOpenSnackbar(true);

            setTimeout(() => {
                router.push("/giris");
            }, 1000);
        } catch (error) {
            setMessage(error.response?.data || "Şifreniz yenilenirken hata oluştu.");
            setSnackbarSeverity("error");
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = (event, reason) => {
        if (reason === "clickaway") return;
        setOpenSnackbar(false);
    };

    if (!isTokenValid && token) {
        return <Typography sx={{ mt: 4, textAlign: "center" }}>Doğrulama yapılıyor...</Typography>;
    }

    return (
        <Box
            sx={{
                minHeight: "100vh",
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                position: "relative",
                overflow: "hidden",
            }}
        >
            <Box
                sx={{
                    position: "absolute",
                    width: 400,
                    height: 400,
                    borderRadius: "50%",
                    background: "rgba(255, 255, 255, 0.1)",
                    filter: "blur(100px)",
                    top: "20%",
                    left: "10%",
                    zIndex: 1,
                }}
            />
            <Box
                sx={{
                    position: "absolute",
                    width: 500,
                    height: 500,
                    borderRadius: "50%",
                    background: "rgba(255, 255, 255, 0.08)",
                    filter: "blur(120px)",
                    bottom: "15%",
                    right: "10%",
                    zIndex: 1,
                }}
            />

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
                    <Typography sx={{ fontSize: 28, fontWeight: 600, color: "#fff" }}>
                        Yeni Şifre Belirle
                    </Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField
                            fullWidth
                            label="Yeni Şifre"
                            type="password"
                            variant="outlined"
                            margin="normal"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                            sx={{ backgroundColor: "rgba(255, 255, 255, 0.8)", borderRadius: 1 }}
                        />
                        <TextField
                            fullWidth
                            label="Şifreyi Doğrula"
                            type="password"
                            variant="outlined"
                            margin="normal"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                            sx={{ backgroundColor: "rgba(255, 255, 255, 0.8)", borderRadius: 1 }}
                        />
                        <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, backgroundColor: "#0F3460" }}>
                            Şifreyi Sıfırla
                        </Button>
                    </form>
                    <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                        <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: "100%" }}>
                            {message}
                        </Alert>
                    </Snackbar>
                </Box>
            </Container>
        </Box>
    );
};

export default ResetPasswordForm;