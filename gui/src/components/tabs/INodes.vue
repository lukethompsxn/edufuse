<template>
     <b-container class="bv-example-row">
            <div>
                <b-modal id="beta" title="Warning">
                    <p class="my-4">This feature is only in beta.</p>
                </b-modal>
            </div>
            <b-row class="top card">
                <div class="title">
                    <span>INode Table.</span>
                </div>
                <div>
                    <tree-view style="max-height: 200px" class="tree" :data="iNodeTable"></tree-view>
                </div>
            </b-row>
            <b-row class="bottom card">
                <div class="title">
                    <span>Block File.</span>
                </div>
                <div>
                    <tree-view style="max-height: 200px" class="tree" :data="blockFileView" :options="{maxDepth: 2}"></tree-view>
                </div>
            </b-row>
        </b-container>
</template>

<script>
    import vuescroll from 'vuescroll';
    import {messageBus} from '../../main.js';
    import fs from 'fs';

    export default {
        name: "INodes",
        components: {
            vuescroll
        },
        data() {
            return {
                iNodeTable: ['File System not running, or no iNode Table'],
                blockFileView: ['File System not running, or no block file'],
                mount: "/tmp/example",
            }
        },
        methods: {
            updateBlockFile(json) {
                console.log(typeof json);
                console.log(Object.values(json));
                console.log(json["Blocks"]);

                let key;
                for(key in json["Blocks"]) {
                    if(json["Blocks"].hasOwnProperty(key)) {
                        let value = json["Blocks"][key];
                        console.log(key);
                        //do something with value;
                        console.log(this.mount + value["File"]);
                        let buffer = fs.readFileSync(this.mount + value["File"]);
                        console.log(buffer.toString());
                        value["Contents"] = buffer;
                    }
                }
            }
        },
        created: function () {
            messageBus.$on('INODE_TABLE', (json) => {
                this.iNodeTable = json;
                // this.updateBlockFile();
            });
            messageBus.$on('BLOCK_FILE', (json) => {
                this.blockFileView = json;
                this.updateBlockFile(json);
            });
            messageBus.$on('MOUNT', (json) => {
                if (json.dir !== null && json.dir !== '') {
                    this.mount = json.dir;
                }
            });
        },
        mounted: function () {
           this.$bvModal.show('beta');
           this.iNodeTable = this.iNodes;
           this.blockFileView = this.blockFile;
        }
    }
</script>

<style scoped>
    .top {
        margin: 16px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 252px
    }

    .bottom {
        margin: 8px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 274px;
    }

    .bv-example-row {
        padding: 0 !important;
    }

    .tree {
        margin-top: 10px;
        color: #232931 !important;
        text-align: left;
    }
</style>
<style src="../../assets/styles/styles.css"></style>