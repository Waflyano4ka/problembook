import axios from "axios";
import router from '../../router'

const resourceApi = '/api/user'

const state = {

}

const getters = {

}

const actions = {
    async GET_MOBILE_TOKEN({ commit }) {
        try {
            const response = await axios.get(resourceApi + '/mobile')
            await this.dispatch('SET_SNACKBAR', {
                text: `Токен для авторизации в мобильном устройстве: ${response.data}`,
                color: "error"
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

}

export default {
    state, getters, actions, mutations
}