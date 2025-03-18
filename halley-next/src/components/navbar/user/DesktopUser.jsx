"use client";

import { Box, createTheme, IconButton, Menu, MenuItem, ThemeProvider, Typography, useMediaQuery } from "@mui/material";
import { PersonRounded } from "@mui/icons-material";
import React, { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import ImportContactsIcon from '@mui/icons-material/ImportContacts';
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import PersonIcon from "@mui/icons-material/Person";
import LogoutIcon from "@mui/icons-material/Logout";
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';

const theme = createTheme({
    breakpoints: {
        values: {
            xs: 0,
            sm: 600,
            md: 960,
            lg: 1280,
            xl: 1920,
        },
    },
});

const User = () => {
    const { user, logout, loading } = useAuth();
    const [anchorEl, setAnchorEl] = useState(null);
    const isMobile = useMediaQuery(theme.breakpoints.down("md"));
    const menuRef = useRef(null);
    const router = useRouter();

    const handleCloseMenu = () => {
        setAnchorEl(null);
    };

    const handleMenuClick = (event) => {
        event.stopPropagation();
        if (anchorEl) {
            setAnchorEl(null);
        } else {
            setAnchorEl(event.currentTarget);
        }
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (anchorEl && !anchorEl.contains(event.target)) {
                setAnchorEl(null);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [anchorEl]);

    if (isMobile) {
        return null;
    }



    return (
        <ThemeProvider theme={createTheme()}>
            {!user ? (
                <Box sx={{ display:"flex", alignItems: "center", textAlign: "center",  }}>
                    <IconButton sx={{pointerEvents: "none"}} >
                        <PersonRounded sx={{ height: "30px", width: "30px", color: "#ffffff" }} />
                    </IconButton>
                    <Typography
                        variant="body1"
                        onClick={() => {router.push("/giris")}}
                        sx={{
                            cursor: "pointer",
                            fontFamily:"Quicksand",
                            color: "#ffffff",
                            "&:hover": {
                                textDecoration: "underline"
                                , color: "#ff7f00"
                            },
                        }}
                    >
                        Giriş Yap
                    </Typography>
                    <FiberManualRecordIcon sx={{ fontSize: 8, color: "#ffffff", mx: 1 }} />

                    <Typography
                        variant="body1"
                        onClick={() => {router.push("/uye-ol")}}
                        sx={{
                            cursor: "pointer",
                            fontFamily:"Quicksand",
                            color: "#ffffff",
                            "&:hover": {
                                textDecoration: "underline"
                                , color: "#ff7f00"
                            },
                        }}
                    >
                        Üye Ol
                    </Typography>
                </Box>
            ) : user ? (
                <Box sx={{ display: "flex", alignItems: "center", cursor: "pointer", marginLeft: "16px" }}>
                    <IconButton>
                        <PersonRounded sx={{ height: "30px", width: "30px", color: "#ffffff" }} />
                    </IconButton>
                    <Typography
                        onClick={handleMenuClick}
                        sx={{
                            fontFamily:"Quicksand",
                            marginRight: "8px",
                            color: "white",
                            "&:hover": { textDecoration: "underline", color: "#ff7f00" },
                        }}
                    >
                        Hesabım
                    </Typography>
                    <Menu  anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleCloseMenu}>
                        <MenuItem sx={{fontFamily:"Quicksand" , borderBottom:"1px solid #ddd", fontWeight:700}} onClick={() => router.push("/uyelik/profilim")}><PersonIcon sx={{color:"#0F3460" , mr:2}} />Kullanıcı Bilgilerim</MenuItem>
                        <MenuItem sx={{fontFamily:"Quicksand", borderBottom:"1px solid #ddd", fontWeight:700}} onClick={() => router.push("/uyelik/siparislerim")}><ShoppingCartIcon sx={{color:"#0F3460" , mr:2}} />Siparişlerim</MenuItem>
                        <MenuItem sx={{fontFamily:"Quicksand", borderBottom:"1px solid #ddd", fontWeight:700}} onClick={() => router.push("/uyelik/adreslerim")}><ImportContactsIcon sx={{color:"#0F3460" , mr:2}} />Adreslerim</MenuItem>
                        <MenuItem sx={{fontFamily:"Quicksand", borderBottom:"1px solid #ddd", fontWeight:700}} onClick={logout}> <LogoutIcon sx={{color:"#0F3460" , mr:2}}/>Çıkış Yap</MenuItem>
                    </Menu>
                </Box>
            ) : null}
        </ThemeProvider>
    );
};

export default User;


