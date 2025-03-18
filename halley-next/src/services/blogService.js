import globalAxiosInstance from "@/api/globalAxiosInstance";
import axiosInstance from "@/services/axiosInstance";


const addBlogPost = (blogPostDto) => {
    return globalAxiosInstance.post('/blog',blogPostDto).then((response) => response.data)
        .catch((error) => {
            console.log('Error fetching data', error);
            throw error;
        });

};


const getBlogPostBySlug = (slug) => {
    return axiosInstance.get(`/blog/${slug}`).then((response) => response.data)
        .catch((error) => {
            console.log('Error fetching data', error);
            throw error;
        });

};

const getAll = (filters) => {
    return axiosInstance.get(`/blog`,{params:{...filters}})
        .then((response) => response.data)
        .catch((error) => {
            console.log('Error fetching data', error);
            throw error;
        });

};

const getLatestBlogPosts = (size) => {
    return axiosInstance.get(`/blog/latest/${size}`)
        .then((response) => response.data)
        .catch((error) => {
            console.log('Error fetching data', error);
            throw error;
        });

};






export const blogService = {
    getBlogPostBySlug,
    addBlogPost,
    getAll,
    getLatestBlogPosts,


};

export default blogService;