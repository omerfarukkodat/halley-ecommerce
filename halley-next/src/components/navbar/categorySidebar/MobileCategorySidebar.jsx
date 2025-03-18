"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import {
    Box,
    Button,
    IconButton,
    Drawer,
    useTheme,
    useMediaQuery,
    Skeleton,
    Divider,
    List,
    ListItem,
    ListItemText,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import Logo from "@/components/navbar/Logo";
import { useAuth } from "@/context/AuthContext";
import {useCategory} from "@/context/CategoryContext";

const MobileCategorySidebar = () => {

    const [drawerOpen, setDrawerOpen] = useState(false);
    const [subDrawerOpen, setSubDrawerOpen] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const router = useRouter();
    const { user } = useAuth();
    const { categories, loading } = useCategory();


    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("1025"));



    const handleHomeClick = () => {
        router.push("/");
        setDrawerOpen(false);
    };

    const handleLoginClick = () => {
        router.push("/giris");
        setDrawerOpen(false);
    };

    const handleProfileClick = () => {
        router.push("/uyelik");
        setDrawerOpen(false);
    };

    const handleCategoryClick = (category) => {
        setSelectedCategory(category);
        setSubDrawerOpen(true);
    };

    const handleCategoryHomeClick = (categorySlug) => {
        router.push(`/kategori/${categorySlug}`);
        setSubDrawerOpen(false);
        setDrawerOpen(false);
    };

    const handleSubCategoryClick = (subCategorySlug) => {
        router.push(`/kategori/${subCategorySlug}`);
        setSubDrawerOpen(false);
        setDrawerOpen(false);
    };

    const handleBackToMain = () => {
        setSubDrawerOpen(false);
    };

    const handleClose = () => {
        setDrawerOpen(false);
        setSubDrawerOpen(false);
    };

    return (
        <>
            {isMobile && (
                <>
                    <IconButton
                        color="inherit"
                        aria-label="Open Categories"
                        onClick={() => setDrawerOpen(true)}
                        sx={{ margin: "0px", color: "#ffffff" }}
                    >
                        <MenuIcon />
                    </IconButton>

                    <Drawer
                        open={drawerOpen}
                        onClose={handleClose}
                        anchor="left"
                        sx={{
                            "& .MuiDrawer-paper": {
                                width: "100%",
                                backgroundColor: "#5899dd",
                            },
                        }}
                    >
                        <Box>
                            <Logo />
                        </Box>
                        <IconButton
                            onClick={handleClose}
                            sx={{ position: "absolute", right: 0, top: 0, color: "#ffffff" }}
                        >
                            <CloseIcon sx={{ fontSize: "44px" }} />
                        </IconButton>

                        <Box role="menubar" sx={{ padding: "16px" }}>
                            <Button
                                variant="text"
                                fullWidth
                                onClick={handleHomeClick}
                                sx={{
                                    color: "#ffffff",
                                    fontWeight: "bold",
                                    justifyContent: "flex-start",
                                    textTransform: "none",
                                    fontFamily: "Quicksand",
                                    fontSize: "16px",
                                }}
                            >
                                Ana Sayfa
                            </Button>
                            <Divider sx={{ my: 2, borderColor: "#ffffff" }} />

                            {user ? (
                                <Button
                                    variant="text"
                                    fullWidth
                                    onClick={handleProfileClick}
                                    sx={{
                                        color: "#ffffff",
                                        fontWeight: "bold",
                                        justifyContent: "flex-start",
                                        textTransform: "none",
                                        fontSize: "16px",
                                        fontFamily: "Quicksand",

                                    }}
                                >
                                    Profilim
                                </Button>
                            ) : (
                                <Button
                                    variant="text"
                                    fullWidth
                                    onClick={handleLoginClick}
                                    sx={{
                                        color: "#ffffff",
                                        fontWeight: "bold",
                                        justifyContent: "flex-start",
                                        textTransform: "none",
                                        fontSize: "16px",
                                        fontFamily: "Quicksand",
                                    }}
                                >
                                    Giriş Yap / Kayıt Ol
                                </Button>
                            )}

                            <Divider sx={{ my: 2, borderColor: "#ffffff" }} />

                            {loading ? (
                                <>
                                    <Skeleton variant="rectangular" width="100%" height={40} sx={{ my: 1 }} />
                                    <Skeleton variant="rectangular" width="100%" height={40} sx={{ my: 1 }} />
                                    <Skeleton variant="rectangular" width="100%" height={40} sx={{ my: 1 }} />
                                </>
                            ) : (
                                categories.map((category) => (
                                    <Box key={category.categoryId}>
                                        <Button
                                            variant="text"
                                            fullWidth
                                            onClick={() => handleCategoryClick(category)}
                                            sx={{
                                                color: "#ffffff",
                                                fontWeight: "bold",
                                                justifyContent: "space-between",
                                                textTransform: "none",
                                                fontSize: "16px",
                                                fontFamily: "Quicksand",
                                                my: 1,
                                            }}
                                        >
                                            {category.categoryName}
                                            <ArrowForwardIcon />
                                        </Button>
                                        <Divider sx={{ my: 1, borderColor: "#ffffff" }} />
                                    </Box>
                                ))
                            )}
                        </Box>
                    </Drawer>

                    <Drawer
                        open={subDrawerOpen}
                        onClose={() => setSubDrawerOpen(false)}
                        anchor="left"
                        sx={{
                            "& .MuiDrawer-paper": {
                                width: "100%",
                                backgroundColor: "#5899dd",
                            },
                        }}
                    >
                        <Box>
                            <Logo />
                        </Box>
                        <IconButton
                            onClick={handleClose}
                            sx={{ position: "absolute", right: 0, top: 0, color: "#ffffff" }}
                        >
                            <CloseIcon sx={{ fontSize: "44px" }} />
                        </IconButton>
                        <Box role="menubar" sx={{ padding: "16px" }}>
                            {/* Geri Butonu */}
                            <Button
                                variant="text"
                                fullWidth
                                onClick={handleBackToMain}
                                sx={{
                                    color: "#ffffff",
                                    fontWeight: "bold",
                                    justifyContent: "flex-start",
                                    textTransform: "none",
                                    fontSize: "16px",
                                    fontFamily: "Quicksand",
                                }}
                            >
                                <ArrowBackIcon sx={{ mr: 1 }} />
                                Geri
                            </Button>

                            <Divider sx={{ my: 2 }} />

                            {selectedCategory && (
                                <List>
                                    <ListItem
                                        component="div"
                                        onClick={() => handleCategoryHomeClick(selectedCategory.slug)}
                                        sx={{
                                            fontWeight: "bold",
                                            textTransform: "none",
                                            fontSize: "16px",
                                            fontFamily: "Quicksand",

                                            color: "#ffffff",
                                            cursor: "pointer",
                                            py: 1,
                                        }}
                                    >
                                        <ListItemText
                                            primary={`${selectedCategory.categoryName} Anasayfa`}
                                            primaryTypographyProps={{
                                                sx: {
                                                    fontSize: "16px",
                                                    fontWeight: "bold",
                                                    fontFamily: "Quicksand",

                                                },
                                            }}
                                        />
                                        <ArrowForwardIcon sx={{ color: "#ffffff" }} />
                                    </ListItem>

                                    <Divider sx={{ my: 1, borderColor: "#ffffff" }} />

                                    {selectedCategory.subCategories.map((subCategory) => (
                                        <Box key={subCategory.categoryId}>
                                            <ListItem
                                                component="div"
                                                onClick={() => handleSubCategoryClick(subCategory.slug)}
                                                sx={{
                                                    color: "#ffffff",
                                                    fontWeight: "bold",
                                                    textTransform: "none",
                                                    fontSize: "16px",
                                                    cursor: "pointer",
                                                    py: 1,
                                                    fontFamily: "Quicksand",
                                                }}
                                            >
                                                <ListItemText
                                                    primary={subCategory.categoryName}
                                                    primaryTypographyProps={{
                                                        sx: {
                                                            fontSize: "16px",
                                                            fontWeight: "bold",
                                                            fontFamily: "Quicksand",

                                                        },
                                                    }}
                                                />
                                                <ArrowForwardIcon sx={{ color: "#ffffff" }} />
                                            </ListItem>
                                            <Divider sx={{ my: 1, borderColor: "#ffffff" }} />
                                        </Box>
                                    ))}
                                </List>
                            )}
                        </Box>
                    </Drawer>
                </>
            )}
        </>
    );
};

export default MobileCategorySidebar;