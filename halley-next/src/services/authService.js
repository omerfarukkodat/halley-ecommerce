import globalAxiosInstance from "@/api/globalAxiosInstance";




const login = (loginData) => {
    return globalAxiosInstance.post(`/auth/login`, loginData)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        });
};

const register = (registerData) => {
    return globalAxiosInstance.post(`/auth/register`, registerData)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        });
};


const isAdmin = () => {
    return globalAxiosInstance.get(`/auth/admin/check`)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        });
};


const adminLogin = (loginData) => {
    return globalAxiosInstance.post(`/auth/admin/login`, loginData)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        });
};





const authService = {
    login,
    register,
    isAdmin,
    adminLogin
}

export default authService;


