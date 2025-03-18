"use client";

import {createContext, useContext, useState, useEffect} from "react";
import authService from "@/services/authService";
import {useRouter} from "next/navigation";
import userService from "@/services/userService";

export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const [profile, setProfile] = useState([]);
    const [isAdminUser, setIsAdminUser] = useState(false);



    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            setUser({token});
        } else {
            setUser(null);
        }
        setLoading(false);
    }, []);

    const login = async (loginData) => {
        try {
            const response = await authService.login(loginData);
            setUser({token: response.data.token});
            localStorage.setItem("token", response.data.token);
            router.push("/");
            return {success: true};
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.error || error.response?.data?.message ||
                    "Bir hata oluştu.Lütfen tekrar deneyin."
            }
        }
    };

    const register = async (registerData) => {
        try {
            await authService.register(registerData);
            router.push("/giris");
            return {success: true};
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.error || error.response?.data?.message ||
                    "Bir hata oluştu. Lütfen tekrar deneyin."
            };

        }
    };

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
        router.push("/giris");
    };

    const fetchProfile = async () => {
        try {
            const response = await userService.getProfile();
            setProfile(response.data);
            setLoading(false);
        } catch (error) {
            console.error("Profil bilgileri alınırken hata oluştu:", error);
            setLoading(false);
        }
    };

    const updateProfile = async (profileDto) => {
        try {
            const response = await userService.updateProfile(profileDto);
            setProfile(response.data);
            return response;
        } catch (error) {
            throw error;
        }
    };

    const checkAdminStatus = async () => {
        try {
            const response = await authService.isAdmin();
            if (response.status === 200) {
                setIsAdminUser(true);
            }
        }catch (error) {
            setIsAdminUser(false);
        }
    }

    const adminLogin = async (loginData) => {
        try {
            const response = await authService.adminLogin(loginData);
            setUser({ token: response.data.token });
            localStorage.setItem("token", response.data.token);
            setIsAdminUser(true);
            return { success: true };
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.error || error.response?.data?.message ||
                    "Bir hata oluştu. Lütfen tekrar deneyin.",
            };
        }
    };

    return (
        <AuthContext.Provider value={{user, login, register,adminLogin, logout,checkAdminStatus,
            loading, profile, fetchProfile, updateProfile,isAdminUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};
