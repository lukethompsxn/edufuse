<template>
<!--    <div>-->

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
                    <tree-view class="tree" :data="iNodeTable" max-depth="7"></tree-view>
                </div>
            </b-row>
            <b-row class="bottom card">
                <div class="title">
                    <span>Block File.</span>
                </div>
            </b-row>
        </b-container>
<!--    </div>-->
</template>

<script>
    import {messageBus} from '../../main.js';

    export default {
        name: "INodes",
        data() {
            return {
                iNodeTable: ['File System not running, or no iNode Table'],
            }
        },
        methods: {
            updateBlockFile() {

            }
        },
        created: function () {
            messageBus.$on('INODE_TABLE', (json) => {
                this.prototype.iNodeTable = json;
            });
        },
        mounted: function () {
           this.$bvModal.show('beta');
           this.updateBlockFile();
        }
    }
</script>

<style scoped>
    .top {
        margin: 16px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 254px
    }

    .bottom {
        margin: 8px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 276px;
    }

    .bv-example-row {
        padding: 0 !important;
    }

    .tree {
        padding-top: 10px;
        color: #232931 !important;
    }
</style>
<style src="../../assets/styles/styles.css"></style>