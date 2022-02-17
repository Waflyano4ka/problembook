<template>
  <v-container class="px-12 pt-0">
    <v-layout align-center justify-start row class="my-4 pt-1 ms-0">
      <v-checkbox label="Мои проекты" v-model="myProjects" color="accent" class="pt-2"></v-checkbox>
    </v-layout>
    <v-layout align-space-around justify-start column>
      <archived-project-row v-for="project in !myProjects ? ARCHIVED_PROJECTS : ARCHIVED_PROJECTS.filter(project => project.project.user.id === this.profile.id)"
                   :key="project.id"
                   :projectUser="project"
      />
    </v-layout>
  </v-container>
</template>

<script>
import ArchivedProjectRow from "../../components/project/ArchivedProjectRow.vue";

import {mapActions, mapGetters} from "vuex";

export default {
  components: {
    ArchivedProjectRow,
  },
  computed: {
    ...mapGetters(['ARCHIVED_PROJECTS'])
  },
  data()  {
    return {
      myProjects: false
    }
  },
  methods: {
    ...mapActions([
      'GET_PROJECTS_FORM_DB'
    ])
  },
  mounted() {
    this.GET_PROJECTS_FORM_DB()
  },
}
</script>

<style scoped>

</style>