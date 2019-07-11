<template>
  <div id="app">
    <Menu id="menu" @tab-selected="updateWindow"/>
    <router-view class="window"></router-view>
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
      }
    }
  }

</script>

<style>
  body, html {
    height: 100%;
    margin: 0;
    font-family: 'Montserrat', sans-serif;
    text-align: center;
    background-color: #2d3134;
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
  }

  .window {
    display: inline-block;
    float: left;
  }
</style>
