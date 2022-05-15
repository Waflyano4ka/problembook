<template>
  <v-container class="px-12 pt-0">
    <v-card flat class="rounded-t-xl mt-9" style="padding-top: 1px">
      <v-toolbar :color="TASK.project.color" extended flat class="pt-0" height="64px" style="border-top-left-radius: 32px;">
        <v-sheet style="padding-left: 16px; padding-right: 24px; margin-left: -16px; margin-top: -32px"
                 class="rounded-br-xl rounded-tl-xl" max-width="calc(100% - 70px)">
          <v-toolbar-title class="nav-text-title text-h5 font-weight-medium text-hover-title" v-text="TASK.project.name"
            @click="$router.push({ name: 'projectPage', params: { id: TASK.project.id } })"
          />
        </v-sheet>
        <template v-slot:extension>
          <v-row align="center">
            <v-col>
              <v-sheet height="50px" class="rounded-t-xl" elevation="0"
                      style="padding-left: 16px; padding-right: 24px; padding-top: 16px">
              </v-sheet>
            </v-col>
            <v-col cols="3" class="px-0 pt-0" align="end">
              <edit-task-button v-if="TASK.project.active && CURRENT_TASK_ROLE.name !== 'READER'" :task="Object.assign({}, TASK)"/>
              <delete-task-button v-if="TASK.project.active && CURRENT_TASK_ROLE.name !== 'READER'" :task="TASK" :deleteTask="deleteTask"/>
            </v-col>
          </v-row>
        </template>
      </v-toolbar>
      <v-row class="mx-3" >
        <v-col cols="9">
          <v-card class="rounded-t-xl px-0 ms-3" elevation="3"
                  style="margin-right: 20px; margin-top: -24px">
            <v-app-bar flat
                       color="rgba(0, 0, 0, 0)"
                       width="100%"
                       elevation="1">
              <v-toolbar-title class="nav-text-title text-h5 font-weight-medium clip" v-text="TASK.name"/>
            </v-app-bar>
            <v-card-title>
              <v-avatar size="56">
                <img
                    alt="user"
                    :src="TASK.author.image"
                >
              </v-avatar>
              <p class="ml-3 mb-0 mt-2" style="line-height: 0.9em;">
                {{ TASK.author.name }}
                <br/>
                <span class="caption"> {{ `${TASK.createDatetime} ${!!TASK.editDatetime ? "(Изменено: " + TASK.editDatetime + ")" : ""}` }} </span>
              </p>
            </v-card-title>
            <v-card-text>
              <v-card-subtitle class="pb-1 ps-0">
                Описание:
              </v-card-subtitle>
              <v-card-text class="text--primary">
                {{ TASK.description }}
              </v-card-text>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="3">
          <v-card
              class="mb-1 mt-6"
              elevation="3"
              shaped
          >
            <v-card-text class="pb-0">
              <v-card-title class="px-0 pb-0">
                <v-row class="px-3 pb-0 mb-0">
                  <v-col class="pa-0">
                    <p>Срок сдачи:</p>
                  </v-col>
                  <v-col class="pa-0" align="end">
                    <p>{{ `${TASK.enableTime ? TASK.datetime : "Не назначен"}` }}</p>
                  </v-col>
                </v-row>
              </v-card-title>
            </v-card-text>
            <v-card-actions v-if="CURRENT_TASK_ROLE.name === 'READER'">
              <v-layout align-center justify-space-around column>
                <complete-task v-if="!CURRENT_TASK_USER.complete" :taskUser="CURRENT_TASK_USER"/>
                <v-list width="100%">
                  <task-file v-if="CURRENT_TASK_USER.complete && CURRENT_TASK_USER.enableFile" :task-user="CURRENT_TASK_USER"/>
                </v-list>
                <v-btn v-if="CURRENT_TASK_USER.complete" block rounded color="error" class="ma-1" @click="uncomplete()">
                  Отменить сдачу
                  <v-icon right dark>
                    mdi-cancel
                  </v-icon>
                </v-btn>
              </v-layout>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script>
import DeleteTaskButton from '../../../components/task/DeleteTaskButton.vue'
import EditTaskButton from '../../../components/task/EditTaskButton.vue'
import CompleteTask from './CompleteTask.vue'
import TaskFile from './TaskFile.vue'

import {mapActions, mapGetters} from "vuex";

export default {
  components: {
    DeleteTaskButton, EditTaskButton, CompleteTask, TaskFile
  },
  computed: {
    ...mapGetters(['TASK', 'CURRENT_TASK_ROLE', 'CURRENT_TASK_USER']),
  },
  methods: {
    ...mapActions(['GET_TASK_FORM_DB', 'DELETE_TASK_FORM_DB', 'COMPLETE_TASK_ON_DB']),
    deleteTask() {
      this.DELETE_TASK_FORM_DB()
    },
    uncomplete() {
      this.COMPLETE_TASK_ON_DB()
    }
  },
  mounted() {
    this.GET_TASK_FORM_DB()
  },
}
</script>

<style scoped>
  .text-hover-title:hover {
    text-decoration: underline;
    cursor: pointer;
  }
  .clip {
    white-space: nowrap; /* Запрещаем перенос строк */
    overflow: hidden; /* Обрезаем все, что не помещается в область */
    text-overflow: ellipsis; /* Добавляем многоточие */
  }
</style>