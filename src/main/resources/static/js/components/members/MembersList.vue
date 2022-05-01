<template>
  <v-list subheader two-line>
    <v-col cols="12" class="px-4 pb-0 pt-4">
      <v-btn block color="secondary" v-if="CHANGE_ROLE_AMOUNT && !archiveBool" @click="saveChangedRoles">
        Сохранить изменения ролей
      </v-btn>
    </v-col>

    <v-subheader inset>Администраторы
      <v-divider inset></v-divider>
    </v-subheader>
    <member-row v-for="member in MEMBERS.filter(member => member.role.name === 'CREATOR' && contains(member.user.name))"
                :key="member.id"
                :profile="member"
                :roles="ROLES"
                :archive-bool="archiveBool"
    />
    <v-subheader inset>Редакторы
      <v-divider inset></v-divider>
    </v-subheader>
    <member-row v-for="member in MEMBERS.filter(member => member.role.name === 'REDACTOR' && contains(member.user.name))"
                :key="member.id"
                :profile="member"
                :roles="ROLES.filter(role => role !== 'CREATOR')"
                :blockUser="blockUser"
                :kickUser="kickUser"
                :archive-bool="archiveBool"
    />
    <v-subheader inset>Читатели
      <v-divider inset></v-divider>
    </v-subheader>
    <member-row v-for="member in MEMBERS.filter(member => member.role.name === 'READER' && contains(member.user.name))"
                :key="member.id"
                :profile="member"
                :roles="ROLES.filter(role => role !== 'CREATOR')"
                :blockUser="blockUser"
                :kickUser="kickUser"
                :archive-bool="archiveBool"
    />
    <v-subheader inset v-if="CREATOR_BOOL && BLOKED_MEMBERS.length">Заблокированные пользователи
      <v-divider inset></v-divider>
    </v-subheader>
    <blocked-member-row v-for="member in BLOKED_MEMBERS.filter(blokedMember => contains(blokedMember.user.name))"
                        :key="member.id"
                        :profile="member"
                        :blockUser="blockUser"
                        :kickUser="kickUser"
    />
  </v-list>
</template>

<script>
import MemberRow from './MemberRow.vue'
import BlockedMemberRow from './BlockedMemberRow.vue'
import {mapActions, mapGetters} from "vuex";

export default {
  components: { MemberRow, BlockedMemberRow },
  props: ['archiveBool'],
  data () {
    return {

    }
  },
  computed: {
    ...mapGetters(['MEMBERS', 'BLOKED_MEMBERS', 'ROLES', 'CHANGE_ROLE_AMOUNT', 'CREATOR_BOOL'])
  },
  methods: {
    ...mapActions(['GET_MEMBERS_FORM_DB', 'GET_ROLES_FORM_DB', 'CHANGE_USER_ROLE', 'CHANGE_MEMBER_ACCESS', 'DELETE_MEMBER_FROM_DB']),
    saveChangedRoles() {
      this.CHANGE_USER_ROLE(this.$route.params.id)
    },
    blockUser() {
      this.CHANGE_MEMBER_ACCESS(this.$route.params.id)
    },
    kickUser() {
      this.DELETE_MEMBER_FROM_DB(this.$route.params.id)
    },
    contains(string) {
      if (this.$store.state.search)
        return string.includes(this.$store.state.search)
      return true
    }
  },
  mounted() {
    this.GET_MEMBERS_FORM_DB(this.$route.params.id)
    this.GET_ROLES_FORM_DB()
  },
}
</script>

<style scoped>

</style>