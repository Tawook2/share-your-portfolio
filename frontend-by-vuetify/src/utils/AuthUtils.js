import jwtDecode from 'jwt-decode';

const tokenName = 'token';

const saveToken = (token) => {
    localStorage.setItem(tokenName, token);
}

const getToken = () => {
    const token = localStorage.getItem(tokenName);
    return token;
}

const removeToken = () => {
    localStorage.removeItem(tokenName);
}

const getUserInfo = () => {
    const token = getToken();
    if (!token) {
        return null;
    }

    try {
        const user = jwtDecode(token);
        const exp = user.exp;
        if (Date.now() >= exp * 1000) {
            user.exp = 0;
        }
        return user;
    } catch {
        return null;
    }
}

export default {
    saveToken,
    getToken,
    removeToken,
    getUserInfo
}