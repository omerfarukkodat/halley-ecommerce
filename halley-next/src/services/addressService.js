import authHeader from "@/services/authHeader";
import apiClient from "@/services/axiosInstance";
import globalAxiosInstance from "@/api/globalAxiosInstance";




export const getAddressById = (addressId) => {
    const headers = authHeader();
    return apiClient.get(`/addresses/${addressId}`, {
        headers
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting address', error);
            throw error;
        });
};

export const getAllAddresses = () => {
    const headers = authHeader();
    return globalAxiosInstance.get('/addresses', {
        headers
    }).then((response) => response.data)
        .catch((error) => {
            console.log('Error getting addresses', error);
            throw error;
        });
};


export const createAddress = (addressDto) => {
    const headers = authHeader();
    return globalAxiosInstance.post('/addresses' , addressDto , {
        headers
    }).then(response => response.data)
        .catch((error) => {
            console.log('Error creating addresse', error);
            throw error;
        });
};


export const updateAddressById = (addressId, addressDto) => {
    const headers = authHeader();
    return apiClient.put(`/addresses/${addressId}`, addressDto, {
        headers
    }).then(response => response.data)
        .catch((error) => {
            console.log('Error updating address', error);
            throw error;
        });
};


export const deleteAddressById = (addressId) => {
    const headers = authHeader();
    return apiClient.delete(`/addresses/${addressId}`, {
        headers
    }).then(response => response.data)
    .catch((error) => {
        console.log('Error deleting address', error);
        throw error;
    });
};






