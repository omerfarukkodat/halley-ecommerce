"use client";

import { Button, Container, Typography, Box } from "@mui/material";
import { useRouter } from "next/navigation";
import Image from "next/image";

const ErrorPage = () => {
    const router = useRouter();

    return (
        <Container maxWidth="xs">
            <Box sx={{ textAlign: "center"}}>
                <Box sx={{ maxWidth: 400, margin: "0 auto", width: "100%", height: "auto" }}>
                    <Image
                        src="/not-found.png"
                        alt="Not Found Image"
                        layout="responsive"
                        width={400}
                        height={400}
                    />
                </Box>

                <Typography variant="h6" sx={{ mb: 1, mt: 1 }}>
                    Aradığınız sayfa bulunmamaktadır ya da kaldırılmıştır.
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                    Lütfen adresi kontrol edip tekrar deneyin.
                </Typography>
                <Button
                    variant="contained"
                    onClick={() => router.push("/")}
                    sx={{
                        backgroundColor: "#0F3460",
                        "&:hover": { backgroundColor: "#0F3460", opacity: 0.9 },
                    }}
                >
                    Ana Sayfaya Dön
                </Button>
            </Box>
        </Container>
    );
};

export default ErrorPage;