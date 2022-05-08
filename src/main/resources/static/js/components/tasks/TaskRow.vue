<template>
  <v-hover v-slot="{ hover }">
    <v-card :elevation="hover ? 4 : 2"
            @click="$router.push({ name: 'taskPage', params: { id: task.id } })"
            class="mx-auto mb-2 rounded-lg"
    >
      <v-card-text justify="center">
        <v-layout align-center justify-start row fill-height class="py-1 mx-1">
          <v-avatar size="48" class="ms-3">
            <img
                alt="user"
                :src="task.author.image"
            >
          </v-avatar>
          <v-col elevation="0" cols="6">
            <v-toolbar-title v-text="task.name"/>
            <v-card-text justify="start" class="pa-0">
              <div class="clip"> {{ `Создано: ${task.createDatetime}` }} </div>
            </v-card-text>
          </v-col>
          <v-spacer/>
          <v-col cols="4" align="end">
            <v-toolbar-title>
              Срок сдачи:
            </v-toolbar-title>
            <v-card-text justify="start" class="pa-0">
              <div class="clip"> {{ `${task.enableTime ? task.datetime : "Не назначен"}` }} </div>
            </v-card-text>
          </v-col>
        </v-layout>
      </v-card-text>
    </v-card>
  </v-hover>
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