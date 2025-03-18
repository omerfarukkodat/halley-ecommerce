"use client";
import React, { useState } from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Divider,
    Drawer,
    IconButton,
    List,
    ListItemButton,
    ListItemText,
    Typography,
    useMediaQuery,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import CloseIcon from "@mui/icons-material/Close";
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';


const CategoryFilter = ({ selectedCategory, onSelect, categories, loading }) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
    const [currentParent, setCurrentParent] = useState(null);
    const [isDrawerOpen, setIsDrawerOpen] = useState(false);

    const currentCategories = currentParent ? currentParent.subCategories || [] : categories;

    const findCategoryName = (categories, categoryId) => {
        for (let cat of categories) {
            if (cat.categoryId === categoryId) return cat.categoryName;
            if (cat.subCategories?.length > 0) {
                const result = findCategoryName(cat.subCategories, categoryId);
                if (result) return result;
            }
        }
        return "";
    };

    const renderCategoryList = () => {
        if (loading) {
            return (
                <Box textAlign="center" py={2}>
                    <CircularProgress size={24} />
                </Box>
            );
        }

        return currentCategories.map((cat, index) => (
            <React.Fragment key={cat.categoryId}>
                <ListItemButton
                    selected={selectedCategory === cat.categoryId}
                    onClick={() => {
                        if (cat.subCategories?.length > 0) {
                            setCurrentParent(cat);
                        } else {
                            onSelect(cat.categoryId);
                            if (isMobile) setIsDrawerOpen(false);
                        }
                    }}
                    sx={{
                        borderBottom: "1px solid #e0e0e0",
                        '&:last-child': {
                            borderBottom: "none",
                        },
                    }}
                >
                    <ListItemText primary={cat.categoryName} />
                    <KeyboardArrowRightIcon
                        className="arrow-icon"
                        sx={{
                            fontSize: 18,
                            color: "#0F3460",
                            ml: 1
                        }}
                    />
                </ListItemButton>
            </React.Fragment>
        ));
    };

    const renderHeader = () => {
        if (!currentParent) return null;
        return (
            <Box mb={2}>
                <Button onClick={() => setCurrentParent(null)} sx={{
                    backgroundColor:"#5899dd", textTransform: "none", mb: 1 }}>
                    ← Geri
                </Button>
                <Typography textAlign={"center"} fontWeight={600} variant="subtitle1"
                sx={{
                    borderBottom: "1px solid #ddd",
                }}
                >

                    {currentParent.categoryName}</Typography>
                <Button
                    fullWidth
                    onClick={() => {
                        onSelect(currentParent.categoryId);
                        setCurrentParent(null);
                        if (isMobile) setIsDrawerOpen(false);
                    }}
                    sx={{ textTransform: "none", mt: 1, backgroundColor: "white", fontWeight:600,
                        color: "#000000", }}
                    variant="outlined"
                >
                    {currentParent.categoryName} (Hepsi)
                </Button>
            </Box>
        );
    };

    if (isMobile) {
        return (
            <>
                <Button
                    variant="outlined"
                    fullWidth
                    onClick={() => setIsDrawerOpen(true)}
                    sx={{ textTransform: "none", mb: 2 , backgroundColor:"#5899dd" }}
                >
                    {selectedCategory
                        ? `Seçili: ${findCategoryName(categories, selectedCategory) || ""}`
                        : "Kategori Seç"}
                </Button>
                <Drawer
                    anchor="left"
                    open={isDrawerOpen}
                    onClose={() => setIsDrawerOpen(false)}
                    variant="temporary"
                >
                    <Box p={2} width={300} position="relative">
                        <IconButton
                            onClick={() => setIsDrawerOpen(false)}
                            sx={{
                                position: "absolute",
                                right: 8,
                                top: 8,
                                zIndex: 1,
                                color: "black",
                            }}
                        >
                            <CloseIcon sx={{fontSize: "32px"}} />
                        </IconButton>

                        <Typography variant="h6" mb={2} fontWeight="bolder" >
                            Kategori Seç
                        </Typography>
                        <Divider/>
                        {renderHeader()}
                        <List>{renderCategoryList()}</List>
                        {selectedCategory && (
                            <Button
                                fullWidth
                                onClick={() => {
                                    onSelect(null);
                                    setIsDrawerOpen(false);
                                }}
                                sx={{ mt: 2, textTransform: "none",backgroundColor: "#5899dd" }}
                                variant="outlined"
                            >
                                Temizle
                            </Button>
                        )}
                    </Box>
                </Drawer>
            </>
        );
    }

    return (
        <Card variant="outlined" sx={{ borderRadius: 2, mb: 2 }}>
            <CardContent>
                <Typography fontWeight="600" variant="subtitle1" gutterBottom>
                    Kategoriler
                </Typography>
                <Divider sx={{ mt: 1 }} />
                {renderHeader()}
                <List disablePadding>{renderCategoryList()}</List>
                {selectedCategory && (
                    <Box textAlign="right" mt={1}>
                        <Button
                            onClick={() => onSelect(null)}
                            sx={{ textTransform: "none" }}
                            variant="outlined"
                        >
                            Temizle
                        </Button>
                    </Box>
                )}
            </CardContent>
        </Card>
    );
};

export default CategoryFilter;