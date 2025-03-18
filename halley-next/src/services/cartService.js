import axios from "axios";
import authHeader from "@/services/authHeader";
import globalAxiosInstance from "@/api/globalAxiosInstance";

const apiClient = axios.create({
    baseURL: 'http://localhost:8088/api/v1/cart',
    withCredentials: true,
});

export const getCart = () => {
    const headers = authHeader();
    return globalAxiosInstance.get('/cart', {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error fetching data', error);
            throw error;
        });
};

export const addCart = (productId) => {
    const headers = authHeader();
    return globalAxiosInstance.post(`/cart/${productId}`, {}, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error adding product to cart', error);
            throw error;
        });
};

export const clearCart = () => {
    const headers = authHeader();
    return globalAxiosInstance.delete('/cart/clear', {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error clearing cart', error);
            throw error;
        });
};

export const removeCartItem = (productId) => {
    const headers = authHeader();
    return globalAxiosInstance.delete(`/cart/${productId}`, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error removing product from cart', error);
            throw error;
        });
};

export const removeSelectedCartItems = (productIds) => {
    const headers = authHeader();
    return globalAxiosInstance.delete('/cart/remove', {
        headers: Object.keys(headers).length ? headers : undefined,
        data: productIds,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error removing selected products from cart', error);
            throw error;
        });
};

export const decreaseProductQuantity = (productId) => {
    const headers = authHeader();
    return globalAxiosInstance.patch(`/cart/${productId}/decrease`, {}, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error decreasing product quantity', error);
            throw error;
        });
};

export const increaseProductQuantity = (productId) => {
    const headers = authHeader();
    return globalAxiosInstance.patch(`/cart/${productId}/increase`, {}, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error increasing product quantity', error);
            throw error;
        });
};

export const isCartEmpty = () => {
    const headers = authHeader();
    return globalAxiosInstance.get('/cart/isEmpty', {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error checking if cart is empty', error);
            throw error;
        });
};

export const getProductQuantity = (productId) => {
    const headers = authHeader();
    return apiClient.get(`/getProductQuantity/${productId}`, {}, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting product quantity', error);
            throw error;
        });
};

export const getCartSummary = () => {
    const headers = authHeader();
    return globalAxiosInstance.get('/cart/cartSummary', {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting cart summary', error);
            throw error;
        });
};
