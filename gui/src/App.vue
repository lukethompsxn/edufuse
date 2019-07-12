<template>
  <div id="app">
    <Menu id="menu" @tab-selected="updateWindow"/>
    <router-view class="window"></router-view>
  </div>
</template>

<script>
  import Menu from './components/Menu'
  import * as net from 'net';

  const port = 8081;
  const host = '127.0.0.1';

  let server = net.createServer(function(socket) {
    socket.on('data', function (data) {
      let str = data.toString();
      console.log(str);
      try {
        let json = JSON.parse(str);
        console.log(json);
      } catch (e) {
        console.log('error str: ' + str);
      }

    });
  });

  server.listen(port, host);

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
