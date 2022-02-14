<template>
  <v-dialog persistent max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn elevation="3" class="me-5" v-on="on" v-bind="attrs">
        Создать проект
      </v-btn>
    </template>
    <v-card ref="form">
      <v-app-bar color="primary" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Создание проекта
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
          <v-layout align-center justify-start row class="mb-2">
            <v-text-field color="secondary"
                          label="Код поключения*"
                          ref="key"
                          v-model="key"
                          disabled class="ps-3 mt-5"
                          :rules="[() => !!key || 'Поле обязательно для заполнения']"
            />
            <v-btn icon @click="refreshKey" class="mb-3">
              <v-icon>
                mdi-refresh
              </v-icon>
            </v-btn>
          </v-layout>
          <v-layout align-center justify-center row class="mt-2 mx-0">
            <project-row-preview
                :color="showColor()"
                :name="showName()"
            />
            <v-spacer/>
            <v-color-picker show-swatches hide-inputs v-model="picker"></v-color-picker>
          </v-layout>
        </v-container>
        <small>*поля необходимые для заполнения</small>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="accent" @click="submit" text>
          Создать
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ProjectRowPreview from "./ProjectRowPreview.vue";

import {mapActions, mapState} from "vuex";

export default {
  components: {
    ProjectRowPreview
  },
  data () {
    return {
      dialog: false,
      picker: null,
      name: null,
      key: null,
      errorMessages: '',
      formHasErrors: false,
    }
  },
  methods: {
    ...mapActions(['ADD_PROJECT_TO_DB']),
    showColor () {
      if (this.picker){
        return this.picker.hex
      }
    },
    showName () {
      if (this.name){
        return this.name
      }
      else {
        return 'Название проекта'
      }
    },
    refreshKey() {
      this.key = Math.random().toString(36).slice(2);
    },
    submit() {
      this.formHasErrors = false

      Object.keys(this.form).forEach(f => {
        if (!this.form[f]) this.formHasErrors = true

        try {
          this.$refs[f].validate(true)
        } catch (error) {

        }

      })
      if (!this.formHasErrors){
        console.log(this.form)
        this.ADD_PROJECT_TO_DB(this.form)

        this.dialog = false
      }
    }
  },
  mounted() {
    this.refreshKey();
  },
  computed: {
    ...mapState(['profile']),
    form () {
      return {
        name: this.name,
        color: this.picker.hex,
        keyToConnect: this.key,
        active: true
      }
    },
  },

  watch: {
    name () {
      this.errorMessages = ''
    },
  },
}
</script>

<style scoped>

</style>