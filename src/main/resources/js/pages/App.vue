<template>
  <v-app>
    <problem-header
        @openNavigationBar="openNavigationBar"
    />
    <problem-navigation
        ref="modal"
    />
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
    <problem-alert/>
  </v-app>
</template>

<script>
import ProblemHeader from '../components/header/ProblemHeader.vue'
import ProblemNavigation from '../components/navigation/ProblemNavigation.vue'
import ProblemAlert from '../components/alert/ProblemAlert.vue'

import { mapState } from "vuex";

export default {
  components: {
    ProblemHeader,
    ProblemNavigation,
    ProblemAlert
  },
  computed: mapState(['profile']),
  data() {
    return {

    }
  },
  methods: {
    openNavigationBar() {
      this.$refs.modal.open()
    },
  },
  beforeUpdate() {
    if (!this.profile){
      this.$router.replace('/')
    }
  }
}
</script>

<style>
  html {
    overflow-y: auto !important
  }
</style>