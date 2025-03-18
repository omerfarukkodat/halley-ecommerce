"use client";
import { Fab, Tooltip } from '@mui/material';
import WhatsAppIcon from '@mui/icons-material/WhatsApp';
import { useEffect, useState } from "react";

const WhatsappButton = () => {
    const [isVisible, setIsVisible] = useState(false);

    const handleWhatsAppClick = () => {
        const phoneNumber = '905555555555';
        const message = 'Merhaba, yardıma ihtiyacım var!';
        const url = `https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`;
        window.open(url, '_blank');
    };

    useEffect(() => {
        const toggleVisibility = () => {
            if (window.pageYOffset > 300) {
                setIsVisible(true);
            } else {
                setIsVisible(false);
            }
        };

        window.addEventListener('scroll', toggleVisibility);
        return () => window.removeEventListener('scroll', toggleVisibility);
    }, []);

    return (
        <div style={{
            display: isVisible ? 'block' : 'none',
            position: 'fixed',
            bottom: '20px',
            left: '20px',
            zIndex: 1000
        }}>
            <Tooltip
                title="WhatsApp Destek"
                arrow
                PopperProps={{
                    modifiers: [
                        {
                            name: "preventOverflow",
                            options: {
                                boundary: "window",
                            },
                        },
                    ],
                }}
                componentsProps={{
                    tooltip: {
                        sx: {
                            backgroundColor: "#25D366",
                            color: "#ffffff",
                            fontSize: "16px",
                            padding: "10px",
                            borderRadius: "8px",
                            fontWeight: "bold"
                        },
                    },
                    arrow: {
                        sx: {
                            color: "#25D366",
                        },
                    },
                }}
            >
                <Fab
                    sx={{
                        backgroundColor: "#25D366",
                        color: "#ffffff",
                        width: 56,
                        height: 56,
                        "&:hover": {
                            backgroundColor: "#25D366",
                            color: "#ffffff"
                        }
                    }}
                    onClick={handleWhatsAppClick}
                    aria-label="WhatsApp Destek"
                >
                    <WhatsAppIcon sx={{ fontSize: 44 }} />
                </Fab>
            </Tooltip>
        </div>
    );
};

export default WhatsappButton;
