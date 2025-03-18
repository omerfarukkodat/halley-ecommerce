import { Box, createTheme, IconButton, InputBase, ThemeProvider, Skeleton, Snackbar } from "@mui/material";
import { Search as SearchIcon } from "@mui/icons-material";
import React, { useState, useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { styled } from "@mui/system";

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

const Search = styled("div")(({ theme }) => ({
    position: "flex",
    borderRadius: "20px",
    border: "1px solid black",
    backgroundColor: theme.palette.common.white,
    "&:hover": {
        backgroundColor: theme.palette.grey[200],
    },
    width: "100%",
}));

const SearchBar = () => {
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const router = useRouter();
    const searchParams = useSearchParams();

    useEffect(() => {
        setTimeout(() => setLoading(false));
        setSearchTerm(searchParams.get("term") || "");
    }, [searchParams]);

    const handleSearch = () => {
        const trimmedTerm = searchTerm.trim();
        if (!trimmedTerm) {
            setSnackbarMessage("Lütfen geçerli bir arama terimi girin.");
            setSnackbarOpen(true);
            return;
        }

        const sanitizedTerm = trimmedTerm.replace(/[^a-zA-Z0-9\s]/g, "");
        if (!sanitizedTerm) {
            setSnackbarMessage("Geçersiz arama terimi.");
            setSnackbarOpen(true);
            return;
        }

        router.push(`/search?term=${encodeURIComponent(trimmedTerm)}`);
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleCloseSnackbar = () => {
        setSnackbarOpen(false);
    };

    return (
        <ThemeProvider theme={theme}>
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    width: "100%",
                    padding: theme.spacing(1),
                    marginLeft: { xs: 0, sm: 0, md: 0, lg: 24, xl: 24 },
                }}
            >
                <Search
                    sx={{
                        width: { xs: "100%", sm: "100%", md: "80%", lg: "70%", xl: "50%" },
                        display: "flex",
                        alignItems: "center",
                        borderColor: "#ffffff",
                        backgroundColor: "#5899dd",
                        '&:hover': {
                            backgroundColor: "#5899dd",
                        },
                    }}
                >
                    <InputBase
                        placeholder=" Ürün ara..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyDown={handleKeyDown}
                        sx={{
                            padding: theme.spacing(1),
                            ml: 3,
                            flexGrow: 1,
                            fontWeight: 500,
                            fontFamily: "Quicksand",
                            color: "#ffffff",
                            '& input::placeholder': {
                                color: '#ffffff',
                                opacity: 1,
                            },
                            fontSize: {
                                xs: "16px",
                                sm: "16px",
                                md: "18px",
                                lg: "18px",
                                xl: "19px"
                            },
                        }}
                    />
                    <IconButton
                        onClick={handleSearch}
                        sx={{
                            padding: theme.spacing(1),
                            color: '#ffffff',
                        }}
                    >
                        <SearchIcon />
                    </IconButton>
                </Search>
            </Box>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                onClose={handleCloseSnackbar}
                message={snackbarMessage}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            />
        </ThemeProvider>
    );
};

export default SearchBar;