<template>
  <v-card class="mb-5" :color="project.color">
    <v-sheet height="150px" :color="project.color">
      <v-app-bar flat color="rgba(0, 0, 0, 0)" height="64">
        <v-sheet style="padding-left: 16px; padding-right: 16px; margin-left: -16px" class="rounded-r-xl" max-width="calc(100% - 70px)">
          <v-toolbar-title class="text-h6" v-text="project.name" />
        </v-sheet>

        <v-spacer></v-spacer>
        <v-sheet class="rounded-xl px-2" style="padding: 2px">
          <v-btn icon small>
            <v-icon v-if="liked" color="red">mdi-heart</v-icon>
            <v-icon v-else>mdi-heart-outline</v-icon>
          </v-btn>
          <v-btn icon small>
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </v-sheet>
      </v-app-bar>

      <v-card-title class="ps-0">
        <v-sheet width="100%" height="70px" class="ps-4 rounded-tr-xl">
          <v-layout align-center justify-start row fill-height class="mt-0">
            <v-avatar size="56" class="ms-3">
              <img
                  alt="user"
                  :src="project.user.image"
              >
            </v-avatar>
            <v-col elevation="0">
              <v-card class="mt-5" elevation="0" max-width="290">
                <v-toolbar-title v-text="project.user.name"/>
              </v-card>
            </v-col>
          </v-layout>
        </v-sheet>
      </v-card-title>
    </v-sheet>
    <v-spacer />
    <v-card-text class="ps-0 pt-0">
      <v-sheet class="pt-1 rounded-br-xl">
        <v-divider class="mb-3"/>
        <div class="font-weight-bold ml-8 pb-2">
          Пусто
        </div>
      </v-sheet>
    </v-card-text>
  </v-card>
</template>

<script>
  export default {
    props: ['project'],
    data: () => ({
      liked: false,
      messages: [
        {
          from: 'Вы',
          message: `Задание`,
          time: '10:42',
        },
        {
          from: 'Джон Дое',
          message: 'Что-то сделать',
          time: '13:27',
        },
      ],
    }),
  }
</script>

<style scoped>

</style>