import Vue from 'vue'
import Vuetify from 'vuetify'
import VueRouter from 'vue-router'
import App from './pages/App.vue'

import 'vuetify/dist/vuetify.min.css'
import router from './router/index'
import store from './store/index'
import colors from 'vuetify/lib/util/colors'

Vue.use(Vuetify)
Vue.use(VueRouter)

export default new Vuetify({
    theme: {
        themes: {
            light: {
                primary: colors.orange.lighten4,
                secondary: colors.orange.lighten2,
                accent: colors.orange.accent3,
                shadow: colors.amber.lighten5,
                redAccent: colors.red.lighten1,
                success: colors.lightGreen.darken1
            },
            dark: {
                primary: colors.purple .darken4,
                secondary: colors.purple .darken2,
                accent: colors.purple .accent2,
                shadow: "#241d2a",
                redAccent: colors.red.accent3,
            }
        },
    },
})

new Vue({
    vuetify : new Vuetify(),
    render: a => a(App),
    el: '#app',
    router,
    store
})