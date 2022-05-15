<template>
  <v-list-item  >
    <v-list-item-avatar>
      <v-icon> mdi-file </v-icon>
    </v-list-item-avatar>

    <v-list-item-content>
      <v-list-item-title class="clip" v-text="taskUser.filename.substring(taskUser.filename.indexOf('.') + 1)"></v-list-item-title>

      <v-list-item-subtitle class="clip"> {{ 'Загружен: ' + taskUser.datetime }} </v-list-item-subtitle>
    </v-list-item-content>

    <v-list-item-action>
      <v-btn icon @click="download()">
        <v-icon color="grey lighten-1">mdi-download</v-icon>
      </v-btn>
    </v-list-item-action>
  </v-list-item>
</template>

<script>
import {mapActions} from "vuex";

export default {
  props: ['taskUser'],
  data() {
    return {

    }
  },
  methods: {
    ...mapActions(['DOWNLOAD_FILE']),
    download (){
      this.DOWNLOAD_FILE(this.taskUser.filename)
    }
  }
}
</script>

<style scoped>
.clip {
  white-space: nowrap; /* Запрещаем перенос строк */
  overflow: hidden; /* Обрезаем все, что не помещается в область */
  text-overflow: ellipsis; /* Добавляем многоточие */
}
</style>