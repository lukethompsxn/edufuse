<template>
    <b-container class="bv-example-row" style="height: 100%">
        <b-row style="height: 100%;">
            <b-col class="left card">
                <div class="title">
                    <span>Directory Contents.</span>
                </div>
                <div id="wrapper" class="columns is-gapless is-mobile" ref="tree">
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
                            <th>File Mode:</th>
                            <td>{{node.data.stat.mode}}</td>
                        </tr>
                        <tr>
                            <th>Size:</th>
                            <td>{{node.data.stat.size === '' ? '' : node.data.stat.size + ' bytes' }}</td>
                        </tr>
                        <tr>
                            <th>Optimal Block Size:</th>
                            <td>{{node.data.stat.blksize === '' ? '' : node.data.stat.blksize + ' bytes' }}</td>
                        </tr>
                        <tr>
                            <th># Blocks Allocated:</th>
                            <td>{{node.data.stat.blocks === '' ? '' : node.data.stat.blocks + ' blocks of 512-bytes'}}
                            </td>
                        </tr>
                        <tr>
                            <th>File Serial Number:</th>
                            <td>{{node.data.stat.ino}}</td>
                        </tr>
                        <tr>
                            <th>Device:</th>
                            <td>{{node.data.stat.dev}}</td>
                        </tr>
                        <tr>
                            <th>Device Number:</th>
                            <td>{{node.data.stat.rdev}}</td>
                        </tr>
                        <tr>
                            <th>Owner ID:</th>
                            <td>{{node.data.stat.uid}}</td>
                        </tr>
                        <tr>
                            <th>Group ID:</th>
                            <td>{{node.data.stat.gid}}</td>
                        </tr>
                        <tr>
                            <th>Link Count:</th>
                            <td>{{node.data.stat.nlink}}</td>
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
    import FileBrowserTree from '../../ext_components/vue-file-tree';

    const blankNode = {
        data: {
            pathname: '',
            stat: {
                ctime: '',
                atime: '',
                mtime: '',
                mode: '',
                size: '',
                blksize: '',
                blocks: '',
                ino: '',
                dev: '',
                uid: '',
                gid: '',
                nlink: '',
                rdev: '',
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
                node: blankNode,
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
            },
        },
        created: function () {
            messageBus.$on('file', (fn, stat) => {
                this.$refs.filetree.addPathToTree(fn, stat, false);
            });
            messageBus.$on('directory', (fn, stat) => {
                this.$refs.filetree.addPathToTree(fn, stat, true);
            });
            messageBus.$on('clear-nodes', () => {
                if (this.$refs.filetree) this.$refs.filetree.clearTree();
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