<template>
  <v-list-group
      v-model="active"
      color="accent"
  >
    <template v-slot:activator>
      <v-list-item-avatar>
        <v-avatar size="36">
          <img
              alt="user"
              :src="profile.user.image"
          >
        </v-avatar>
      </v-list-item-avatar>
      <v-list-item-content>
        <v-list-item-title v-text="profile.user.name"></v-list-item-title>
      </v-list-item-content>
    </template>

    <v-list-item-content class="pa-0">
      <v-card elevation="0" class="rounded-0" color="shadow">
        <v-divider></v-divider>
        <v-card-text class="pa-0">
          <v-list class="transparent pa-0">
            <v-list-item>
              <v-list-item-subtitle>
                Почта:
              </v-list-item-subtitle>
              <v-list-item-subtitle class="text-right">
                {{ profile.user.email }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-list-item>
              <v-list-item-subtitle>
                Последний раз был в сети:
              </v-list-item-subtitle>
              <v-list-item-subtitle class="text-right">
                {{ profile.user.lastVisit }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-list-item>
              <v-list-item-subtitle>
                Регион:
              </v-list-item-subtitle>
              <v-list-item-subtitle class="text-right">
                {{ profile.user.locale }}
              </v-list-item-subtitle>
            </v-list-item>
            <v-row class="px-7">
              <v-select class="my-5"
                        v-model="profile.role.name"
                        :items="roles"
                        color="accent"
                        item-color="accent"
                        label="Роль"
                        dense
                        outlined
                        hide-details
                        append-icon="mdi-account-star"
                        :disabled="(profile.role.name === 'CREATOR' || !CREATOR_BOOL)"
                        v-on:change="changeRole(profile.id, profile.role.name)"
              ></v-select>
            </v-row>
          </v-list>
        </v-card-text>
        <v-card-actions class="py-0">
          <v-spacer></v-spacer>
          <block-button v-if="!(profile.role.name === 'CREATOR') && CREATOR_BOOL" :member="profile" :blockUser="blockUser"/>
          <kick-button v-if="!(profile.role.name === 'CREATOR') && CREATOR_BOOL" :member="profile" :kickUser="kickUser"/>
        </v-card-actions>
      </v-card>
    </v-list-item-content>
  </v-list-group>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import BlockButton from './buttons/BlockButton.vue'
import KickButton from './buttons/KickButton.vue'

export default {
  components: { BlockButton, KickButton },
  props: ['profile', 'roles', 'blockUser', 'kickUser'],
  data: () => ({
    action: 'mdi-silverware-fork-knife',
    active: false,
    changedRole: {
      roleName: null,
      projectUserID: null,
    }
  }),
  computed: {
    ...mapGetters(['CREATOR_BOOL']),
    ...mapActions(['CHANGE_MEMBER_ACCESS'])
  },
  methods: {
    changeRole(idUser, roleName) {
      this.changedRole.projectUserID = idUser
      this.changedRole.roleName = roleName

      let index = this.$store.state.role.changeUserRoles.data.findIndex(changeRole => changeRole.projectUserID === idUser)
      if (index === -1) {
        this.$store.state.role.changeUserRoles.data.push(this.changedRole)
      }
      else {
        this.$store.state.role.changeUserRoles.data.splice(index, 1, this.changedRole)
      }
    },
  }
}
</script>

<style scoped>
  .v-list-item {
    height: 32px !important;
    min-height: 32px !important;
  }
</style>