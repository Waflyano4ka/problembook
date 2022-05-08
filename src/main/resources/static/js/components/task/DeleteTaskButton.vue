<template>
  <v-dialog
      v-model="dialog"
      persistent
      max-width="590"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn fab color="white" elevation="0" small class="me-2"
             v-bind="attrs"
             v-on="on">
        <v-icon>mdi-delete-outline</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="text-h5">
        Вы действительно хотите удалить задачу &nbsp;<span>"{{ task.name }}"</span>?
      </v-card-title>
      <v-card-text>Данная задача будет удалена навседа. Вы действиетльно этого хотите?</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="accent"
            text
            @click="close"
        >
          Отмена
        </v-btn>
        <v-btn
            color="redAccent"
            text
            @click="deleteTaskButton()"
        >
          Удалить
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions} from "vuex";

export default {
  props: ['task', 'deleteTask'],
  data () {
    return {
      dialog: false,
    }
  },
  computed: {

  },
  methods: {
    deleteTaskButton() {
      this.dialog =false;
      this.deleteTask()
    },
    close(){
      this.dialog =false;
    }
  }
}
</script>

<style scoped>

</style>