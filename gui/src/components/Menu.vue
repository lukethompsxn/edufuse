<head>
    <meta charset="UTF-8">
    <title>eduFUSE</title>
</head>

<template>
    <div class="tab">
        <img class="logo" src="../assets/logo.png">
        <div class="tabs">
            <div class="tablinks active" v-on:click="selectTab($event, 'Charts')">
                <i class="material-icons icons">show_chart</i>
                <i class="text">CHARTS</i>
            </div>
            <div class="tablinks" v-on:click="selectTab($event, 'Directory')">
                <i class="material-icons icons">folder</i>
                <i class="text">DIRECTORY</i>
            </div>
            <div class="tablinks" v-on:click="selectTab($event, 'Logger')">
                <i class="material-icons icons">developer_mode</i>
                <i class="text">LOGGER</i>
            </div>
            <div class="tablinks" v-on:click="selectTab($event, 'INodes')">
                <i class="material-icons icons">apps</i>
                <i class="text">INODES</i>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Menu',
        methods: {
            selectTab(event, tab) {
                let i, tablinks;
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }

                event.target.parentElement.className += " active";

                this.$emit('tab-selected', '/' + tab);
            }
        }
    }
</script>
<style src="../../node_modules/material-design-icons/iconfont/material-icons.css"></style>
<style src="../assets/styles/styles.css"></style>