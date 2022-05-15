<template>
  <v-dialog max-width="820px" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn block rounded color="secondary" v-on="on" v-bind="attrs" class="ma-1">
        Сдать задание
        <v-icon right dark>
          mdi-bookmark-check
        </v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-app-bar color="primary" elevation="0">
        <v-toolbar-title class="text-h6 pl-0">
          Сдача задания
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn icon @click="dialog = false">
          <v-icon>mdi-close-box-multiple-outline</v-icon>
        </v-btn>
      </v-app-bar>
      <v-card-text class="mt-2 pb-0">
        <v-file-input
            color="secondary"
            :rules="rules"
            accept=".docx, .doc, .pdf, .xlsx, .pptx"
            show-size
            truncate-length="15"
            label="Прикрепите, если это необходимо файл"
            v-model="file"
            ref="file"
        ></v-file-input>
      </v-card-text>
      <v-card-actions>
        <v-btn class="mb-2" block rounded color="secondary" @click="submit">
          {{ !!file ? 'Сдать здачу' : 'Сдать задачу без вложений' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
  props: ['taskUser'],
  data() {
    return {
      dialog: false,
      taskUserId: null,
      file: null,
      rules: [
        value => value.size < 10000000 || 'Размер файла не может превышать 10 MB!',
      ],
    }
  },
  methods: {
    ...mapActions(['UPLOAD_TASK_TO_SERVER', 'COMPLETE_TASK_ON_DB']),
    submit() {
      this.formHasErrors = false

      Object.keys(this.form).forEach(f => {
        if (!this.form.file)
          this.formHasErrors = true
        try {
          this.$refs[f].validate(true)
        } catch (error) {

        }

      })
      try {
        if (!this.formHasErrors){

          this.form.taskUserId = this.taskUser.id
          this.UPLOAD_TASK_TO_SERVER(this.form)
        }
        else {
          this.COMPLETE_TASK_ON_DB()
        }
      }
      catch (e) {
        console.error(e)
      }

      this.dialog = false
    },
  },
  computed: {
    form () {
      return {
        file: this.file,
        taskUserId: this.taskUserId,
      }
    },
    ...mapGetters([])
  },
  watch: {
    name () {
      this.errorMessages = ''
    },
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>