"use client";
import {createTheme} from '@mui/material/styles';
import {Quicksand} from 'next/font/google';


const ibmPlexMono = Quicksand({
    subsets: ['latin'],
    weight: [ '300', '400', '500', '700'],
});

const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#dc004e',
        },
    },
    typography: {
        fontFamily: ibmPlexMono.style.fontFamily,
        h1: {
            fontSize: '1rem',
            fontWeight: 700,
        },
        body1: {
            fontSize: '1rem',
            fontWeight: 600,
        },
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    fontFamily: ibmPlexMono.style.fontFamily,
                    fontWeight: 500,
                    backgroundColor: 'black',
                    color: 'white',
                    textTransform: 'none',
                },
            },
        },
        MuiTabs: {
            styleOverrides: {
                indicator: {
                    backgroundColor: 'black',
                },
            },
        },
        MuiTab: {
            styleOverrides: {
                root: {
                    '&.Mui-selected': {
                        color: 'black',
                        fontWeight: 'bold',
                        fontFamily: ibmPlexMono.style.fontFamily,
                    },
                },
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#0F3460',
                    },
                },
            },
        },
        MuiInputLabel: {
            styleOverrides: {
                root: {
                    '&.Mui-focused': {
                        color: 'black',
                        fontFamily: ibmPlexMono.style.fontFamily,
                    },
                },
            },
        },
    },

});



export default theme;