"use client";
import React, {useState} from "react";
import {
    ListItem,
    IconButton,
    Typography,
    Box,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import {useRouter} from "next/navigation";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";

const CartItem = ({item, onIncrease, onDecrease, onRemove, isLoading}) => {
    const [localLoading, setLocalLoading] = useState(false);
    const router = useRouter();
    const productQuantity = item.quantity;

    const handleDirect = () => {
        router.push(`/urun/${item.product.slug}-${item.product.id}`);
    };

    const handleIncrease = async (productId) => {
        setLocalLoading(true);
        await onIncrease(productId);
        setLocalLoading(false);
    };

    const handleDecrease = async (productId) => {
        setLocalLoading(true);
        await onDecrease(productId);
        setLocalLoading(false);
    };

    const handleRemove = async (productId) => {
        setLocalLoading(true);
        await onRemove(productId);
        setLocalLoading(false);
    };

    return (
        <ListItem
            sx={{
                padding: 0,
                display: "flex",
                marginLeft: "2px",
                minHeight: "150px",
                ":hover": {bgcolor: "#f3f4f6"},
            }}
        >
            <Box display="flex" alignItems="center" width="100%" justifyContent="space-between">
                <Box display="flex" alignItems="center">
                    <Box sx={{width: 135, height: 135, flexShrink: 0}}>
                        <img
                            onClick={handleDirect}
                            src={item.product.imageUrls[0]}
                            alt={item.product.name}
                            style={{
                                cursor: "pointer",
                                width: "100%",
                                height: "100%",
                                borderRadius: 2,
                                objectFit: "cover",
                            }}
                        />
                    </Box>
                    <Box display="flex" flexDirection="column" sx={{ml: 2, flex: 1}}>
                        <Typography
                            variant="body1"
                            fontWeight={600}
                            onClick={handleDirect}
                            sx={{
                                color: "#000000",
                                cursor: "pointer",
                                '&:hover': {
                                    color: '#ff7f00',
                                },
                                mb: 1,
                            }}
                        >
                            {item.product.name}
                        </Typography>

                        <Box display="flex" alignItems="center" mt={1}>
                            <IconButton
                                onClick={() => handleRemove(item.product.id)}
                                disabled={localLoading || isLoading}
                                sx={{display: "flex", alignItems: "center"}}
                            >
                                <DeleteForeverIcon/>
                            </IconButton>
                            <Typography
                                onClick={() => handleRemove(item.product.id)}
                                sx={{
                                    cursor: "pointer",
                                    fontSize: "14px",
                                    color: "black",
                                    fontWeight: "bold",
                                    ":hover": {
                                        color: "#ff7f00",
                                    },
                                }}
                            >
                                Sil
                            </Typography>
                        </Box>
                    </Box>
                </Box>

                <Box display="flex" flexDirection="column" justifyContent="space-between" sx={{ml: 2}}>
                    <Box>
                        <Typography sx={{flex: 1, textAlign: "center", fontWeight: "bold"}}>
                            {(item.quantity * item.product.discountedPrice).toLocaleString("tr-TR", {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2
                            })} TL
                        </Typography>
                    </Box>

                    <Box display="flex" alignItems="center" sx={{ flex: 1, justifyContent: "center", marginTop: 3 }}>
                        <IconButton
                            onClick={() => handleDecrease(item.product.id)}
                            disabled={localLoading || isLoading}
                        >
                            <RemoveIcon
                                sx={{
                                    color: "#ffffff",
                                    backgroundColor: "#0F3460",
                                    borderRadius: '2px',
                                    width: "20px",
                                    height: '24px',
                                    fontSize: '0.75rem',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                }}
                            />
                        </IconButton>
                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{
                                fontSize: "16px",
                                textAlign: "center",
                                minWidth: "20px",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                            }}
                        >
                            {item.quantity}
                        </Typography>
                        <IconButton
                            disabled={localLoading || isLoading || productQuantity >= item.product.stock}
                            onClick={() => handleIncrease(item.product.id)}
                        >
                            <AddIcon
                                sx={{
                                    color: "#ffffff",
                                    backgroundColor: productQuantity >= item.product.stock ? "#ccc" : "#0F3460",
                                    borderRadius: '2px',
                                    width: "20px",
                                    height: '24px',
                                    fontSize: '0.75rem',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                }}
                            />
                        </IconButton>
                    </Box>
                </Box>
            </Box>
        </ListItem>
    );
};

export default CartItem;