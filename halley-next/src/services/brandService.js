import apiClient from "@/services/axiosInstance";



export const getAllBrands = (params) => {
    return apiClient.get(`/brands`);
};




const brandService = {

    getAllBrands,
};

export default brandService;