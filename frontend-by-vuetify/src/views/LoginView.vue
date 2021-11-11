<template>
    <center-card>
        <template v-slot:content>
            <v-form width="600">
                <v-text-field 
                    label="이메일"
                    v-model="loginForm.email"
                    :rules="rules.email"
                ></v-text-field>
                <v-text-field 
                    label="비밀번호"
                    type="password"
                    v-model="loginForm.password"
                    :rules="rules.password"
                ></v-text-field>
            </v-form>
        </template>
        <template v-slot:actions>
            <v-flex>
                <v-btn 
                    block 
                    color="primary"
                    class="my-2"
                    @click="submitLoginForm"
                >
                    로그인
                </v-btn>
                <router-link to="/signup">
                    <v-btn 
                        block
                        color="secondary"
                    >
                        회원가입
                    </v-btn>
                </router-link>
            </v-flex>
        </template>
    </center-card>
</template>

<script>
import CenterCard from '@/components/layout/CenterCard.vue';
import { mapActions } from 'vuex';

export default {
    name: 'LoginView',
    components: {
        CenterCard,
    },
    data: function() {
        return {
            loginForm: {                
                email: '',
                password: '',
            },
            rules: {
                email: [
                    value => !!value || '이메일을 입력해주세요.',
                ],
                password: [
                    value => !!value || '비밀번호를 입력해주세요.',
                ],
            }
        }
    },
    methods: {
        ...mapActions({
            login: 'user/login', 
        }),
        submitLoginForm() {
            this.login(this.loginForm)
                .then(() => {
                    alert('로그인 성공');
                    this.$router.push('/');
                })
                .catch(() => {
                    alert('아이디 또는 비밀번호가 일치하지 않습니다.');
                });
        }
    }
}
</script>