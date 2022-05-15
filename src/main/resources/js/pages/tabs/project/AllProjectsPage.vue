<template>
  <v-container class="px-12 pt-0">
    <v-layout align-center justify-start row class="mt-4 mb-2 pt-1 ms-0">
      <project-create/>
      <project-join/>
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
      <project-row v-for="project in !myProjects ? PROJECTS.filter(project => contains(project.project.name)) : PROJECTS.filter(project => contains(project.project.name))
      .filter(project => project.project.user.id === this.profile.id)"
          :key="project.id"
          :projectUser="project"
      />
    </v-layout>
  </v-container>
</template>

<script>
  import ProjectRow from '../../../components/project/ProjectRow.vue'
  import ProjectCreate from '../../../components/project/ProjectCreate.vue'
  import ProjectJoin from '../../../components/project/ProjectJoin.vue'

  import {mapActions, mapGetters, mapState} from 'vuex'

  export default {
    components: {
      ProjectRow,
      ProjectCreate,
      ProjectJoin
    },
    computed: {
      ...mapGetters(['PROJECTS']),
      ...mapState(['profile'])
    },
    data()  {
      return {
        myProjects: false,
        search: ""
      }
    },
    methods: {
      ...mapActions(['GET_PROJECTS_FORM_DB']),
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