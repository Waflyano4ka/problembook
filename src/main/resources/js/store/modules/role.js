import axios from "axios";

const resourceApi = '/api/roles'

function findByName(roleName) {
    let index = -1
    for(let i = 0; i < state.roles.length; i++) {
        if (state.roles[i].name === roleName){
            index = i
        }
    }

    return index
}

const state = {
    roles: [],
    changeUserRoles: {
        data: []
    }
}

const getters = {
    CHANGE_ROLE_AMOUNT: state => state.changeUserRoles.data.length,
    ROLES (state)
    {
        let nameRoles = [];
        state.roles.forEach((role) => {
            nameRoles.push(role.name)
        })

        return nameRoles
    },
}

const actions = {
    async GET_ROLES_FORM_DB({ commit }) {
        try {
            const response = await axios.get(resourceApi)
            commit('SET_ROLES_TO_STATE', response.data)
        } catch (err) {
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
    async CHANGE_USER_ROLE({ commit }, projectID){
        try {
            let stringJSON = JSON.parse(JSON.stringify(state.changeUserRoles))
            const response = await axios.put(resourceApi + '/' + projectID + '/change', stringJSON)
            try {
                commit('UPDATE_MEMBERS_TO_STATE', response.data)
                await this.dispatch('SET_SNACKBAR', {
                    text: "Роли успешно изменены",
                    color: "success"
                })

                state.changeUserRoles.data = []
            }
            catch (e) {
                console.error(e)
            }
        } catch (err) {
            console.error(err.response.data)
            await this.dispatch('SET_SNACKBAR', {
                text: err.response.data,
                color: "error"
            })
        }
    },
}

const mutations = {
    SET_ROLES_TO_STATE: (state, roles) => state.roles = roles,
}

export default {
    state, getters, actions, mutations
}