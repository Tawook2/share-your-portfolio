import axios from 'axios';

const uploadImage = (form) => {
    const config = {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    };
    return axios.post('/account/image', form, config);
}

const removeImage = () => {
    return axios.delete('/account/image');
}

export default {
    uploadImage,
    removeImage,
}