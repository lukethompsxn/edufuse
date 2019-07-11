<template>
    <p>charts placeholder</p>
</template>

<script>
    import * as net from 'net';
    const port = 8081;
    const host = '127.0.0.1';

    let server = net.createServer(function(socket) {
        socket.on('data', function(data){
            let str = data.toString();
            console.log(str);
            try {
                let json = JSON.parse(str);
                console.log(json);
            } catch (e) {
                console.log('error str: ' + str);
            }

        });
        socket.on('error', function(err) {
            console.log(err)
        })
    });

    server.listen(port, host);

    export default {
        name: "Charts"
    }
</script>

<style scoped>

</style>