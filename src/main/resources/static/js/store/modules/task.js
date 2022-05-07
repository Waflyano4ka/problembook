import axios from "axios";
import router from '../../router/index'

const resourceApi = '/api/task'

const state = {
    tasks: [],
}

const getters = {
    GROUPTASKS: state => state.tasks,
}

const actions = {
    async CHANGE_DAILY_MESSAGE({ commit }, message) {
        try {
            const idProject = router.currentRoute.params.id
            const response = await axios.put(resourceApi + '/' + idProject + '/daily', {data: message})
            commit('EDIT_PROJECT_TO_STATE', response.data)
        } catch (err) {
            console.error(err)
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async GET_TASKS_FORM_DB({ commit }) {
        try {
            const idProject = router.currentRoute.params.id
            const response = await axios.get(resourceApi + '/' + idProject)
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
    ADD_TASK_TO_STATE (state, task) {
        let index = state.tasks.findIndex(item => item.name === task.taskGroup.name)
        if (index !== -1)
            state.tasks[index].tasks.push(task)
        else {
            let content = { name: task.taskGroup.name, tasks: [] }
            content.tasks.push(task)

            state.tasks.push(content)
        }
    },
    SET_TASKS_TO_STATE: (state, tasks) => state.tasks = tasks,
}

export default {
    state, getters, actions, mutations
}