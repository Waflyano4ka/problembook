import Vue from 'vue'
import Vuex from 'vuex'

import projects from './modules/projects';
import project from './modules/project';
import role from './modules/role';
import tasks from './modules/tasks';
import task from './modules/task';
import group from './modules/group';
import user from './modules/user';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        projects, project, role, tasks, task, group, user
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
        },
        CURRENT_ROLE (state) {
            try {
                const index = project.state.object.members.findIndex(item => item.user.id === state.profile.id)
                return project.state.object.members[index].role.name
            }
            catch (e){

            }
        },
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