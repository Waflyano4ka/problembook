<template>
  <v-container class="px-12 pt-0">
    <v-layout align-center justify-start row class="my-4 pt-1">
      <project-create :profile="profile"/>
      <v-checkbox label="Мои проекты" color="accent"></v-checkbox>
    </v-layout>
    <v-layout v-if="profile" align-center justify-start row>
      <project-row
          v-for="([], i) in sortedProjects"
          :key="i"
      />
    </v-layout>
  </v-container>
</template>

<script>
import ProjectRow from '../../components/project/ProjectRow.vue'
import ProjectCreate from '../../components/project/ProjectCreate.vue'


export default {
  components: {
    ProjectRow,
    ProjectCreate
  },
  data()  {
    return {
      profile: frontendData.profile,
      sortedProjects: [
        [],
        [],
        [],
        [],
        [],
        [],
      ]
    }
  }
}
</script>

<style scoped>

</style>