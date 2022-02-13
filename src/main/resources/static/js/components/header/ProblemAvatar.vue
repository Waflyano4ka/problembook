<template>
  <v-menu bottom min-width="250px" rounded offset-y>
    <template v-slot:activator="{ on }">
      <v-btn v-if="!profile" icon x-large href="/login">
        <v-icon size="36">
          mdi-login-variant
        </v-icon>
      </v-btn>
      <v-btn v-else icon x-large v-on="on">
        <v-avatar color="white" size="36">
          <img v-if="profile.image" :src="profile.image">
          <span v-else class="white--text text-h5">##</span>
        </v-avatar>
      </v-btn>
    </template>
    <v-card v-if="profile">
      <v-list-item-content class="justify-center">
        <div class="mx-auto text-center">
          <v-avatar color="white" size="62" class="ma-3">
            <img v-if="profile.image" :src="profile.image">
            <span v-else class="white--text text-h5">##</span>
          </v-avatar>
          <h3>{{ profile.name }}</h3>
          <p class="text-caption mt-1">
            {{ profile.email }}
          </p>
          <v-divider class="my-3"></v-divider>
          <v-btn text href="/logout">
            Выйти
          </v-btn>
        </div>
      </v-list-item-content>
    </v-card>
  </v-menu>
</template>

<script>
  import { mapState } from "vuex";

  export default {
    computed: mapState(['profile']),
    data: () => ({
      user: {
        fullName: 'John Doe',
        email: 'john.doe@doe.com',
      },
    }),
  }
</script>

<style scoped>

</style>