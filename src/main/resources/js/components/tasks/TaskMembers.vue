<template>
  <v-select
      color="accent"
      item-color="accent"
      v-model="select"
      :items="READERS"
      item-text="user.name"
      item-value="id"
      label="Участники, которым предназначена задача"
      multiple
  >
    <template v-slot:selection="data">
      <v-chip
          :key="data.item.id"
          v-bind="data.attrs"
          :input-value="data.selected"
          :disabled="data.disabled"
          @click:close="data.parent.selectItem(data.item)"
      >
        <v-avatar class="accent white--text"
                  left>
          <img
              alt="user"
              :src="data.item.user.image"
          >
        </v-avatar>
        {{ data.item.user.name }}
      </v-chip>
    </template>
    <template v-slot:prepend-item>
      <v-list-item
          ripple
          @mousedown.prevent
          @click="toggle"
      >
        <v-list-item-action>
          <v-icon :color="select.length > 0 ? 'indigo darken-4' : ''">
            {{ icon }}
          </v-icon>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>
            Выбрать всех
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-divider class="mt-2"></v-divider>
    </template>
  </v-select>
</template>

<script>
import {mapGetters} from "vuex";

  export default {
    event: ['membersChange'],
    data: function () {
      return {
        select: []
      }
    },
    computed: {
      ...mapGetters(['READERS']),
      likesAll () {
        return this.select.length === this.READERS.length
      },
      likesSome () {
        return this.select.length > 0 && !this.likesAll
      },
      icon () {
        if (this.likesAll) return 'mdi-close-box'
        if (this.likesSome) return 'mdi-minus-box'
        return 'mdi-checkbox-blank-outline'
      },
    },
    methods: {
      toggle () {
        this.$nextTick(() => {
          if (this.likesAll) {
            this.select = []
          } else {
            this.select = this.READERS.slice()
          }
        })
      },
    },
    watch: {
      select: function (val) {
        let members = []

        val.forEach((member) => {
          members.push(member)
        })

        this.$emit('membersChange', members)
      },
    }
  }
</script>

<style scoped>

</style>