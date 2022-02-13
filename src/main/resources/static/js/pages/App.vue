<template>
  <v-app>
    <problem-header
        @openNavigationBar="openNavigationBar"
    />
    <problem-navigation v-if="profile"
        ref="modal"
    />
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import ProblemHeader from '../components/header/ProblemHeader.vue'
import ProblemNavigation from '../components/navigation/ProblemNavigation.vue'

import { mapState } from "vuex";

export default {
  components: {
    ProblemHeader,
    ProblemNavigation
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