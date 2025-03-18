import apiClient from "@/services/axiosInstance";




const getAllProducts = (params) => {

    return apiClient.get(`/products`, {params:{ ...params }});
};

export const getProductById = (slug,productId) => {
    return apiClient.get(`/products/${slug}-${productId}`);
};

const addProduct = (productData) => {
    return apiClient.post(`/products`, productData, {
        headers: {Authorization: 'Bearer ' + localStorage.getItem('token')}
    });
};

const updateProduct = (productId,productData) => {
    return apiClient.put(`/products/${productId}`,productData, {
        headers: {Authorization: 'Bearer ' + localStorage.getItem('token')}
    });
};

const deleteProductById = (productId) => {
    return apiClient.delete(`/products/${productId}`,{
        headers: {Authorization: 'Bearer ' + localStorage.getItem('token')}
    });
};

const getProductsByCategoryId = (slug, filters) => {
    return apiClient.get(`/products/category/${slug}`, {params: {...filters}});
};


//search
const getProductsBySearch = (searchTerm , params) => {
    return apiClient.get(`/products/search`, {params: {searchTerm, ...params } });
};

const filterProducts = (filters , params) => {
    return apiClient.get(`/products/filter`, {params: {...filters, ...params}});
};

const getSimilarProducts = (productId , params ) => {
    return apiClient.get(`/products/similar/${productId}`, { params });
};

const getFeaturedProducts = (filters) => {
    return apiClient.get(`/products/featured`, {params: { ...filters }});
};

const getDiscountedProducts = (filters) => {
    return apiClient.get(`/products/discounted`, {params: {...filters}});
};

export const getCategoryPaths = (productId, categoryId) => {
    return apiClient.get(`/products/${productId}/category-paths`, {
        params: { ...categoryId }
    });
};


 const getProductsByBrand = (brand , params ) => {
    return apiClient.get(`/products/brands/${brand}` , {params: {...params}});
}

const getLastAddedProducts = (params) => {
    return apiClient.get(`/products/lastAdded`, {params: {...params}});
}

const getByColourSlug = (slug,filters) => {
     return apiClient.get(`/products/colour/${slug}`, {params: {...filters}})
}

const getByDesignSlug = (slug,filters) => {
     return apiClient.get(`/products/design/${slug}`,{params: {...filters}});
}

const getByRoomSlug = (slug,filters) => {
     return apiClient.get(`/products/room/${slug}`, {params: {...filters}});
}









export const productService = {
    getAllProducts,
    getProductById,
    addProduct,
    updateProduct,
    deleteProductById,
    getProductsByCategoryId,
    getProductsBySearch,
    filterProducts,
    getSimilarProducts,
    getFeaturedProducts,
    getDiscountedProducts,
    getCategoryPaths,
    getProductsByBrand,
    getLastAddedProducts,
    getByColourSlug,
    getByDesignSlug,
    getByRoomSlug,

};

export default productService;