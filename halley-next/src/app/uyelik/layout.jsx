"use client";

import React, {useEffect} from 'react';
import {
    Box,
    Typography,
    List,
    ListItemIcon,
    ListItemText,
    Divider,
    Avatar,
    ListItemButton,
    IconButton,
    useMediaQuery,
    CircularProgress
} from '@mui/material';
import { useRouter, usePathname } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import ImportContactsIcon from '@mui/icons-material/ImportContacts';
import PersonIcon from '@mui/icons-material/Person';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import LogoutIcon from '@mui/icons-material/Logout';



const UserLayout = ({ children }) => {
    const router = useRouter();
    const pathname = usePathname();
    const { user, profile , logout,fetchProfile } = useAuth();
    const isMobile = useMediaQuery('(max-width: 768px)');


    useEffect(() => {
        if (typeof window === "undefined") return;

        const storedToken = localStorage.getItem("token");

        if (!storedToken) {
            router.push("/giris");
        } else {
            fetchProfile();
        }
    }, []);




    const menuItems = [
        { text: 'Adreslerim', icon: <ImportContactsIcon sx={{color:"#0F3460"}} />, path: '/uyelik/adreslerim' },
        { text: 'Siparişlerim', icon: <ShoppingCartIcon sx={{color:"#0F3460"}} />, path: '/uyelik/siparislerim' },
        { text: 'Profilim', icon: <PersonIcon sx={{color:"#0F3460"}} />, path: '/uyelik/profilim' },
    ];

    const handleMenuItemClick = (path) => {
        router.push(path);
    };

    const handleBackToMember = () => {
        router.push('/uyelik');
    };

    if (!user) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress size={60} thickness={4} sx={{ color: "#0F3460" }} />
            </Box>
        );
    }

    // mobile
    if (isMobile) {
        return (
            <Box sx={{ display: 'flex', flexDirection: 'column',  }}>
                {pathname !== '/uyelik' && (
                    <Box
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            p: 2,
                            backgroundColor: '#ffffff',
                            borderBottom: '1px solid #e0e0e0',
                            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                            transition: 'all 0.3s ease',
                            '&:hover': {
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)',
                            },
                        }}
                    >
                        <IconButton
                            onClick={handleBackToMember}
                            sx={{
                                color: '#0F3460',
                                '&:hover': {
                                    backgroundColor: 'rgba(15, 52, 96, 0.1)',
                                },
                            }}
                        >
                            <ArrowBackIcon sx={{ fontSize: 28, fontWeight: 700 }} />
                        </IconButton>

                        <Typography
                            onClick={handleBackToMember}
                            sx={{
                                fontSize: 24,
                                fontWeight: 600,
                                color: '#0F3460',
                                cursor: 'pointer',
                                ml: 1,
                                '&:hover': {
                                    textDecoration: 'underline',
                                },
                            }}
                        >
                            Hesabım
                        </Typography>
                    </Box>
                )}

                <Box sx={{ flexGrow: 1, p: 2 }}>
                    {pathname === '/uyelik' ? (
                        <Box sx={{ textAlign: 'center' }}>
                            <Avatar sx={{
                                width: 80, backgroundColor:"#0F3460", height: 80,fontSize: 48 , margin: 'auto', mb: 2
                            }}>
                                {profile?.firstName?.charAt(0)}
                            </Avatar>
                            <Typography sx={{fontWeight:600}} variant="h5">
                                {profile?.firstName} {profile?.lastName}</Typography>
                            <Typography sx={{fontWeight:600}} variant="body1" color="textSecondary">
                                {profile?.email}</Typography>

                            <List sx={{ mt: 3 }}>
                                {menuItems.map((item) => (
                                    <ListItemButton
                                        key={item.text}
                                        onClick={() => handleMenuItemClick(item.path)}
                                        sx={{ py: 2, borderBottom: '1px solid #ddd' }}
                                    >
                                        <ListItemIcon>{item.icon}</ListItemIcon>
                                        <ListItemText primary={item.text} />
                                        <ArrowForwardIcon/>
                                    </ListItemButton>

                                ))}
                                <ListItemButton
                                    onClick={logout}
                                    sx={{ py: 2, borderBottom: '1px solid #ddd' }}
                                >
                                    <ListItemIcon>
                                        <LogoutIcon sx={{ color: "#0F3460" }} />
                                    </ListItemIcon>
                                    <ListItemText primary="Çıkış Yap" />
                                    <ArrowForwardIcon/>
                                </ListItemButton>
                            </List>

                        </Box>
                    ) : (
                        children
                    )}
                </Box>
            </Box>
        );
    }

    // Desktop
    return (
        <Box sx={{ display: 'flex', minHeight: '100vh',}}>

            <Box sx={{ width: 250, bgcolor: 'background.paper', boxShadow: 3 }}>
                <Box sx={{ p: 2, textAlign: 'center' }}>
                    <Avatar sx={{
                        width: 80,
                        backgroundColor:"#0F3460",
                        height: 80,
                        margin: 'auto',
                        mb: 2 ,
                        fontSize: 48 ,
                        justifyContent: 'center'
                    }}>
                        {profile?.firstName?.charAt(0)}
                    </Avatar>
                    <Typography variant="h6" sx={{ mt: 2 }}>
                        {profile?.firstName} {profile?.lastName}
                    </Typography>
                    <Typography
                        sx={{fontWeight:600}} variant="body1" color="textSecondary">{profile?.email}
                    </Typography>

                </Box>
                <Divider />
                <List>
                    {menuItems.map((item) => (
                        <ListItemButton
                            key={item.text}
                            onClick={() => handleMenuItemClick(item.path)}
                            sx={{
                                py: 2, borderBottom: '1px solid #ddd',
                                '&:hover': { backgroundColor: 'action.hover'},
                            }}
                        >

                            <ListItemIcon>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                            <ArrowForwardIcon/>
                        </ListItemButton>
                    ))}
                    <ListItemButton
                        onClick={logout}
                        sx={{ py: 2, borderBottom: '1px solid #ddd' }}
                    >
                        <ListItemIcon>
                            <LogoutIcon sx={{ color: "#0F3460" }} />
                        </ListItemIcon>
                        <ListItemText primary="Çıkış Yap" />
                        <ArrowForwardIcon/>
                    </ListItemButton>
                </List>
            </Box>

            <Box sx={{ flexGrow: 1, p: 3 }}>
                {children}
            </Box>
        </Box>
    );
};

export default UserLayout;