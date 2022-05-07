<template>
  <div>
    <div v-if="group.tasks.length !== 0">
      <v-subheader class="text-h6">{{ group.name === "DEFAULT" ? "Без названия" : group.name }}</v-subheader>
      <v-divider></v-divider>
    </div>
    <task-row v-for="task in group.tasks"
              :key="task.id"
              :task="task"
              class="ms-5 my-3"
    />
  </div>
</template>

<script>
import TaskRow from './TaskRow.vue'

export default {
  components: {
    TaskRow
  },
  props: ['group'],
  data: function () {
    return {

    }
  }
}
</script>

<style scoped>

</style>