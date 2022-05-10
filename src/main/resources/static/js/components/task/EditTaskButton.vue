<template>
  <v-dialog persistent max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn fab color="white" elevation="0" small class="mx-2"
             v-on="on" v-bind="attrs">
        <v-icon>mdi-credit-card-edit-outline</v-icon>
      </v-btn>
    </template>
    <v-card ref="form" class="">
      <v-app-bar color="secondary" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Изменение задания
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
                        v-model="task.name"
                        :rules="[
              () => !!task.name || 'Это поле необходимо заполнить',
              () => !!task.name && task.name.length <= 100 || 'Название должно быть не меньше 1 и не больше 100 символов'
            ]"
                        counter="100"
                        required/>
          <v-textarea color="secondary"
                      label="Описание"
                      rows="6"
                      no-resize
                      v-model="task.description"
          />
          <!--
          <v-row>
            <v-col>
              <v-checkbox
                  color="secondary"
                  v-model="task.enableTime"
                  label="Срок сдачи"
              ></v-checkbox>
            </v-col>
            <v-col>
              <v-checkbox
                  color="secondary"
                  v-model="groupVisible"
                  label="Без группы"
              ></v-checkbox>
            </v-col>
          </v-row>
          <v-select v-if="!groupVisible"
                    v-model="groupVisible"
                    color="accent"
                    :items="GROUPS"
                    :item-text="(obj) => (obj)['name']"
                    :return-object="true"
                    label="Группа"
          />
          <task-date-time-picker-edit v-if="task.enableTime" @dateChange="dateChange"/>
          -->
        </v-container>
        <small>*поля необходимые для заполнения</small>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="secondary" @click="submit" text>
          Добавить
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import TaskDateTimePickerEdit from './TaskDateTimePickerEdit.vue'
import { mapActions, mapGetters, mapState } from "vuex";

export default {
  props: ['task'],
  components: {
    TaskDateTimePickerEdit
  },
  data() {
    return {
      dialog: false,
    }
  },
  methods: {
    ...mapActions(['EDIT_TASK_TO_DB', 'GET_GROUPS_FROM_DB_EDIT_TASK']),
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

        // if (!this.task.datetime) {
        //   const [year, month, day] = (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10).split('-')
        //   this.task.datetime = `${day}/${month}/${year} 23:59`
        // }

        // if (this.groupVisible) {
        //   this.group = null
        // }

        this.EDIT_TASK_TO_DB(this.form)
        this.dialog = false
      }
    },
    // dateChange(val) {
    //   this.task.datetime = val
    // }
  },
  // mounted() {
  //   this.GET_GROUPS_FROM_DB_EDIT_TASK(this.task.project.id)
  // },
  computed: {
    form () {
      return {
        name: this.task.name,
        description: this.task.description,
      }
    },
    ...mapGetters(['GROUPS'])
  },
}
</script>

<style scoped>

</style>