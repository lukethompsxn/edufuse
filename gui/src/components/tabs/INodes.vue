<template>
     <b-container class="bv-example-row">
            <b-row class="top card">
                <div class="title">
                    <span>INode Table.</span>
                </div>
                <div>
                    <tree-view style="max-height: 480px" class="tree" :data="iNodeTable"></tree-view>
                </div>
            </b-row>
        </b-container>
</template>

<script>
    import {messageBus} from '../../main.js';

    export default {
        name: "INodes",
        data() {
            return {
                iNodeTable: ['File System not running, no iNode Table, or no write call made yet'],
            }
        },
        created: function () {
            messageBus.$on('INODE_TABLE', (json) => {
                this.iNodeTable = json;
            });
        },
        mounted: function () {
           this.iNodeTable = this.iNodes;
        }
    }
</script>

<style scoped>
    .top {
        margin: 16px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 542px
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