<template>
    <div 
        :style="style" 
        class="pointer"
        @click="moveToHome"
    >
        <v-img
            :src="require('@/assets/images/logo.png')"
            class="mt-2"
        ></v-img>
    </div>
</template>

<script>
export default {
    name: 'LogoImage',
    props: ['width'],
    computed: {
        style() {
            return {
                width: `${this.width ? this.width : 120}px`, 
            }
        }
    },
    methods: {
        moveToHome() {
            this.$router.push('/');
        }
    }
}
</script>
