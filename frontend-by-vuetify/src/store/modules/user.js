import AuthApi from "../../api/AuthApi";
import AuthUtils from "../../utils/AuthUtils";

const state = {
    user: null,
}

const getters = {
    isAuth(state) {
        return !!state.user;
    }
}

const mutations = {
    setUser(state, user) {
        state.user = user;
    },
    resetUser(state) {
        state.user = null;
    },
}

const actions = {
    login(context, { email, password }) {
        const username = email;
        return AuthApi.login({ username, password })
            .then(response => {
                const token = response.data.token;
                AuthUtils.saveToken(token);

                const user = AuthUtils.getUserInfo();
                context.commit('setUser', user);
                return response;
            })
            .catch(error => {
                context.commit('resetUser');
                AuthUtils.removeToken();
                throw error;
            });
    },
    logout(context) {
        return AuthApi.logout()
            .then(response => {
                context.commit('resetUser');
                AuthUtils.removeToken();
                return response;
            });
    },
    refreshToken(context) {
        return AuthApi.refreshToken()
            .then(response => {
                const token = response.data.token;
                AuthUtils.saveToken(token);

                const user = AuthUtils.getUserInfo();
                context.commit('setUser', user);
                return response;
            })
            .catch(error => {
                context.commit('resetUser');
                AuthUtils.removeToken();
                throw error;
            });
    },
    async init(context) {
        const user = AuthUtils.getUserInfo();
        if (!user) {
            return;
        } else if (user.exp === 0) {
            await this.dispatch('user/refreshToken');
        } else {
            context.commit('setUser', user);
            return;
        }
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}