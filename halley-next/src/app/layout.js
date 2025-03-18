
import Navbar from '../components/navbar/Navbar';
import FooterMenu from "@/components/footer/FooterMenu";
import { Box, ThemeProvider } from '@mui/material';
import { CartProvider } from "@/context/CartContext";
import { AuthProvider } from "@/context/AuthContext";
import theme from '../theme';
import {CategoryProvider} from "@/context/CategoryContext";
import ScrollToTopButton from "@/components/home/ScrollToTopButton";
import WhatsappButton from "@/components/home/WhatsappButton";
import ScrollPositionHandler from "@/components/home/ScrollPositionHandler";

export const metadata = {
    title: "Halleywall",
    description: "This is my awesome Next.js app",
};

export default function RootLayout({ children }) {

    return (
        <html lang="en">
        <head>
            <title>{metadata.title}</title>
            <meta name="description" content={metadata.description} />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        </head>
        <body style={{ margin: 0 }}>
        <ThemeProvider theme={theme}>
            <AuthProvider>
                <CartProvider>
                    <CategoryProvider>
                    <Box
                        sx={{
                            mb: { xs: "64px", sm: "65px", md: "65px", lg: "0px", xl: "0px" }
                        }}
                    >
                        <Navbar />
                    </Box>
                    <Box
                        sx={{
                            minHeight: "800px",
                            mt: { xs: "0px", sm: "0px", md: "0px", lg: "0px", xl: "0px" }
                        }}
                    >
                        {children}
                        <ScrollToTopButton/>
                        <WhatsappButton/>
                        <ScrollPositionHandler />

                    </Box>
                        <FooterMenu />
                    </CategoryProvider>
                </CartProvider>
            </AuthProvider>
        </ThemeProvider>
        </body>
        </html>
    );
}