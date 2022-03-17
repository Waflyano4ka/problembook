<template>
  <v-dialog
      v-model="dialog"
      persistent
      max-width="590"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          text
          color="redAccent"
          v-bind="attrs"
          v-on="on"
      >
        Заблокировать
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="text-h5">
        Вы действительно хотите заблокировать &nbsp;<span>{{ member.user.name }}</span>?
      </v-card-title>
      <v-card-text>После блокировки данного пользователя, он не сможет войти и подключиться к данному проекту</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="accent"
            text
            @click="close"
        >
          Отмена
        </v-btn>
        <v-btn
            color="redAccent"
            text
            @click="changeAccess()"
        >
          Заблокировать
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions} from "vuex";

export default {
  props: ['member', 'blockUser'],
  data () {
    return {
      dialog: false,
      currentMemberID: this.member.id,
    }
  },
  computed: {
    ...mapActions(['CHANGE_MEMBER_ACCESS'])
  },
  methods: {
    changeAccess() {
      this.$store.state.project.currentMemberId = this.currentMemberID
      this.blockUser()

      this.dialog =false;
    },
    close(){
      this.dialog =false;
    }
  }
}
</script>

<style scoped>

</style>