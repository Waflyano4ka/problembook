<template>
  <v-app>
    <problem-header
        :profile="profile"
        @openNavigationBar="openNavigationBar"
    />
    <problem-navigation
        ref="modal"
    />
    <v-content app>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
import ProblemHeader from '../components/header/ProblemHeader.vue'
import ProblemNavigation from '../components/navigation/ProblemNavigation.vue'

export default {
  components: {
    ProblemHeader,
    ProblemNavigation
  },
  data() {
    return {
      profile: frontendData.profile,
    }
  },
  methods: {
    openNavigationBar() {
      this.$refs.modal.open()
    },
  }
}
</script>

<style>
  html {
    overflow-y: auto !important
  }
</style>