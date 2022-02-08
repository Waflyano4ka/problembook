<template>
  <v-container class="px-12 mt-4">
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

export default {
  components: {
    ProjectRow
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
data() {
return {
profile: frontendData.profile,
}
},
<style scoped>

</style>