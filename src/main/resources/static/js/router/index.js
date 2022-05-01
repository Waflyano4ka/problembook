import VueRouter from 'vue-router'

import MainPage from '../pages/tabs/MainPage.vue'
import AllProjectsPage from '../pages/tabs/project/AllProjectsPage.vue'
import ProjectPage from '../pages/tabs/project/ProjectPage.vue'
import ArchivedProjectsPage from "../pages/tabs/archive/ArchivedProjectsPage.vue"
import ArchivePage from "../pages/tabs/archive/ArchivePage.vue"

export default new VueRouter({
    mode: 'history',
    routes: [
        { path: '/', name: 'mainPage', component: MainPage},
        { path: '/projects', name: 'allProjectsPage', component: AllProjectsPage},
        { path: '/projects/:id', name: 'projectPage', component: ProjectPage},
        { path: '/archive', name: 'archivedProjectsPage', component: ArchivedProjectsPage},
        { path: '/archive/:id', name: 'archivePage', component: ArchivePage},
        { path: "*", component: MainPage}
    ]
})