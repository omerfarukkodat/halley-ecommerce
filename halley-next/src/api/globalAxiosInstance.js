import axios from "axios";
import authHeader from "@/services/authHeader";



const axiosInstance = axios.create({
    baseURL: "http://localhost:8088/api/v1",
    withCredentials: true,
});

axiosInstance.interceptors.request.use(config => {
    const headers = authHeader();
    if (headers.Authorization){
        config.headers = {...config.headers, ...headers };
    }
    return config;
},
    (error) => Promise.reject(error)
    );

//For error handling

axiosInstance.interceptors.response.use(
    (response) =>response,
    (error) => {
        if (error.response.status === 403){
            localStorage.removeItem("token");
            window.location.href = "/giris";
        }
        return Promise.reject(error);
    }
);


export default axiosInstance;