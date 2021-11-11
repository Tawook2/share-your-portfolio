<template>
    <v-layout class="justify-center align-center">
        <v-card class="px-4 py-4" width="360">
            <logo-image :width="200" class="mx-auto"></logo-image>
            <v-card-text>
                <slot name="content"></slot>
            </v-card-text>
            <v-card-actions class="pa-0">
                <slot name="actions"></slot>
            </v-card-actions>
        </v-card>
    </v-layout>
</template>

<script>
import LogoImage from '@/components/LogoImage.vue';

export default {
    name: 'CenterCard',
    components: {
        LogoImage,
    }
}
</script>