<template>
  <v-dialog persistent max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn block rounded color="accent" v-on="on" v-bind="attrs" class="ma-1">
        Добавить
        <v-icon right dark>
          mdi-calendar-check
        </v-icon>
      </v-btn>
    </template>
    <v-card ref="form" class="">
      <v-app-bar color="accent" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Добавление задания
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn icon @click="dialog = false">
          <v-icon>mdi-close-box-multiple-outline</v-icon>
        </v-btn>
      </v-app-bar>
      <v-card-text>
        <v-container>
          <v-text-field color="secondary"
                        label="Название*"
                        ref="name"
                        v-model="name"
                        :rules="[
              () => !!name || 'Это поле необходимо заполнить',
              () => !!name && name.length <= 100 || 'Название должно быть не меньше 1 и не больше 100 символов'
            ]"
                        counter="100"
                        required/>
          <v-textarea color="secondary"
                      label="Описание"
                      rows="6"
                      no-resize
                      v-model="description"
          />
          <v-row>
            <v-col>
              <v-checkbox
                  color="accent"
                  v-model="timeVisible"
                  label="Срок сдачи"
              ></v-checkbox>
            </v-col>
            <v-col>
              <v-checkbox
                  color="accent"
                  v-model="groupVisible"
                  label="Без группы"
              ></v-checkbox>
            </v-col>
          </v-row>
          <v-select v-if="!groupVisible"
                      v-model="group"
                      color="accent"
                      :items="GROUPS"
                      :item-text="(obj) => (obj)['name']"
                      :return-object="true"
                      label="Группа"
          />
          <task-members @membersChange="membersChange"/>
          <task-date-time-picker v-if="timeVisible" @dateChange="dateChange"/>
        </v-container>
        <small>*поля необходимые для заполнения</small>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="accent" @click="submit" text>
          Добавить
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import TaskDateTimePicker from './TaskDateTimePicker.vue'
import TaskMembers from "./TaskMembers.vue";
import {mapActions, mapGetters, mapState} from "vuex";

export default {
  components: {
    TaskDateTimePicker, TaskMembers
  },
  data() {
    return {
      dialog: false,
      timeVisible: false,
      groupVisible: true,
      group: null,
      members: [],
      datetime: null,
      name: null,
      description: '',
      picker: new Date().toISOString().substr(0, 7),
    }
  },
  methods: {
    ...mapActions(['ADD_TASK_TO_DB', 'GET_GROUPS_FROM_DB']),
    submit() {
      this.formHasErrors = false

      Object.keys(this.form).forEach(f => {
        if (!this.form.name) this.formHasErrors = true

        try {
          this.$refs[f].validate(true)
        } catch (error) {

        }

      })

      if (!this.formHasErrors){

        if (!this.datetime) {
          const [year, month, day] = (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10).split('-')
          this.datetime = `${day}/${month}/${year} 23:59`
        }

        if (this.groupVisible) {
          this.group = null
        }

        this.ADD_TASK_TO_DB(this.form)

        this.dialog = false
      }
    },
    membersChange(val) {
      this.members = val
    },
    dateChange(val) {
      this.datetime = val
    }
  },
  mounted() {
    this.GET_GROUPS_FROM_DB()
  },
  computed: {
    form () {
      return {
        name: this.name,
        description: this.description,
        members: this.members,
        enableTime: this.timeVisible,
        datetime: this.datetime,
        groupEnable: !this.groupVisible,
        group: this.group,
      }
    },
    ...mapGetters(['GROUPS'])
  },
}
</script>

<style scoped>

</style>