<template>
  <v-container class="px-12">
    <v-layout align-center justify-start column>
      <v-card class="font-weight-bold text-h1 rounded-tl-xl rounded-br-xl" elevation="4">
        <v-toolbar-title class="pa-3">
          <span style="color: #FF9100">Problem</span><span style="color: #FFE0B2">Book</span>
          <br/>
          <span style="color: #FFE0B2">Book</span><span style="color: #FF9100">Problem</span>
        </v-toolbar-title>
      </v-card>
      <v-sheet class="mt-4 text--caption" style="color: lightgrey">
        Разработка программного комплекса по управлению проектами организации
        <br/>
        (на примере ФГБОУ ВО «Российский экономический университет им. Г.В. Плеханова»)
      </v-sheet>
      <v-row class="mt-16">
        <v-spacer/>
        <v-btn v-if="!profile" x-large color="accent" block class="text-h4 font-weight-bold" href="/login">
          Авторизируйтесь для продолжения
        </v-btn>
        <v-btn v-if="profile" x-large color="accent" block class="text-h4 font-weight-bold" @click="$router.push({ name: 'allProjectsPage' })">
          Мои проекты
        </v-btn>
        <v-btn v-if="profile" x-large color="primary" block class="text-h4 font-weight-bold mt-4" style="color: #221E17AA" @click="$router.push({ name: 'archivedProjectsPage' })">
          Архив
        </v-btn>
        <v-spacer/>
      </v-row>
    </v-layout>
  </v-container>
</template>

<script>
import {mapState} from "vuex";

export default {
  computed: mapState(['profile']),
}
</script>

<style scoped>
  .v-toolbar__title {
    font-size: larger !important;
    line-height: 96px;
  }
</style>