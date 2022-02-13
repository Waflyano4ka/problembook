<template>
  <v-card width="400" min-height="300" class="me-5 mb-5" :color="color">
    <v-sheet height="150px" :color="color">
      <v-app-bar flat color="rgba(0, 0, 0, 0)">
        <v-sheet style="padding-left: 16px; padding-right: 16px; margin-left: -16px" class="rounded-r-xl" max-width="250">
          <v-toolbar-title class="text-h6" v-text="name" />
        </v-sheet>

        <v-spacer></v-spacer>
        <v-sheet class="rounded-xl 2px" style="padding: 2px">
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
        <v-sheet width="400" height="70" class="ps-4 rounded-tr-xl">
          <v-layout align-center justify-start row fill-height class="mt-0">
            <v-avatar size="56" class="ms-3">
              <img
                  alt="user"
                  :src="profile.image"
              >
            </v-avatar>
            <v-col elevation="0">
              <v-card class="mt-5" elevation="0" max-width="290">
                <v-toolbar-title v-text="profile.name"/>
              </v-card>
            </v-col>
          </v-layout>
        </v-sheet>
      </v-card-title>
    </v-sheet>

    <v-card-text class="ps-0 pt-0">
      <v-sheet class="pt-1 rounded-br-xl" height="220">
        <v-divider class="mb-3"/>
        <div class="font-weight-bold ml-8 mb-2">
          Сегодня:
        </div>
        <v-timeline
            align-top
            dense
        >
          <v-timeline-item
              v-for="message in messages"
              :key="message.time"
              :color="color"
              small
          >
            <div class="font-weight-normal">
              <strong>{{ message.from }}</strong> {{ message.time }}
            </div>
            <div>
              <div>{{ message.message }}</div>
            </div>
          </v-timeline-item>
        </v-timeline>
      </v-sheet>
    </v-card-text>
  </v-card>
</template>

<script>
  import { mapState } from 'vuex'

  export default {
    props: ['name', 'color'],
    computed: mapState(['profile']),
    data: () => ({
      liked: true,
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