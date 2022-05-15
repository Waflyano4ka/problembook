<template>
  <div>
    <div v-if="group.tasks.filter(t => contains(t.name)).length !== 0">
      <v-subheader class="text-h6">{{ group.name === "DEFAULT" ? "Без названия" : group.name }}</v-subheader>
      <v-divider></v-divider>
    </div>
    <task-row v-for="task in group.tasks.filter(t => contains(t.name))"
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
  },
  methods: {
    contains(string) {
      if (this.$store.state.search)
        return string.includes(this.$store.state.search)
      return true
    }
  }
}
</script>

<style scoped>

</style>