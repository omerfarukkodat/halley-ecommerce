"use client";

import React, { useEffect, useState, memo } from "react";
import {
    AppBar,
    Toolbar,
    Box,
    useMediaQuery,
    createTheme,
    ThemeProvider,
    Skeleton,
    IconButton,
    Slide,
} from "@mui/material";
import CategorySidebar from "@/components/navbar/categorySidebar/CategorySidebar";
import Logo from "@/components/navbar/Logo";
import MobileCategorySidebar from "@/components/navbar/categorySidebar/MobileCategorySidebar";
import SearchBar from "@/components/navbar/search/Search";
import User from "@/components/navbar/user/DesktopUser";
import Cart from "@/components/navbar/Cart";
import SearchIcon from "@mui/icons-material/Search";
import CloseIcon from "@mui/icons-material/Close";

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

const NavbarMobileSkeleton = () => (
    <AppBar position="fixed" sx={{ width: "100%", height: "65px", bgcolor: "#5899dd" }}>
        <Toolbar
            sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                paddingX: 2,
            }}
        >
            <Skeleton variant="rectangular" width={50} height={40} />
            <Skeleton variant="circular" width={40} height={40} />
            <Skeleton variant="circular" width={40} height={40} />
        </Toolbar>
    </AppBar>
);

const NavbarDesktopSkeleton = () => (
    <AppBar position="fixed" sx={{ width: "100%", height: "65px", bgcolor: "#5899dd" }}>
        <Toolbar
            sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                paddingX: 3,
            }}
        >
            <Skeleton variant="rectangular" width={110} height={40} />
            <Skeleton variant="rectangular" width="40%" height={40} />
            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                <Skeleton variant="circular" width={40} height={40} />
                <Skeleton variant="circular" width={40} height={40} />
            </Box>
        </Toolbar>
        <Skeleton variant="rectangular" width="100%" height={100} />
    </AppBar>
);

const NavbarMobile = memo(() => {
    const [isSearchVisible, setIsSearchVisible] = useState(false);

    const handleSearchClick = () => {
        setIsSearchVisible(true);
    };

    const handleSearchClose = () => {
        setIsSearchVisible(false);
    };

    return (
        <ThemeProvider theme={theme}>
            <AppBar
                position="fixed"
                sx={{
                    width: "100%",
                    height: "65px",
                    bgcolor: "#5899dd",
                    display: "flex",
                    flexDirection: "column",
                }}
            >
                <Toolbar
                    sx={{
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        padding: 0,
                        flex: 0,
                    }}
                >
                    <Slide direction="right" in={!isSearchVisible} mountOnEnter unmountOnExit>
                        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                            <Logo />
                        </Box>
                    </Slide>

                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "flex-end",
                            gap: 2,
                        }}
                    >
                        <Slide direction="left" in={!isSearchVisible} mountOnEnter unmountOnExit>
                            <IconButton onClick={handleSearchClick}>
                                <SearchIcon sx={{ color: "white" }} />
                            </IconButton>
                        </Slide>

                        <Slide direction="left" in={!isSearchVisible} mountOnEnter unmountOnExit>
                            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                                <Cart />
                                <MobileCategorySidebar />
                            </Box>
                        </Slide>

                        <Slide direction="down" in={isSearchVisible} mountOnEnter unmountOnExit>
                            <Box
                                sx={{
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center",
                                    justifyItems: "center",
                                    backgroundColor: "#5899dd",
                                    position: "absolute",
                                    left: 0,
                                    right: 0,
                                    width: "100%",

                                    margin: "0 auto",
                                    overflow: "hidden",
                                }}
                            >
                                <Box sx={{ display: "flex", alignItems: "center", width: "100%" }}>
                                    <IconButton onClick={handleSearchClose} sx={{ color: "white" }}>
                                        <CloseIcon sx={{fontSize:40}} />
                                    </IconButton>

                                    <SearchBar sx={{
                                        width: "50%",
                                    }} />
                                </Box>
                            </Box>
                        </Slide>
                    </Box>
                </Toolbar>
            </AppBar>
        </ThemeProvider>
    );
});

const NavbarDesktop = memo(() => (
    <ThemeProvider theme={createTheme()}>
        <AppBar position="fixed" sx={{ width: "100%", height: "64px", bgcolor: "#5899dd" }}>
            <Toolbar
                sx={{
                    flexDirection: "row",
                    padding: 0,
                    boxSizing: "border-box",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Logo />
                <SearchBar />
                <Box
                    sx={{
                        display: "flex",
                        alignItems: "left",
                        justifyContent: "flex-end",
                        width: "100%",
                    }}
                >
                    <User />
                    <Cart />
                </Box>
            </Toolbar>
        </AppBar>
        <CategorySidebar />
    </ThemeProvider>
));

const Navbar = () => {
    const isMobile = useMediaQuery("(max-width:1024px)");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => setLoading(false));
    }, []);


    return (
        <ThemeProvider theme={theme}>
            {loading ? (
                isMobile ? <NavbarMobileSkeleton/> : <NavbarDesktopSkeleton />
            ) : (
                isMobile ? <NavbarMobile /> : <NavbarDesktop />
            )}
        </ThemeProvider>
    );
};

export default Navbar;