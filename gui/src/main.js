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
import Console from './components/tabs/Console'
import Directory from './components/tabs/Directory'
import Settings from './components/tabs/Settings'

Vue.use(VueRouter);
Vue.use(Buefy);
Vue.use(BootstrapVue);

export const messageBus = new Vue({});

const routes = [
    {path: '/Overview', component: Overview},
    {path: '/Charts', component: Charts},
    {path: '/Console', component: Console},
    {path: '/Settings', component: Settings},
    {path: '/Directory', component: Directory},
    {path: '/', redirect: '/Overview'}
];

const router = new VueRouter({
    routes: routes,
});

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
    console.log('clear nodes called');
    messageBus.$emit('clear-nodes');
});