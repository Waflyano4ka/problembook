<template>
  <v-row align="start" class="px-4 pb-0 pt-4">
    <v-col cols="8">
      <v-list subheader two-line>
        <task-row v-for="task in TASKS"
                  :key="task.id"
                  :task="task"
        />
      </v-list>
    </v-col>
    <v-col cols="4">
      <v-card
          class="mb-1"
          elevation="3"
          shaped
      >
        <v-card-text>
          <v-card-title class="px-0">
            Фраза дня:
          </v-card-title>
          <v-card-subtitle class="px-0">
            Продолжаем выполнять задачи.
          </v-card-subtitle>
        </v-card-text>
        <v-card-actions v-if="CURRENT_ROLE === 'CREATOR' || CURRENT_ROLE === 'REDACTOR'">
          <task-create/>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import TaskCreate from './TaskCreate.vue'
import TaskRow from './TaskRow.vue'
import {mapActions, mapState, mapGetters} from "vuex";
import router from "../../router";

export default {
  components: {
    TaskCreate, TaskRow
  },
  methods: {
    ...mapActions(['GET_MEMBERS_FORM_DB', 'GET_TASKS_FORM_DB']),
  },
  mounted() {
    this.GET_MEMBERS_FORM_DB(this.$route.params.id)
    this.GET_TASKS_FORM_DB(this.$route.params.id)
  },
  computed: {
    ...mapState(['profile']),
    ...mapGetters(['CURRENT_ROLE', 'TASKS'])
  }
}
</script>

<style scoped>

</style>