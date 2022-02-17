<template>
  <v-card class="mb-5" :color="projectUser.project.color">
    <v-sheet height="150px" :color="projectUser.project.color">
      <v-app-bar flat color="rgba(0, 0, 0, 0)" height="64">
        <v-sheet style="padding-left: 16px; padding-right: 16px; margin-left: -16px" class="rounded-r-xl" max-width="calc(100% - 70px)">
          <v-toolbar-title class="text-h6" v-text="projectUser.project.name" />
        </v-sheet>

        <v-spacer></v-spacer>
        <v-sheet class="rounded-xl px-2" style="padding: 2px">
          <v-btn icon small @click="like">
            <v-icon v-if="projectUser.liked" color="red">mdi-heart</v-icon>
            <v-icon v-else>mdi-heart-outline</v-icon>
          </v-btn>
          <v-menu bottom right offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn small icon
                  v-bind="attrs"
                  v-on="on"
              >
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="">
                <v-list-item-title>
                  Настройки
                </v-list-item-title>
              </v-list-item>
              <v-list-item v-if="projectUser.project.user.id === profile.id" @click="archivingProject">
                <v-list-item-title>
                  Поместить в архив
                </v-list-item-title>
              </v-list-item>
              <v-list-item v-else @click="leaveProject">
                <v-list-item-title>
                  Покинуть проект
                </v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-sheet>
      </v-app-bar>

      <v-card-title class="ps-0">
        <v-sheet width="100%" height="70px" class="ps-4 rounded-tr-xl">
          <v-layout align-center justify-start row fill-height class="mt-0">
            <v-avatar size="56" class="ms-3">
              <img
                  alt="user"
                  :src="projectUser.project.user.image"
              >
            </v-avatar>
            <v-col elevation="0">
              <v-card class="mt-5" elevation="0" max-width="290">
                <v-toolbar-title v-text="projectUser.project.user.name"/>
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
  import {mapActions, mapState} from "vuex";

  export default {
    props: ['projectUser'],
    computed: mapState(['profile']),
    data: () => ({
      liked: false,
    }),
    methods: {
      ...mapActions(['LIKE_PROJECT', 'ARCHIVE_PROJECT', 'LEAVE_PROJECT']),
      like() {
        this.LIKE_PROJECT(this.projectUser.id)
      },
      archivingProject() {
        this.ARCHIVE_PROJECT(this.projectUser.id)
      },
      leaveProject() {
        this.LEAVE_PROJECT(this.projectUser.id)
      }
    }
  }
</script>

<style scoped>

</style>