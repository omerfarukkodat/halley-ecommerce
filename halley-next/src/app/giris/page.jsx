"use client";

import React, {useContext, useEffect, useState} from 'react';
import AuthTabs from '../../components/auth/Auth';
import { useRouter } from "next/navigation";
import { Box, CircularProgress } from "@mui/material";
import {AuthContext} from "@/context/AuthContext";

export default function Login() {
    const router = useRouter();
    const [isTokenAvailable, setIsTokenAvailable] = useState(null);
    const {user} = useContext(AuthContext);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem("token");
            const isAuthenticated = token !== null;

            if (isAuthenticated) {
                router.push('/');
            } else {
                setIsTokenAvailable(false);
            }
        };
        checkAuth();
    }, [router]);

    if (isTokenAvailable === null) {
        return (
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                height="100vh"
            >
                <CircularProgress />
            </Box>
        );
    }

    return (
        <div>
            <AuthTabs type="login" />
        </div>
    );
}
