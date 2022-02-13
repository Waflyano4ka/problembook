<template>
  <v-container class="px-12 pt-0">
    <v-layout align-center justify-start row class="my-4 pt-1 ms-0">
      <project-create/>
      <v-checkbox label="Мои проекты" color="accent"></v-checkbox>
    </v-layout>
    <v-layout align-space-around justify-start column>
      <project-row v-for="project in PROJECTS"
          :key="project.id"
          :project="project"
      />
    </v-layout>
  </v-container>
</template>

<script>
  import ProjectRow from '../../components/project/ProjectRow.vue'
  import ProjectCreate from '../../components/project/ProjectCreate.vue'

  import { mapState } from 'vuex'
  import { mapActions } from 'vuex'
  import { mapGetters } from 'vuex'

  export default {
    components: {
      ProjectRow,
      ProjectCreate
    },
    computed: {
      ...mapState(['profile']),
      ...mapGetters(['PROJECTS'])
    },
    data()  {
      return {

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
    created() {
      console.log()
    }
  }
</script>

<style scoped>

</style>