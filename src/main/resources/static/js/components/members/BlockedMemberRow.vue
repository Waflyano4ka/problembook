<template>
  <v-list-item
      class="repeating-gradient mb-3"
      color="accent"
  >
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

      <v-list-item-subtitle v-text="profile.user.email"></v-list-item-subtitle>
    </v-list-item-content>

    <v-list-item-action>
        <v-btn text color="success" v-if="CREATOR_BOOL" @click="unBlockUser">
          Разблокировать
        </v-btn>
    </v-list-item-action>
  </v-list-item>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
  props: ['profile', 'blockUser'],
  data: () => ({
    active: false
  }),
  computed: {
    ...mapGetters(['CREATOR_BOOL']),
    ...mapActions(['CHANGE_MEMBER_ACCESS'])
  },
  methods: {
    unBlockUser() {
      this.$store.state.project.currentMemberId = this.profile.id

      this.blockUser()
    }
  }
}
</script>

<style scoped>
  .v-list-item {
    height: 60px !important;
    min-height: 60px !important;
  }
  .repeating-gradient {
    background-image: repeating-linear-gradient(-45deg,
      rgba(255, 0, 0, 0.1),
      rgba(255, 0, 0, 0.1) 5px,
      rgba(255, 255, 255, 0) 5px,
      rgba(255, 255, 255, 0) 10px
    );
  }
</style>