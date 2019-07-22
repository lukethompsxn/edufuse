<template>
  <div id="app">
    <Menu id="menu" @tab-selected="updateWindow"/>
      <keep-alive>
          <router-view class="window"></router-view>
      </keep-alive>
  </div>
</template>

<script>
  import Menu from './components/Menu'

  export default {
    name: 'app',
    components: {
      Menu,
      Window
    },
    methods: {
      updateWindow(tab) {
        this.$router.push(tab);
          console.log('tab pushed: ' + tab);
      }
    },
      mounted() {
          this.$router.options.routes.forEach(route => {
              this.$router.push(route.path);
              console.log('pre loaded: ' + route.path);
          });
          this.$router.push('/');
      }
  }

</script>
<style>
  @import url('https://fonts.googleapis.com/css?family=Montserrat&display=swap');
</style>
<style>
  body, html {
    height: 100%;
    margin: 0;
    font-family: 'Montserrat', sans-serif;
    text-align: center;
    background-color: #E7EBEE !important;
  }

  #app {
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #ffffff;
    margin-top: 0;
    height: 100%;
  }

  #menu {
    width: 125px;
    height: 100%;
    background-color: #232931;
    display: inline-block;
    float: left;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  }

  .window {
    display: inline-block;
    float: left;
  }
</style>
