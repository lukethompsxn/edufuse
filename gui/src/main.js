import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import {ipcRenderer} from 'electron';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Buefy from 'buefy';
import TreeView from 'vue-json-tree-view'


import Charts from './components/tabs/Charts'
import Logger from './components/tabs/Logger'
import Directory from './components/tabs/Directory'
import INodes from './components/tabs/INodes'

Vue.use(TreeView);
Vue.use(VueRouter);
Vue.use(Buefy);
Vue.use(BootstrapVue);

export const messageBus = new Vue({});

const routes = [
    {path: '/Charts', component: Charts},
    {path: '/Logger', component: Logger},
    {path: '/Directory', component: Directory},
    {path: '/INodes', component: INodes},
    {path: '/', redirect: '/Charts'}
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
    '/.DS_STORE',
    '/.DS_Store'
];
Vue.prototype.logHistory = [];
Vue.prototype.iNodes = ['File System not running, or no iNode Table'];
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

ipcRenderer.on('CALL_INFO', (event, json) => {
    if (!ignorePaths.includes(json.file)) {
        Vue.prototype.logHistory.push(json);
        messageBus.$emit('CALL_INFO', json);
    }
});

ipcRenderer.on('MOUNT', (event, json) => {
    messageBus.$emit('MOUNT', json);
});

ipcRenderer.on('READ_WRITE', (event, json) => {
    messageBus.$emit('READ_WRITE', json);
});

ipcRenderer.on('INODE_TABLE', (event, json) => {
    Vue.prototype.iNodes = json;
    messageBus.$emit('INODE_TABLE', json);
});