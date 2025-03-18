import apiClient from "@/services/axiosInstance";

export const getCategories = () => {
    return apiClient.get(`/categories/tree`);
};

const getMainCategories = () => {
    return apiClient.get(`/categories/getMainCategories`);
}

export const getCategory = (categorySlug) => {
    return apiClient.get(`/categories/${categorySlug}`);
}

const getCategoryPaths = (categorySlug) => {
    return apiClient.get(`/categories/category-paths/${categorySlug}`);
}




const categoryService = {
    getCategories,
    getMainCategories,
    getCategory,
    getCategoryPaths
}

export default categoryService;