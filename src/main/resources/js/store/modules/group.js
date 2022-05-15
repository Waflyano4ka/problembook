import axios from "axios";
import router from '../../router'

const resourceApi = '/api/group'

const state = {
    groups: [],
}

const getters = {
    GROUPS: state => state.groups,
}

const actions = {
    async GET_GROUPS_FROM_DB_EDIT_TASK({ commit }, idProject){
        try {
            const response = await axios.get(resourceApi + '/' + idProject)
            commit('SET_GROUPS_TO_STATE', response.data)
        } catch (err) {
            if (err.response.data !== "У вас недостаточно прав для получения списка групп")
                await this.dispatch('SET_SNACKBAR', {
                    text: err.response.data,
                    color: "error"
                })
        }
    },
    async GET_GROUPS_FROM_DB({ commit }){
        try {
            const idProject = router.currentRoute.params.id
            const response = await axios.get(resourceApi + '/' + idProject)
            commit('SET_GROUPS_TO_STATE', response.data)
        } catch (err) {
            if (err.response.data !== "У вас недостаточно прав для получения списка групп")
                await this.dispatch('SET_SNACKBAR', {
                    text: err.response.data,
                    color: "error"
                })
        }
    },
    async DELETE_GROUPS_FROM_DB({ commit }, group){
        try {
            const idProject = router.currentRoute.params.id
            const idGroup = group.id
            const response = await axios.get(resourceApi + '/' + idProject + '/delete/' + idGroup)
            commit('DELETE_GROUP_TO_STATE', response.data)

            await this.dispatch('SET_SNACKBAR', {
                text: `Группа "${response.data.name}" была удалена`,
                color: "info"
            })

            router.go(0);
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async ADD_GROUP_TO_DB({ commit }, name) {
        try {
            const idProject = router.currentRoute.params.id
            const response = await axios.post(resourceApi + '/' + idProject, name)
            commit('ADD_GROUP_TO_STATE', response.data)

            await this.dispatch('SET_SNACKBAR', {
                text: `Группа "${response.data.name}" создана`,
                color: "success"
            })
        } catch (err) {
            console.error(err)
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async EDIT_GROUP_TO_DB({ commit }, group) {
        try {
            let stringJSON = JSON.parse(JSON.stringify(group))
            const idProject = router.currentRoute.params.id
            const response = await axios.put(resourceApi + '/' + idProject, stringJSON)
            commit('EDIT_GROUP_TO_STATE', response.data)

            await this.dispatch('SET_SNACKBAR', {
                text: `Название группы изменено на "${response.data.name}"`,
                color: "success"
            })
        } catch (err) {
            console.error(err)
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
}

const mutations = {
    ADD_GROUP_TO_STATE: (state, group) => state.groups.push(group),
    SET_GROUPS_TO_STATE: (state, groups) => state.groups = groups,
    EDIT_GROUP_TO_STATE (state, group) {
        const index = state.groups.findIndex(item => item.id === group.id)
        state.groups.splice(index, 1, group)
    },
    DELETE_GROUP_TO_STATE (state, group) {
        const index = state.groups.findIndex(item => item.id === group.id)
        state.groups.splice(index, 1)
    },
}

export default {
    state, getters, actions, mutations
}