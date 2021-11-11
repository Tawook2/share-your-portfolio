import axios from 'axios';

const getChatUserList = (id) => {
    return axios.get(`/chat/${id}`);
}

const getChatHistory = (senderId, receiverId) => {
    return axios.get(`/recallChat/${senderId}/${receiverId}`);
}

export default {
    getChatHistory,
    getChatUserList,
}