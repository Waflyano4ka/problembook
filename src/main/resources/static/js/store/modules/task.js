import axios from "axios";
import router from '../../router/index'

const resourceApi = '/api/task'

const state = {
    currentTaskRole: null,
    task: null,
}

const getters = {
    TASK: state => state.task,
    CURRENT_TASK_ROLE: state => state.currentTaskRole
}

const actions = {
    async GET_TASK_FORM_DB({ commit }) {
        try {
            const idTask = router.currentRoute.params.id
            const response = await axios.get(resourceApi + '/' + idTask)

            commit('SET_TASK_TO_STATE', response.data)
        } catch (err) {
            await router.replace({ name: 'allProjectsPage'})

            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async EDIT_TASK_TO_DB({ commit }, editTask) {
        try {
            const idTask = router.currentRoute.params.id
            let stringJSON = JSON.parse(JSON.stringify(editTask))
            const response = await axios.put(resourceApi + '/' + idTask + '/edit', stringJSON)
            commit('EDIT_TASK_TO_STATE', response.data)
            await this.dispatch('SET_SNACKBAR', {
                text: "Задача изменена",
                color: "success"
            })
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async DELETE_TASK_FORM_DB({ commit }) {
        try {
            const idTask = router.currentRoute.params.id
            const response = await axios.get(resourceApi + '/' + idTask + '/delete')
            const data = response.data

            const idProject = data.project.id
            await router.push({ name: 'projectPage', params: { id: idProject } })
            commit('DELETE_TASK_FROM_STATE', data)

            await this.dispatch('SET_SNACKBAR', {
                text: `Задача "${data.name}" удалена`,
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
    SET_TASK_TO_STATE (state, data) {
        state.task = data.task
        state.currentTaskRole = data.role
    },
    EDIT_TASK_TO_STATE: (state, task) => state.task = task,
}

export default {
    state, getters, actions, mutations
}