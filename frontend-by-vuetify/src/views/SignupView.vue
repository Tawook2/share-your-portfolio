<template>
    <center-card>
        <template v-slot:content>
            <v-form
                ref="form" 
                width="600"
            >
                <v-text-field
                    label="이메일"
                    v-model="signupForm.email"
                    :rules="rules.email"
                    autocomplete="username"
                ></v-text-field>
                <v-text-field
                    label="비밀번호"
                    v-model="signupForm.password"
                    type="password"
                    :rules="rules.password"
                    autocomplete="new-password"
                ></v-text-field>
                <v-text-field
                    label="비밀번호 확인"
                    type="password"
                    :rules="rules.passwordConfirmation"
                ></v-text-field>
                <v-text-field
                    label="이름"
                    v-model="signupForm.name"
                    :rules="rules.name"
                ></v-text-field>
            </v-form>
        </template>
        <template v-slot:actions>
            <v-flex>
                <v-btn
                    block
                    color="success"
                    @click="submitSignupForm"
                >
                    회원가입
                </v-btn>
            </v-flex>
        </template>
    </center-card>
</template>

<script>
import CenterCard from '@/components/layout/CenterCard.vue';

export default {
    name: 'SignupView',
    components: {
        CenterCard,
    },
    data: function() {
        return {
            signupForm: {
                email: '',
                password: '',
                name: '',
            },
            rules: {
                email: [
                    value => !!value || '이메일은 필수 입력값입니다.',
                    value => /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i.test(value) || '이메일 형식에 맞지 않습니다.',
                ],
                password: [
                    value => !!value || '비밀번호는 필수 입력값입니다.',
                ],
                passwordConfirmation: [
                    value => value === this.signupForm.password || '비밀번호가 일치하지 않습니다.',
                ],
                name: [
                    value => !!value || '이름은 필수 입력값입니다.',
                ],
            }
        };
    },
    methods: {
        validate() {
            const valid = this.$refs.form.validate();
            if (valid) {
                console.log(this.signupForm);
            }
        },
        submitSignupForm() {
            this.validate();
        },
    }
}
</script>