<template>
  <v-dialog max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn block rounded color="secondary" v-on="on" v-bind="attrs" class="ma-1">
        Группы
        <v-icon right dark>
          mdi-folder-multiple
        </v-icon>
      </v-btn>
    </template>
    <v-card height="640">
      <v-app-bar color="primary" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Группы
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn icon @click="dialog = false">
          <v-icon>mdi-close-box-multiple-outline</v-icon>
        </v-btn>
      </v-app-bar>
      <v-card-text class="mt-2">
        <v-row align="start">
          <v-col cols="7" class="pb-0">
            <v-toolbar-title class="text-h6 pl-0">
              Список групп
            </v-toolbar-title>
            <v-list subheader two-line class="overflow-y-auto" max-height="500">
              <group-row v-for="group in GROUPS"
                         :key="group.id"
                         :group="group"
                         :getCurrentGroup="getCurrentGroup"
              />
            </v-list>
          </v-col>
          <v-col cols="5" class="pb-0">
            <v-toolbar-title class="text-h6">
              Добавить группу
            </v-toolbar-title>
            <v-card ref="form" elevation="0">
              <v-text-field v-model="name"
                            ref="name"
                            :rules="[() => !!name || 'Поле обязательно для заполнения']"
                            :error-messages="errorMessages"
                            required
                            label="Название группы"
              />
              <v-btn block rounded color="success" @click="submit" class="mt-2">
                Добавить
              </v-btn>
            </v-card>
            <v-toolbar-title class="text-h6 mt-6" v-if="currentGroup">
              Изменить группу
            </v-toolbar-title>
            <v-card ref="reform" elevation="0" v-if="currentGroup">
              <v-text-field v-model="rename"
                            ref="rename"
                            :rules="[() => !!rename || 'Поле обязательно для заполнения']"
                            :error-messages="errorMessagesRe"
                            required
                            label="Название группы"
              />
              <v-btn block rounded color="secondary" @click="resubmit" class="mt-2">
                Изменить
              </v-btn>
            </v-card>
            <v-btn block rounded color="accent" @click="cancel" class="mt-2" v-if="currentGroup">
              Отмена
            </v-btn>
            <v-btn block rounded color="error" @click="del" class="mt-2" v-if="currentGroup">
              Удалить
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import GroupRow from "./GroupRow.vue";

export default {
  components: {
    GroupRow
  },
  data() {
    return {
      dialog: false,
      name: null,
      rename: null,
      currentGroup: null,
      errorMessages: '',
      errorMessagesRe: '',
      formHasErrors: false,
      formHasErrorsRe: false,
    }
  },
  methods: {
    ...mapActions(['ADD_GROUP_TO_DB', 'EDIT_GROUP_TO_DB', 'DELETE_GROUPS_FROM_DB', 'GET_GROUPS_FROM_DB']),
    getCurrentGroup(group) {
      this.currentGroup = group
      this.rename = group.name
    },
    cancel() {
      this.currentGroup = null
      this.rename = null
    },
    del(){
      this.DELETE_GROUPS_FROM_DB(this.currentGroup)
      this.cancel()
    },
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
        this.ADD_GROUP_TO_DB(this.form)
      }
    },
    resubmit() {
      this.formHasErrorsRe = false

      Object.keys(this.reform).forEach(f => {
        if (!this.reform.rename) this.formHasErrorsRe = true

        try {
          this.$refs[f].validate(true)
        } catch (error) {

        }

      })

      if (!this.formHasErrorsRe){
        let group = Object.assign({}, this.currentGroup);
        group.name = this.reform.rename

        this.EDIT_GROUP_TO_DB(group)
      }
    },
  },
  computed: {
    form () {
      return {
        name: this.name,
      }
    },
    reform () {
      return {
        rename: this.rename,
      }
    },
    ...mapGetters(['GROUPS'])
  },
  watch: {
    name () {
      this.errorMessages = ''
    },
    rename () {
      this.errorMessagesRe = ''
    },
  },
  mounted() {
    this.GET_GROUPS_FROM_DB()
  }
}
</script>

<style scoped>

</style>