import VueRouter from 'vue-router'

import MainPage from '../pages/tabs/MainPage.vue'
import AllProjectsPage from '../pages/tabs/AllProjectsPage.vue'

export default new VueRouter({
    routes: [
        {
            path: '/',
            component: MainPage
        },
        {
            path: '/projects',
            component: AllProjectsPage
        }
    ]
})