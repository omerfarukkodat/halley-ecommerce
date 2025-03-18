"use client";

import React from "react";
import { Box, Typography, Divider, Button, useTheme } from "@mui/material";
import { useRouter } from "next/navigation";

const Summary = ({ cartSummary }) => {
    const router = useRouter();
    const theme = useTheme();

    if (!cartSummary || cartSummary.productPrices === 0) {
        return null;
    }

    return (
        <Box
            sx={{
                p: 3,
                border: `1px solid ${theme.palette.divider}`,
                borderRadius: "12px",
                boxShadow: theme.shadows[2],
                backgroundColor: theme.palette.background.paper,
                maxWidth: { xs: "100%", md: "350px" },
            }}
        >
            <Typography variant="h6" fontWeight={700} mb={2} color="primary.main">
                Sepet Özeti
            </Typography>
            <Divider sx={{ mb: 2 }} />

            <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="body1" color="text.secondary">
                    Sipariş Tutarı:
                </Typography>
                <Typography variant="body1" fontWeight={600} color="text.primary">
                    {cartSummary.productPrices.toLocaleString("tr-TR")} TL
                </Typography>
            </Box>

            <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="body1" color="text.secondary">
                    Kargo Ücreti:
                </Typography>
                <Typography variant="body1" fontWeight={600} color="text.primary">
                    {cartSummary.totalShippingCost === 0
                        ? "Ücretsiz"
                        : `${cartSummary.totalShippingCost.toLocaleString("tr-TR", {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2
                        })} TL`}
                </Typography>
            </Box>

            <Box display="flex" justifyContent="space-between" mb={2}>
                <Typography variant="body1" color="text.secondary">
                    Ürün Miktarı:
                </Typography>
                <Typography variant="body1" fontWeight={600} color="text.primary">
                    {cartSummary.totalQuantity}
                </Typography>
            </Box>

            <Divider sx={{ mb: 2 }} />

            <Box display="flex" justifyContent="space-between" mb={3}>
                <Typography variant="body1" fontWeight={700} color="text.primary">
                    Toplam Tutar:
                </Typography>
                <Typography variant="h6" fontWeight={700} color="primary.main">
                    {cartSummary.totalPrice.toLocaleString("tr-TR")} TL
                </Typography>
            </Box>


        </Box>
    );
};

export default Summary;