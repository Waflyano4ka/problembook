<template>
  <v-row align="start">
    <v-col cols="7">
      <v-date-picker
          v-model="date"
          full-width
          locale="ru-ru"
      ></v-date-picker>
    </v-col>
    <v-col cols="5">
      <v-time-picker
          v-model="time"
          format="24hr"
          scrollable
          full-width
      ></v-time-picker>
    </v-col>
    <v-text-field
        label="Дата сдачи задачи"
        prepend-icon="mdi-clock-time-four-outline"
        v-model="computedDateFormatted"
        readonly
    ></v-text-field>
  </v-row>
</template>

<script>
export default {
  event: ['dateChange'],
  data: vm => ({
    date: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    time: "23:59",
    dateFormatted: vm.formatDate((new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10)),
  }),

  computed: {
    computedDateFormatted () {
      return this.formatDate(this.date)
    },
  },

  watch: {
    date (val) {
      this.dateFormatted = this.formatDate(this.date)
    },
    time (val) {
      this.dateFormatted = this.formatDate(this.date)
    }
  },

  methods: {
    formatDate (date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      let string = ""

      if (!this.time) {
        string = `${day}/${month}/${year} 23:59`
      }
      else
        string = `${day}/${month}/${year} ${this.time}`

      this.$emit('dateChange', string)
      return string
    },
  },
}
</script>

<style scoped>

</style>