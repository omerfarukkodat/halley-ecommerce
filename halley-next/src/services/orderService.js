import authHeader from "@/services/authHeader";
import axiosInstance from "@/services/axiosInstance";



export const createOrderFromCart = (orderDto) => {
    const headers = authHeader();
    return axiosInstance.post('/order' , orderDto, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
    .catch((error) => {
        console.log('Error creating order', error);
        throw error;
    });
};

export const getOrderSummary = (orderId) => {
    const headers = authHeader();
    return axiosInstance.get(`/order/orderSummary/${orderId}`, {
        headers: Object.keys(headers).length ? headers : undefined,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting order', error);
            throw error;
        });
};

export const getAllOrders = () => {
    const headers = authHeader();
    return axiosInstance.get(`/order`, {
        headers: headers,
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting order', error);
            throw error;
        });
};


export const getOrderById = (orderId) => {
    const headers = authHeader();
    return axiosInstance.get(`/order/${orderId}`, {
        headers: headers
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting order', error);
            throw error;
        });
};