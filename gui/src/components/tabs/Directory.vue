<template>
    <b-container class="bv-example-row" style="height: 100%">
        <b-row style="height: 100%;">
            <b-col class="left card">
                <div class="title">
                    <span>Directory Contents.</span>
                </div>
                <div id="wrapper" class="columns is-gapless is-mobile">
                    <file-browser-tree
                            id="file-tree"
                            ref="filetree"
                            class="column content"
                            @nodeClick="nodeClick">
                    </file-browser-tree>
                </div>
            </b-col>
            <b-col class="right card">
                <div class="title">
                    <span>File Information.</span>
                </div>
                <div class="fileinfo">
                    <table class="content">
                        <tr>
                            <th>Filename:</th>
                            <td>{{node.data.pathname}}</td>
                        </tr>
                        <tr>
                            <th>Created:</th>
                            <td>{{node.data.stat.ctime}}</td>
                        </tr>
                        <tr>
                            <th>Access:</th>
                            <td>{{node.data.stat.atime}}</td>
                        </tr>
                        <tr>
                            <th>Modified:</th>
                            <td>{{node.data.stat.mtime}}</td>
                        </tr>
                        <tr>
                            <th>Size:</th>
                            <td>{{node.data.stat.size === '' ? '' : node.data.stat.size + ' bytes' }}</td>
                        </tr>
                        <tr>
                            <th>Mode:</th>
                            <td>{{node.data.stat.mode}}</td>
                        </tr>
                    </table>
                </div>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>

    const path = require('path');
    const util = require('util');
    import {messageBus} from '../../main.js';
    import {ipcRenderer} from 'electron';

    import FileBrowserTree from 'vue-file-tree';

    const blankNode = {
        data: {
            pathname: '',
                stat: {
                ctime: '',
                    atime: '',
                    mtime: '',
                    size: '',
                    mode: ''
            }
        }
    };

    export default {
        name: 'file-browser-main',
        components: {
            'file-browser-tree': FileBrowserTree,
        },
        data() {
            return {
                fileInfo: "",
                nodes: [],
                node: blankNode
            }
        },
        methods: {
            nodeClick(event, node) {
                if (node.level === 1) {
                    this.node = blankNode;

                } else {
                    this.node = node;
                }
            },
            rescan() {
                this.nodes = [];
                ipcRenderer.send('rescan-directory');
            }
        },
        created: function () {
            messageBus.$on('file', (fn, stat) => {
                this.$refs.filetree.addPathToTree(fn, stat, false);
            });
            messageBus.$on('directory', (fn, stat) => {
                this.$refs.filetree.addPathToTree(fn, stat, true);
            });
            messageBus.$on('clear-nodes', () => {
               this.nodes = [];
            });

            this.nodes = [];
            ipcRenderer.send('rescan-directory');
        }
    }


</script>

<style>

    body {
        height: 100%;
        font-size: 13px;
    }

    .sl-vue-tree.sl-vue-tree-root {
        background-color: transparent !important;
        border: 0 !important;
        border-radius: 0 !important;
        color: #232931 !important;
    }

    .sl-vue-tree-selected > .sl-vue-tree-node-item {
        /*if we need to override the selected property*/
        background-color: #8eabb0 !important;
        color: #232931 !important;
    }

    .sl-vue-tree-node-item:hover {
        color: #232931 !important;
    }

    #wrapper {
        display: inline;
        float: left;
        height: 100%;
        width: auto;
        background-color: #ffffff;
        padding-top: 10px;
    }

    #file-tree {
        height: 100%;
        overflow: scroll;
    }

    .fileinfo {
        display: inline-flex;
        align-content: flex-end;
        float: right;
        background-color: #ffffff;
        /*margin-left: 10px;*/
        padding-top: 10px;
    }

    .content {
        color: #232931;
        width: auto;
        height: 100%;
    }

    .card {
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    }

    .left {
        margin: 16px 8px 16px 16px;
    }

    .right {
        margin: 16px 16px 16px 8px;
    }

    th {
        text-align: right;
        padding-right: 10px;
    }

    td {
        text-align: left;
    }

    .title {
        text-align: left;
        line-height: 35px;
        border-bottom: 1px solid #232931;
        color: #232931;
        vertical-align: middle;
        font-family: 'Montserrat', sans-serif;
        font-size: 15px;
    }
</style>