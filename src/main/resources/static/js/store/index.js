import Vue from 'vue'
import Vuex from 'vuex'

import projects from './modules/projects';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        projects
    },
    state: {
        profile: frontendData.profile,
        snackbar: {showing: false, text: null, color: null}
    },
    getters: {
    },
    mutations: {
        SET_SNACKBAR_TO_STATE: (state, snackbar) => state.snackbar = snackbar
    },
    actions: {
        SET_SNACKBAR({commit}, snackbar){
            snackbar.showing = true;
            commit('SET_SNACKBAR_TO_STATE', snackbar)
        }
    }
})