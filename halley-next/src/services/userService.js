
import userAxiosInstance from "@/services/userAxiosInstance";

const getProfile = () => {
    return userAxiosInstance.get(`/users/profile`,)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
};

const updateProfile = (profileDto) => {
    return userAxiosInstance.put(`/users/profile`,profileDto)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
};

const resetPassword = (token,passwordResetDto) => {
    return userAxiosInstance.post(`/users/reset-password?token=${token}`,passwordResetDto)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
}

const resetPasswordRequest = (email) => {
    return userAxiosInstance.post(`/users/reset-password/request?email=${email}`)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
}


const validateToken = (token) => {
    return userAxiosInstance.get(`/users/validate-token?token=${token}`)
        .then(response => {
            return response;
        }).catch(error => {
            throw error;
        })
}



const userService = {
    getProfile,
    updateProfile,
    resetPassword,
    resetPasswordRequest,
    validateToken
}

export default userService;
