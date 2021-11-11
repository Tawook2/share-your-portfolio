import axios from 'axios';

const login = ({ username, password }) => {
    const url = '/login';
    const data = {
        username,
        password,
    };
    return axios.post(url, data);
}

const logout = () => {
    const url = '/auth/logout';
    return axios.post(url);
}

const refreshToken = () => {
    const url = '/auth/refresh';
    return axios.get(url);
}

export default {
    login,
    logout,
    refreshToken,
}