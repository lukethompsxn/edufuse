import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import {ipcRenderer} from 'electron';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Buefy from 'buefy';

import Overview from './components/tabs/Overview'
import Charts from './components/tabs/Charts'
import Logger from './components/tabs/Logger'
import Directory from './components/tabs/Directory'
import Settings from './components/tabs/Settings'

Vue.use(VueRouter);
Vue.use(Buefy);
Vue.use(BootstrapVue);

export const messageBus = new Vue({});

const routes = [
    {path: '/Overview', component: Overview},
    {path: '/Charts', component: Charts},
    {path: '/Logger', component: Logger},
    {path: '/Settings', component: Settings},
    {path: '/Directory', component: Directory},
    {path: '/', redirect: '/Overview'}
];

const router = new VueRouter({
    routes: routes,
});

const ignorePaths = [
    '/',
    './',
    '.',
    '.hidden',
    '/.hidden',
    '/.Trash',
    '/.Trash-100',
    '/.Trash-1000',
    '/.localized',
    '/.DS_STORE'
];
Vue.prototype.logHistory = [];
Vue.config.productionTip = false;


new Vue({
    router: router,
    render: h => h(App)
}).$mount('#app');

ipcRenderer.on('file', (event, fn, stat) => {
    messageBus.$emit('file', fn, stat);
});

ipcRenderer.on('directory', (event, fn, stat) => {
    messageBus.$emit('directory', fn, stat);
});

ipcRenderer.on('clear-nodes', () => {
    messageBus.$emit('clear-nodes');
});

ipcRenderer.on('LOG', (event, json) => {
    if (!ignorePaths.includes(json.file)) {
        Vue.prototype.logHistory.push(json);
        messageBus.$emit('LOG', json);
    }
});

ipcRenderer.on('MOUNT', (event, json) => {
    messageBus.$emit('MOUNT', json);
});

ipcRenderer.on('READ_WRITE', (event, json) => {
    messageBus.$emit('READ_WRITE', json);
});