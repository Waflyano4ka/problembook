<template>
    <v-app-bar app color="primary" class="px-3">
      <v-app-bar-nav-icon @click="openNavigationBar"></v-app-bar-nav-icon>

      <v-icon class="mx-3" color="accent">mdi-book-edit</v-icon>
      <v-toolbar-title>Problem Book</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn @click="theme" icon x-large class="me-3">
        <v-icon v-if="$vuetify.theme.dark" color="accent" size="36">
          mdi-weather-night
        </v-icon>
        <v-icon v-else color="accent" size="36">
          mdi-white-balance-sunny
        </v-icon>
      </v-btn>

      <problem-avatar :profile="profile"/>
    </v-app-bar>
</template>

<script>
  import ProblemAvatar from './ProblemAvatar.vue'

  import { mapState } from 'vuex'

  export default {
    components: {
      ProblemAvatar
    },
    computed: mapState(['profile']),
    methods: {
      theme() {
        this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
      },
      openNavigationBar() {
        this.$emit('openNavigationBar')
      }
    }
  }
</script>

<style scoped>

</style>