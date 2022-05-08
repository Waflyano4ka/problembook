<template>
  <v-container class="px-12 pt-0">
    <v-layout align-center justify-start row class="mt-4 mb-2 pt-1 ms-0">
      <v-checkbox label="Мои проекты" v-model="myProjects" color="accent" class="pt-2"></v-checkbox>
      <v-spacer/>
      <v-text-field
          color="accent"
          solo
          v-model="search"
          placeholder="Поиск"
          style="margin-bottom: -32px"
          class="ps-3 mb-0 mt-7 me-2"
          label="Поиск"
          prepend-inner-icon="mdi-magnify"
      ></v-text-field>
    </v-layout>
    <v-layout align-space-around justify-start column>
      <archived-project-row v-for="project in !myProjects ? ARCHIVED_PROJECTS.filter(project => contains(project.project.name)) :
      ARCHIVED_PROJECTS.filter(project => project.project.user.id === this.profile.id).filter(project => contains(project.project.name))"
                   :key="project.id"
                   :projectUser="project"
      />
    </v-layout>
  </v-container>
</template>

<script>
import ArchivedProjectRow from "../../../components/archive/ArchivedProjectRow.vue";

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
      myProjects: false,
      search: ""
    }
  },
  methods: {
    ...mapActions([
      'GET_PROJECTS_FORM_DB'
    ]),
    contains(string) {
      if (this.search)
        return string.includes(this.search)
      return true
    }
  },
  mounted() {
    this.GET_PROJECTS_FORM_DB()
  },
}
</script>

<style scoped>

</style>