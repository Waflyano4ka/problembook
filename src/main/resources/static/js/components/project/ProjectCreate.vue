<template>
  <v-dialog persistent max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn elevation="3" class="me-5" v-on="on" v-bind="attrs">
        Создать проект
      </v-btn>
    </template>
    <v-card>
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
          <v-text-field color="secondary" label="Название*" hint="Название должно быть не меньше 1 и не больше 30 символов" v-model="name"/>
          <v-layout align-center justify-start row class="mb-2">
            <v-text-field color="secondary" label="Код поключения*" v-model="key" disabled class="ps-3 mt-5"/>
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
                :profile="profile"
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
        <v-btn color="accent" @click="showName" text>
          Создать
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ProjectRowPreview from "./ProjectRowPreview.vue";

export default {
    components: {
      ProjectRowPreview
    },
    props: ['profile'],
    data () {
      return {
        dialog: false,
        picker: null,
        name: null,
        key: null,
      }
    },
    methods: {
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
      }
    },
    mounted() {
      this.refreshKey();
    }
}
</script>

<style scoped>

</style>