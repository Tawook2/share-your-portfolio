<template>
    <v-avatar :size="avatarSize">
        <v-img 
            v-if="!isError"
            :src="imgSrc"
            @error="isError = true"
        ></v-img>
        <v-img 
            v-else
            :src="require('@/assets/images/no-account-image.png')"></v-img>
    </v-avatar>
</template>

<script>
export default {
    name: 'UserImage',
    data: function() {
        return {
            isError: false,
        }
    },
    props: ['id', 'size'],
    computed: {
        imgSrc() {
            return `http://localhost:8081/api/account/image/${this.id}`; 
        },
        avatarSize() {
            let size = this.size;
            if (!size) {
                size = 40;
            }
            return size;
        }
    }
}
</script>