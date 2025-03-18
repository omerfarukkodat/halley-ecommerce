"use client";

import { Box, Button, Typography } from "@mui/material";
import { useRouter } from "next/navigation";

const AllProductsBanner = () => {
    const router = useRouter();

    return (
        <Box
            sx={{
                mt:0,
                width: "100%",
                display: "flex",
                flexDirection: { xs: "column", sm: "row" },
                minHeight: { xs: 450, sm: 600, md: 550, lg:550 , xl:550 },
            }}
        >
            <Box
                sx={{
                    flex: 1,
                    bgcolor: "#364941",
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    textAlign: { xs: "center", sm: "left" },
                    color: "white",
                    p: 3,
                    minHeight: { xs: 350, sm: "auto" },
                    order: { xs: 1, sm: 0 },
                }}
            >
                <Typography variant="h5" fontWeight="bold"  mb={2}>
                    HalleyWall'da, arasından seçim yapabileceğiniz 400'den fazla duvar kağıdı çeşidimiz var.
                </Typography>
                <Typography variant="body1" mb={3}>
                    İlham almak için tüm koleksiyonumuzu inceleyebilir veya aşağıdan istediğiniz kategoriye göre alışveriş yapabilirsiniz.
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    size="large"
                    onClick={() => router.push("/urunler")}
                    sx={{
                        backgroundColor:"#ffffff",
                        color:"#000000",
                        fontWeight: "bold", px: 4 }}
                >
                    Tüm Duvar Kağıtlarını Keşfet
                </Button>
            </Box>

            <Box
                sx={{
                    flex: 1,
                    minHeight: { xs: 450, sm: 550, md: 550 , lg:550 , xl:550 },
                    backgroundImage: "url('/homebanner.webp')",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    order: { xs: 1, sm: 0 },
                }}
            />
        </Box>
    );
};

export default AllProductsBanner;