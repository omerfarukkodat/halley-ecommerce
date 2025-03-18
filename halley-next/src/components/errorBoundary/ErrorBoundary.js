import React, { Component } from "react";
import { Box, Typography, Button } from "@mui/material";

class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }

    static getDerivedStateFromError() {
        return { hasError: true };
    }

    componentDidCatch(error, errorInfo) {
        console.error("Hata oluştu:", error, errorInfo);
    }

    render() {
        if (this.state.hasError) {
            return (
                <Box textAlign="center" p={4}>
                    <Typography variant="h6" color="error">
                        Bir hata oluştu. Lütfen tekrar deneyin.
                    </Typography>
                    <Button variant="contained" onClick={() => window.location.reload()}>
                        Sayfayı Yenile
                    </Button>
                </Box>
            );
        }

        return this.props.children;
    }
}

export default ErrorBoundary;
