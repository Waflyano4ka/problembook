import axios from "axios";

const resourceApi = '/api/projects'

const state = {
    projects: []
}

const getters = {
    PROJECTS (state) {
        return state.projects
            .filter(project => project.project.active)
            .sort(function(a, b) {
            return (b.liked - a.liked) || (b.project.name < a.project.name) - (a.project.name < b.project.name)
        })
    },
    ARCHIVED_PROJECTS (state) {
        return state.projects
            .filter(project => !project.project.active)
            .sort(function(a, b) {
                return (b.project.name < a.project.name) - (a.project.name < b.project.name)
            })
    },
}

const actions = {
    async GET_PROJECTS_FORM_DB({ commit }) {
        try {
            const response = await axios.get(resourceApi)
            commit('SET_PROJECTS_TO_STATE', response.data)
        } catch (error) {
            console.error(error)
        }
    },
    async ADD_PROJECT_TO_DB({ commit }, project) {
        try {
            const response = await axios.post(resourceApi, project)
            commit('ADD_PROJECT_TO_STATE', response.data)
            await this.dispatch('SET_SNACKBAR', {
                text: "Был создан новый проект",
                color: "success"
            })
        } catch (error) {
            console.error(error)
        }
    },
    async JOIN_PROJECT({ commit }, keyToConnect) {
        try {
            const response = await axios.get(resourceApi + '/' + keyToConnect.key)
            commit('ADD_PROJECT_TO_STATE', response.data)
            await this.dispatch('SET_SNACKBAR', {
                text: "Вы присоединились к проекту",
                color: "success"
            })
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "warning"
            })
        }
    },
    async LIKE_PROJECT({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id +'/like')
            commit('UPDATE_PROJECT_TO_STATE', response.data)
        } catch (error) {
            console.error(error)
        }
    },
    async ARCHIVE_PROJECT({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id +'/archive')
            commit('UPDATE_PROJECT_TO_STATE', response.data, id)
            if (response.data.project.active) {
                await this.dispatch('SET_SNACKBAR', {
                    text: "Проект был восстановлен",
                    color: "info"
                })
            }
            else {
                await this.dispatch('SET_SNACKBAR', {
                    text: "Проект был архивирован",
                    color: "info"
                })
            }
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async LEAVE_PROJECT({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id +'/leave')
            commit('DELETE_PROJECT_FROM_STATE', response.data.id)
            await this.dispatch('SET_SNACKBAR', {
                text: "Вы покинули проект \"" + response.data.name + "\"",
                color: "info"
            })
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
}

const mutations = {
    SET_PROJECTS_TO_STATE: (state, projects) => state.projects = projects,
    ADD_PROJECT_TO_STATE: (state, project) => state.projects.push(project),
    UPDATE_PROJECT_TO_STATE (state, project) {
        const index = state.projects.findIndex(item => item.id === project.id)
        state.projects.splice(index, 1, project)
    },
    DELETE_PROJECT_FROM_STATE (state, id) {
        const index = state.projects.findIndex(item => item.id === id)
        state.projects.splice(index, 1)
    }
}

export default {
    state, getters, actions, mutations
}

