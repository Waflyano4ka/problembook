<template>
  <v-container class="px-12 pt-0">
    <v-card flat class="rounded-t-xl mt-9" style="padding-top: 1px">
      <v-toolbar :color="OBJECT.color" extended flat class="pt-0" height="64px" style="border-top-left-radius: 32px;">
        <v-sheet style="padding-left: 16px; padding-right: 24px; margin-left: -16px; margin-top: -32px"
                 class="rounded-br-xl rounded-tl-xl" max-width="calc(100% - 70px)">
            <v-toolbar-title class="nav-text-title text-h5 font-weight-medium" v-text="OBJECT.name"/>
        </v-sheet>
        <template v-slot:extension>
          <v-spacer/>
          <v-sheet v-if="VISIBLE_SEARCH" width="calc(100% - 127px)" height="50px" class="rounded-tl-xl" style="margin-right: -16px;">
            <v-text-field
                color="accent"
                v-model="$store.state.search"
                style="margin-top: 16px"
                class="ps-3"
                label="Поиск"
                prepend-icon="mdi-magnify"
                clearable
            ></v-text-field>
          </v-sheet>
        </template>
      </v-toolbar>
      <v-tabs vertical color="accent" v-model="model">
        <v-tab :href="'#tasks'" @click="$router.replace({hash: '#tasks'})">
          <v-icon left>mdi-calendar-check</v-icon>
          Задачи
        </v-tab>
        <v-tab :href="'#members'" @click="$router.replace({hash: '#members'})">
          <v-icon left>mdi-account-multiple</v-icon>
          Участники
        </v-tab>
        <v-tab v-if="OBJECT.user.id === profile.id" :href="'#settings'" @click="$router.replace({hash: '#settings'})">
          <v-icon left>mdi-cog-outline</v-icon>
          Настройки
        </v-tab>
        <v-tabs-items v-model="model">
          <v-tab-item :value="`tasks`">
            <v-card flat class="rounded-0">
              <tasks-list/>
            </v-card>
          </v-tab-item>
          <v-tab-item :value="`members`">
            <v-card flat class="rounded-0">
              <members-list/>
            </v-card>
          </v-tab-item>
          <v-tab-item v-if="OBJECT.user.id === profile.id" :value="`settings`">
            <v-card flat class="rounded-0">
              <settings :project="OBJECT"/>
            </v-card>
          </v-tab-item>
        </v-tabs-items>
      </v-tabs>
    </v-card>
  </v-container>
</template>

<script>
import {mapActions, mapGetters, mapState} from "vuex";
import MembersList from '../../../components/members/MembersList.vue'
import Settings from '../../../components/settings/Settings.vue'
import TasksList from '../../../components/tasks/TasksList.vue'

  export default {
    components: {
      MembersList, Settings, TasksList
    },
    data () {
      return {
        model: 'tasks'
      }
    },
    computed: {
      ...mapGetters(['OBJECT', 'CREATOR_BOOL', 'VISIBLE_SEARCH']),
      ...mapState(['profile'])
    },
    methods: {
      ...mapActions(['GET_PROJECT_FORM_DB']),
      hashChange(name){
        switch (name){
          case "#tasks": {
            this.model = 'tasks'
            this.$store.state.project.currentHash = name
            break
          }
          case "#members": {
            this.model = 'members'
            this.$store.state.project.currentHash = name
            break
          }
          case "#settings": {
            this.model = 'settings'
            this.$store.state.project.currentHash = name
            break
          }
          default: {
            this.model = 'tasks'
            this.$store.state.project.currentHash = "#tasks"
            break
          }
        }
      },
    },
    mounted() {
      this.GET_PROJECT_FORM_DB(this.$route.params.id)
      this.hashChange(this.$route.hash)
    },
    watch: {
      $route (to, from){
        this.hashChange(to.hash)
      }
    }
  }
</script>

<style scoped>

</style>