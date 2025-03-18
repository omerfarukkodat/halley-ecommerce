"use client";
import React, { useState } from "react";
import { Box, TextField, Button, Typography, Paper } from "@mui/material";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

const AdminLoginPage = () => {
    const router = useRouter();
    const { adminLogin } = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const response = await adminLogin({ email, password });
            if (response.success) {
                router.push("/admin/blog/create");
            } else {
                setError(response.error || "Giriş başarısız. Lütfen bilgilerinizi kontrol edin.");
            }
        } catch (error) {
            setError("Giriş başarısız. Lütfen bilgilerinizi kontrol edin.");
        }
    };

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: "100vh",
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                backgroundSize: "200% 200%",
                animation: "moveBackground 10s linear infinite",
            }}
        >
            <Paper elevation={3} sx={{ p: 4, width: "100%", maxWidth: 400 }}>
                <Typography variant="h4" gutterBottom sx={{ textAlign: "center" }}>
                    Admin Girişi
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Şifre"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        margin="normal"
                        required
                    />
                    {error && (
                        <Typography color="error" sx={{ mt: 2 }}>
                            {error}
                        </Typography>
                    )}
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        fullWidth
                        sx={{ mt: 3 }}
                    >
                        Giriş Yap
                    </Button>
                </form>
            </Paper>
        </Box>
    );
};

export default AdminLoginPage;