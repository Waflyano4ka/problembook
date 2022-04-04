import axios from "axios";
import router from '../../router/index'

const resourceApi = '/api/task'

const state = {
    tasks: []
}

const getters = {
    TASKS: state => state.tasks,
}

const actions = {
    async GET_TASKS_FORM_DB({ commit }, id) {
        try {
            const response = await axios.get(resourceApi + '/' + id)
            commit('SET_TASKS_TO_STATE', response.data)
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async ADD_TASK_TO_DB({ commit }, task) {
        try {
            const idProject = router.currentRoute.params.id
            let stringJSON = JSON.parse(JSON.stringify(task))
            console.log(stringJSON)
            const response = await axios.put(resourceApi + '/' + idProject, stringJSON)
            commit('ADD_TASK_TO_STATE', response.data)
            await this.dispatch('SET_SNACKBAR', {
                text: "Задача опубликована",
                color: "success"
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
    ADD_TASK_TO_STATE: (state, task) => state.tasks.push(task),
    SET_TASKS_TO_STATE: (state, tasks) => state.tasks = tasks,
}

export default {
    state, getters, actions, mutations
}