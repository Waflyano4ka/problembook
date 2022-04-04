<template>
  <v-card
      class="mx-auto mb-2 rounded-lg"
      elevation="1"
  >
    <v-card-text justify="center">
      <v-layout align-center justify-start row fill-height class="py-1 mx-1">
        <v-avatar size="48" class="ms-3">
          <img
              alt="user"
              :src="task.author.image"
          >
        </v-avatar>
        <v-col elevation="0" cols="8">
          <v-card elevation="0" max-height="100px">
            <v-toolbar-title v-text="task.name"/>
            <v-card-text justify="start" class="pa-0">
              <div class="clip">{{ task.description }}</div>
            </v-card-text>
          </v-card>
        </v-col>
        <v-spacer/>
        <v-btn icon v-if="CURRENT_ROLE !== 'READER'">
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
        <v-btn v-else-if="!complete"
               rounded
               color="accent"
               @click="complete = !complete"
        >
          Сдать
        </v-btn>
        <v-col v-else cols="1" align="center">
          <span>
            Сдано
          </span>
        </v-col>
      </v-layout>
    </v-card-text>
  </v-card>
</template>

<script>

import {mapGetters, mapState} from "vuex";

export default {
  props: ['task'],
  data: function () {
    return {
      complete: false,
    }
  },
  computed: {
    ...mapGetters(['CURRENT_ROLE'])
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