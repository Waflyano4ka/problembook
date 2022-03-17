<template>
  <v-dialog
      v-model="dialog"
      persistent
      max-width="640"
      width="640"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          text
          color="accent"
          v-bind="attrs"
          v-on="on"
      >
        Выгнать
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="text-h5">
        Вы действительно хотите выгнать &nbsp;<span> {{ member.user.name }}</span>?
      </v-card-title>
      <v-card-text>Пользователь будет удален из данного проекта</v-card-text>
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
          Выгнать
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import {mapActions} from "vuex";

export default {
  props: ['member', 'kickUser'],
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
      this.kickUser()

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