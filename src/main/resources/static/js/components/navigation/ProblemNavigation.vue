<template>
  <v-navigation-drawer app
      v-model="drawer"
      temporary
  >
    <v-list color="primary" class="pb-0">
      <v-list-item>
        <v-list-item-icon>
          <v-icon>mdi-book-edit</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-toolbar-title class="font-weight-medium">Problem Book</v-toolbar-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <v-list color="secondary">
    </v-list>
    <v-list class="pt-0">
      <v-list-item
          v-for="([icon, text, path], i) in items"
          :key="i"
          @click="$router.push(path)"
      >
        <v-list-item-icon>
          <v-icon>{{ icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>{{ text }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <template v-slot:append>
      <div class="pa-2">
        <v-btn block>
          *Тык*
        </v-btn>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script>
  export default {
    data: () => ({
      drawer: false,
      items: [
        ['mdi-application-edit', 'Проекты', '/projects'],
        ['mdi-archive-remove', 'Архив', '/archive'],
      ],
    }),
    methods: {
      open(){
        this.drawer = true
      }
    },
    watch: {
      group () {
        this.drawer = false
      },
    },
  }
</script>

<style scoped>

</style>