import axios from "axios";
import router from '../../router/index'

const resourceApi = '/api/project'

const state = {
    object: {
        project: {},
        members: [],
    },
    search: null,
    pages: ["TASKS", "MEMBERS", "SETTINGS"],
    currentPage: null,
    currentMemberId: null
}

const getters = {
    OBJECT: state => state.object.project,
    MEMBERS: state => state.object.members.filter(member => member.access),
    BLOKED_MEMBERS: state => state.object.members.filter(member => !member.access),
}

const actions = {
    async GET_PROJECT_FORM_DB({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id)
            commit('SET_PROJECT_TO_STATE', response.data)
        } catch (err) {
            await router.replace({ name: 'allProjectsPage'})
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async GET_MEMBERS_FORM_DB({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id + '/members')
            commit('SET_MEMBERS_TO_STATE', response.data)
        } catch (err) {
            await router.replace({ name: 'allProjectsPage'})
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async CHANGE_MEMBER_ACCESS ({ commit }, idProject) {
        try {
            const response = await axios.get(resourceApi + '/' + idProject + '/access/' + state.currentMemberId)
            commit('UPDATE_MEMBER_TO_STATE', response.data)

            if (response.data.access) {
                await this.dispatch('SET_SNACKBAR', {
                    text: "Пользователь: " + response.data.user.name + ", разблокирован",
                    color: "success"
                })
            }
            else {
                await this.dispatch('SET_SNACKBAR', {
                    text: "Пользователь: " + response.data.user.name + ", заблокирован",
                    color: "warning"
                })
            }

        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async DELETE_MEMBER_FROM_DB ({ commit }, idProject) {
        try {
            const response = await axios.delete(resourceApi + '/' + idProject + '/kick/' + state.currentMemberId)
            commit('DELETE_MEMBER_TO_STATE', response.data)

            await this.dispatch('SET_SNACKBAR', {
                text: "Пользователь: " + response.data.user.name + ", покинул данный проект",
                color: "warning"
            })
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    }
}

const mutations = {
    SET_PROJECT_TO_STATE: (state, project) => state.object.project = project,
    SET_MEMBERS_TO_STATE: (state, members) => state.object.members = members,
    UPDATE_MEMBER_TO_STATE (state, member) {
        const index = state.object.members.findIndex(item => item.id === member.id)
        state.object.members.splice(index, 1, member)
    },
    DELETE_MEMBER_TO_STATE (state, member) {
        const index = state.object.members.findIndex(item => item.id === member.id)
        state.object.members.splice(index, 1)
    },
    UPDATE_MEMBERS_TO_STATE (state, members) {
        members.forEach((member) => {
            const index = state.object.members.findIndex(item => item.id === member.id)
            state.object.members.splice(index, 1, member)
        })
    }
}

export default {
    state, getters, actions, mutations
}