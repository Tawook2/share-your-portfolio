<template>
  <v-app>
    <app-header></app-header>
    <v-main class="grey lighten-2">
      <v-container class="py-8" fluid fill-height>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';

export default {
  name: 'App',
  components: {
    AppHeader,
  },
}
</script>

<style>
.pointer {
  cursor: pointer;
}
</style>