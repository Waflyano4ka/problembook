<template>
  <v-list subheader two-line class="px-4 pt-4 pb-0" ref="form">
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
      <v-btn icon @click="refreshKey">
        <v-icon>
          mdi-refresh
        </v-icon>
      </v-btn>
    </v-layout>
    <v-layout align-start justify-center row class="mt-2 mx-0">
      <v-layout align-space-around justify-start column>
        <settings-project-row-preview
            :color="showColor()"
            :name="showName()"
        />
      </v-layout>
      <v-color-picker
          show-swatches
          v-model="picker"
          hide-mode-switch
          mode="rgba">
      </v-color-picker>
    </v-layout>
    <v-layout align-start justify-end class="mt-9">
      <v-btn color="accent" @click="cancel" class="ms-3" outlined>
        Сбросить
      </v-btn>
      <v-btn color="success" @click="submit" class="ms-3">
        Изменить
      </v-btn>
    </v-layout>
  </v-list>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import SettingsProjectRowPreview from "./SettingsProjectRowPreview.vue";

export default {
  components: {
    SettingsProjectRowPreview
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
  props: ['project'],
  computed: {
    form () {
      return {
        name: this.name,
        color: this.picker.hex,
        keyToConnect: this.key,
      }
    },
  },
  methods: {
    ...mapActions(['EDIT_PROJECT_IN_DB']),
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
        this.EDIT_PROJECT_IN_DB(this.form)
      }
    },
    cancel() {
      this.name = this.project.name
      this.key = this.project.keyToConnect
      this.picker.hex = this.project.color
    }
  },
  mounted() {
    this.cancel()

    this.picker.hex = this.project.color
  },
  watch: {
    project (alt){
      this.cancel()
    }
  }
}
</script>

<style scoped>

</style>