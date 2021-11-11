<template>
    <v-app-bar
        app
        color="white"
        class="px-4 py-1"
        height="70"
    >
        <v-container class="d-flex justify-space-between align-center">
            <logo-image
                :width="150"
            ></logo-image>
            <v-spacer></v-spacer>
            <div v-if="!isAuth">
                <router-link to="/login">
                    <v-btn
                        text
                    >
                        로그인
                    </v-btn>
                </router-link>
                <router-link to="/signup">
                    <v-btn
                        text
                    >
                        회원가입
                    </v-btn>
                </router-link>
            </div>
            <div v-else>
                <v-menu bottom left rounded offset-y>
                    <template v-slot:activator="{ on }">
                        <v-btn icon v-on="on">
                            <user-image :id="id"></user-image>    
                        </v-btn>
                    </template>
                    <v-card class="pa-1" width="130">
                        <router-link to="/account">
                            <v-btn text block>
                                계정관리
                            </v-btn>
                        </router-link>
                            
                        <v-divider></v-divider>
                        
                        <router-link to="/message">
                            <v-btn text block>
                                채팅
                            </v-btn>                        
                        </router-link>

                        <v-divider></v-divider>
                        
                        <v-btn text block @click="clickLogout">
                            로그아웃
                        </v-btn>
                    </v-card>
                    

                </v-menu>
            </div>
        </v-container>
    </v-app-bar>
</template>

<script>
import LogoImage from '@/components/LogoImage.vue';
import UserImage from '@/components/UserImage.vue';
import { mapState, mapGetters, mapActions } from 'vuex';

export default {
    name: 'AppHeader',
    components: {
        LogoImage,
        UserImage,
    },
    computed: {
        ...mapState({
            id: state => state.user.user.id,
        }),
        ...mapGetters({
            isAuth: 'user/isAuth',
        }),
    },
    methods: {
        ...mapActions({
            logout: 'user/logout',
        }),
        clickLogout() {
            this.logout()
                .then(() => {
                    alert('로그아웃 성공');
                });
        }
    }
}
</script>
