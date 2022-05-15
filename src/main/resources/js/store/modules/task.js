import axios from "axios";
import router from '../../router'

const resourceApi = '/api/task'

const state = {
    currentTaskRole: null,
    currentTaskUser: null,
    task: null,
}

const getters = {
    TASK: state => state.task,
    CURRENT_TASK_ROLE: state => state.currentTaskRole,
    CURRENT_TASK_USER: state => state.currentTaskUser,
}

const actions = {
    async DOWNLOAD_FILE({ commit }, filename) {
        try {
            const res = await axios.get(resourceApi + '/file/' + filename, { responseType: 'blob' })
            const downloadUrl = window.URL.createObjectURL(new Blob([res.data]))

            const fileName = filename.substring(filename.indexOf(".") + 1)

            const link = document.createElement('a')
            link.href = downloadUrl
            link.download = decodeURI(fileName)
            document.body.appendChild(link)
            link.click()
            link.remove()

        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err,
                color: "error"
            })
        }
    },
    async UPLOAD_TASK_TO_SERVER({ commit }, form) {
        try {
            let taskUserId = form.taskUserId
            let formData = new FormData()
            formData.append('file', form.file)
            const response = await axios.post(resourceApi + '/' + taskUserId + '/complete',
                formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
            })
            commit('EDIT_TASK_USER_TO_STATE', response.data)
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async COMPLETE_TASK_ON_DB({ commit }) {
        try {
            let taskUserId = state.currentTaskUser.id
            const response = await axios.get(resourceApi + '/' + taskUserId + '/complete')
            commit('EDIT_TASK_USER_TO_STATE', response.data)
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },

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
        state.currentTaskUser = data.taskUsers
    },
    EDIT_TASK_TO_STATE: (state, task) => state.task = task,
    EDIT_TASK_USER_TO_STATE: (state, taskUser) => state.currentTaskUser = taskUser,
}

export default {
    state, getters, actions, mutations
}