<template>
    <v-flex>
        <user-image></user-image>
    </v-flex>
</template>

<script>
import UserImage from '@/components/UserImage.vue';

export default {
    name: 'MessageUserInfo',
    components: {
        UserImage,
    }
}
</script>