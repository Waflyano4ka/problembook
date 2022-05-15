<template>
  <v-list-item>
    <v-list-item-avatar>
      <v-icon class="grey lighten-1" dark>
        mdi-folder-settings
      </v-icon>
    </v-list-item-avatar>

    <v-list-item-content>
      <v-list-item-title v-text="group.name"></v-list-item-title>
    </v-list-item-content>

    <v-list-item-action>
      <v-btn icon @click="getCurrentGroup(group)">
        <v-icon color="grey lighten-1">mdi-information</v-icon>
      </v-btn>
    </v-list-item-action>
  </v-list-item>
</template>

<script>
export default {
  props: ['group', 'getCurrentGroup'],
}
</script>

<style scoped>

</style>