import axios from "axios";

const userAxiosInstance = axios.create({
    baseURL: "http://localhost:8088/api/v1",
});

userAxiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default userAxiosInstance;



userAxiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const errorMessage = error.response.data.error || error.response.data.message || "Bir hata oluştu. Lütfen tekrar deneyin.";
            return Promise.reject(new Error(errorMessage));
        } else if (error.request) {
            return Promise.reject(new Error("Sunucuya bağlanılamadı. Lütfen internet bağlantınızı kontrol edin."));
        } else {
            return Promise.reject(new Error("Bir hata oluştu. Lütfen tekrar deneyin."));
        }
    }
);

