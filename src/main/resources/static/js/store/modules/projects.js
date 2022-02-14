import axios from "axios";

const resourceApi = '/api/projects'

const state = {
    projects: []
}

const getters = {
    PROJECTS: state => state.projects
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
            commit('ADD_PROJECT_TO_DB', response.data)
        } catch (error) {
            console.error(error)
        }
    }
}

const mutations = {
    SET_PROJECTS_TO_STATE: (state, projects) => state.projects = projects,
    ADD_PROJECT_TO_DB: (state, project) => state.projects.push(project)
}

export default {
    state, getters, actions, mutations
}

