"use client";
import {useEffect, useState} from 'react';
import {Fab, useMediaQuery} from '@mui/material';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import theme from "@/theme";

const ScrollToTopButton = () => {
    const [isVisible, setIsVisible] = useState(false);
    const isXs = useMediaQuery(theme.breakpoints.down('sm'));


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

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth',
        });
    };

    return (
        <div style={{
            display: isVisible && !isXs ? 'block' : 'none',
            position: 'fixed',
            bottom: '20px',
            right: '20px',
            zIndex: 1000
        }}>
            <Fab sx={{backgroundColor: "#9F1127",
                width: 56,
                height: 56,
                color: "#ffffff",

                "&:hover": {
                    backgroundColor: "#9F1127",
                    color: "#ffffff"
                }
            }} size="small"
                 onClick={scrollToTop} aria-label="scroll back to top">
                <KeyboardArrowUpIcon sx={{fontSize: 44 }} />
            </Fab>
        </div>
    );
};

export default ScrollToTopButton;