import Vue from 'vue'
import Vuex from 'vuex'

import projects from './modules/projects';
import project from './modules/project';
import role from './modules/role';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        projects, project, role
    },
    state: {
        profile: frontendData.profile,
        creatorBool: false,
        snackbar: {showing: false, text: null, color: null}
    },
    getters: {
        PROFILE: state => state.profile,
        CREATOR_BOOL (state) {
            try {
                return state.creatorBool = project.state.object.project.user.id === state.profile.id
            }
            catch (err) {
                return false
            }
        }
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