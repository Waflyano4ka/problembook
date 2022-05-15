<template>
  <v-row align="start" class="px-4 pb-0 pt-4">
    <v-col :cols="OBJECT.active ? 8 : 12">
      <v-list subheader two-line>
        <task-group v-for="group in GROUPTASKS"
                  :key="group.id"
                  :group="group"
        />
      </v-list>
    </v-col>
    <v-col v-if="OBJECT.active" cols="4">
      <v-card
          class="mb-1"
          elevation="3"
          shaped
      >
        <v-card-text>
          <v-card-title class="px-0">
            Фраза дня:
          </v-card-title>
          <v-card-subtitle v-if="!daily" class="px-0 pb-0" @click="changeDaily">
            {{ this.OBJECT.dailyMessage === null ? "Доброго времени суток! Продолжаем выполнять задачи." : this.OBJECT.dailyMessage }}
          </v-card-subtitle>
          <v-textarea v-if="daily"
                      name="dailyTextChanged"
                      :rules="[() => !!dailyTextChanged && dailyTextChanged.length <= 250 || 'Повестка дня должна быть быть не больше 250 символов']"
                      v-model="dailyTextChanged"
                      style="margin-top: -24px"
                      color="accent"
                      counter
          ></v-textarea>
          <v-layout align-center justify-space-around row v-if="daily">
            <v-col cols="6" class="pb-0">
                <v-btn block rounded color="warning" @click="Cancel">
                  Отмена
                </v-btn>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-btn block rounded color="success" @click="Apply">
                Сохранить
              </v-btn>
            </v-col>
          </v-layout>
        </v-card-text>
        <v-card-actions v-if="CURRENT_ROLE === 'CREATOR' || CURRENT_ROLE === 'REDACTOR'">
          <v-layout align-center justify-space-around column>
            <task-create/>
            <group-list/>
          </v-layout>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import TaskCreate from './TaskCreate.vue'
import TaskGroup from './TaskGroup.vue'
import GroupList from '../groups/GroupList.vue'
import {mapActions, mapState, mapGetters} from "vuex";
import router from "../../router";

export default {
  components: {
    TaskCreate, TaskGroup, GroupList
  },
  data: function () {
    return {
      daily: false,
      dailyTextChanged: null
    }
  },
  methods: {
    ...mapActions(['GET_MEMBERS_FORM_DB', 'GET_TASKS_FORM_DB', 'CHANGE_DAILY_MESSAGE']),
    changeDaily() {
      if (this.CURRENT_ROLE === 'CREATOR' || this.CURRENT_ROLE === 'REDACTOR')
      {
        this.dailyTextChanged = this.OBJECT.dailyMessage
        this.daily = !this.daily
      }
    },
    Cancel() {
      this.changeDaily()
    },
    Apply() {
      if (this.dailyTextChanged.length <= 250)
        this.CHANGE_DAILY_MESSAGE(this.dailyTextChanged)

      this.changeDaily()
    }
  },
  mounted() {
    this.GET_MEMBERS_FORM_DB(this.$route.params.id)
    this.GET_TASKS_FORM_DB()
  },
  computed: {
    ...mapState(['profile']),
    ...mapGetters(['CURRENT_ROLE', 'GROUPTASKS', 'OBJECT'])
  }
}
</script>

<style scoped>

</style>