<template>
  <v-dialog max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn elevation="3" class="me-5" v-on="on" v-bind="attrs">
        Присоединиться
      </v-btn>
    </template>
    <v-card ref="form">
      <v-app-bar color="primary" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Присоединиться к проекту по коду
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn icon @click="dialog = false">
          <v-icon>mdi-close-box-multiple-outline</v-icon>
        </v-btn>
      </v-app-bar>
      <v-card-text>
        <v-container>
          <v-text-field color="secondary"
                        label="Код для присодинения*"
                        ref="key"
                        v-model="key"
                        :rules="[() => !!key || 'Это поле необходимо заполнить']"
                        required/>
        </v-container>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="accent" @click="submit" text>
          Присоединиться
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>

import {mapActions, mapState} from "vuex";

export default {
  data () {
    return {
      dialog: false,
      key: null,
      errorMessages: '',
      formHasErrors: false,
    }
  },
  methods: {
    ...mapActions(['JOIN_PROJECT']),
    submit() {
      this.formHasErrors = false

      Object.keys(this.form).forEach(f => {
        if (!this.form[f])
          this.formHasErrors = true
        this.$refs[f].validate(true)
      })
      if (!this.formHasErrors){
        this.JOIN_PROJECT(this.form)

        this.dialog = false
      }
    }
  },
  computed: {
    form () {
      return {
        key: this.key,
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