<template>
  <v-card width="400" min-height="300" class="me-5 mb-5">
    <v-img height="150px" src="https://cdn.pixabay.com/photo/2020/07/12/07/47/bee-5396362_1280.jpg">
      <v-app-bar flat color="rgba(0, 0, 0, 0)">
        <v-toolbar-title class="text-h6 white--text">
          Название проекта
        </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-btn color="white" icon>
          <v-icon v-if="liked" color="red">mdi-heart</v-icon>
          <v-icon v-else>mdi-heart-outline</v-icon>
        </v-btn>
        <v-btn color="white" icon>
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </v-app-bar>

      <v-card-title class="white--text">
        <v-avatar size="56">
          <img
              alt="user"
              src="https://cdn.pixabay.com/photo/2020/06/24/19/12/cabbage-5337431_1280.jpg"
          >
        </v-avatar>
        <p class="ml-3">
          John Doe
        </p>
      </v-card-title>
    </v-img>

    <v-card-text>
      <div class="font-weight-bold ml-8 mb-2">
        Today
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
  export default {
    data: () => ({
      liked: true
    }),
  }
</script>

<style scoped>

</style>