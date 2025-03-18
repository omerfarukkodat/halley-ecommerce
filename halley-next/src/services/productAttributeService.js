import apiClient from "@/services/axiosInstance";



const getAllColour = () => {
    return apiClient.get(`/colours`)
}

const getColourBySlug = (slug) => {
    return apiClient.get(`/colours/slug/${slug}`)
}


const getAllDesign = () => {
    return apiClient.get(`/designs`)
}

const getDesignBySlug = (slug) => {
    return apiClient.get(`/designs/slug/${slug}`)
}

const getAllRoom = () => {
    return apiClient.get(`/rooms`)
}

const getRoomBySlug = (slug) => {
    return apiClient.get(`/rooms/slug/${slug}`)
}



export const productAttributeService = {
    getAllColour,
    getAllDesign,
    getAllRoom,
    getColourBySlug,
    getDesignBySlug,
    getRoomBySlug,
}