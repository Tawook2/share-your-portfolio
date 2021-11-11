import axios from 'axios';
import AuthUtils from '../utils/AuthUtils';

const tokenHeader = 'Authorization';

axios.defaults.baseURL = '/api';
axios.defaults.headers['Content-Type'] = 'application/json';
axios.defaults.headers['Accepts'] = 'application/json';

axios.interceptors.request.use(config => {
    const token = AuthUtils.getToken();
    if (token) {
        config.headers[tokenHeader] = token;
    }
    return config;
});

