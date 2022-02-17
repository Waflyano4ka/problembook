<template>
  <v-snackbar
      v-model="snackbar.showing"
      :timeout="timeout"
      :color="snackbar.color"
      text
      bottom right
      elevation="3"
  >
    {{ snackbar.text }}

    <template v-slot:action="{ attrs }">
      <v-btn
          icon
          :color="snackbar.color"
          v-bind="attrs"
          @click="snackbar.showing = false"
      >
        <v-icon>mdi-close-circle</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
import { mapState } from "vuex";

export default {
  computed: mapState(['snackbar']),
  data: () => ({
    timeout: 5000,
  }),
}
</script>

<style scoped>

</style>