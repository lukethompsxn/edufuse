import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import Overview from './components/tabs/Overview'
import Charts from './components/tabs/Charts'
import Console from './components/tabs/Console'
import Settings from './components/tabs/Settings'

Vue.use(VueRouter);

const routes = [
  {path: '/Overview', component: Overview},
  {path: '/Charts', component: Charts},
  {path: '/Console', component: Console},
  {path: '/Settings', component: Settings},
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