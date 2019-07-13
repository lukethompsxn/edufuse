<!--Courtesy of https://github.com/robogeek/vue-file-tree (Modified)-->

<template>
    <span>
    <sl-vue-tree
            id="vue-file-tree"
            ref="slvuetree"
            :value="nodes"
            :allowMultiselect="false"
            @nodeclick="nodeClick"
            @nodedblclick="nodeDoubleClick"
            @select="nodeSelect"
            @toggle="nodeToggle"
            @drop="nodeDrop"
            @nodecontextmenu="nodeContextMenu"
            @externaldrop.prevent="onExternalDropHandler">

        <template slot="toggle" slot-scope="{ node }">
            <span v-if="!node.isLeaf">
                <font-awesome-icon
                        icon="caret-right"
                        v-if="!node.isExpanded"></font-awesome-icon>
                <font-awesome-icon
                        icon="caret-down"
                        v-else-if="node.isExpanded"></font-awesome-icon>
            </span>
        </template>

        <template slot="title" slot-scope="{ node }">
            <font-awesome-icon
                    :icon="[ 'fab', 'js' ]"
                    v-if='node.data.type === "application/javascript"'></font-awesome-icon>
            <font-awesome-icon
                    icon="table"
                    v-else-if='node.data.type === "application/json"'></font-awesome-icon>
            <font-awesome-icon
                    icon="image"
                    v-else-if='node.data.type === "IMAGE"'></font-awesome-icon>
            <font-awesome-icon
                    icon="code"
                    v-else-if='node.data.type === "EJS"'></font-awesome-icon>
            <font-awesome-icon
                    :icon="[ 'fab', 'vuejs' ]"
                    v-else-if='node.data.type === "VUEJS"'></font-awesome-icon>
            <font-awesome-icon
                    icon="file"
                    v-else-if="node.isLeaf"></font-awesome-icon>
            {{ node.title }} </template>


        <template slot="sidebar" slot-scope="{ node }">
            <font-awesome-icon
                    icon="circle"
                    v-if="node.data.isModified"></font-awesome-icon>
        </template>
    </sl-vue-tree>


    <aside class="menu vue-file-tree-contextmenu"
           ref="contextmenu"
           v-show="contextMenuIsVisible">
        <slot name="context-menu"></slot>
    </aside>

    </span>
</template>

<script>
    import path from 'path';
    import util from 'util';
    // import splitter from './path-splitdirs';
    import mime from 'mime';
    import slVueTree from 'sl-vue-tree';
    import {library} from '@fortawesome/fontawesome-svg-core';
    import {
        faCaretRight,
        faCaretDown,
        faTable,
        faImage,
        faFile,
        faCircle,
        faCode
    } from '@fortawesome/free-solid-svg-icons';
    import {faJs, faVuejs} from '@fortawesome/free-brands-svg-icons';
    import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';

    const normalize = require('normalize-path');
    const parsePath = require('parse-filepath');

    library.add(faJs, faVuejs, faCaretRight, faCaretDown, faTable, faImage, faFile, faCircle, faCode);

    export default {
        name: 'vue-file-tree',
        data() {
            return {
                nodes: [],
                contextMenuIsVisible: false,
                isWin32: false,
                isPosix: true,
            }
        },
        components: {
            'sl-vue-tree': slVueTree,
            'font-awesome-icon': FontAwesomeIcon
        },
        created() {
            /*
             * Derived from Buefy's b-dropdown
             * https://github.com/buefy/buefy/blob/dev/src/components/dropdown/Dropdown.vue
             */
            if (typeof window !== 'undefined') {
                document.addEventListener('click', this.clickedOutside)
            }
        },
        methods: {
            nodeClick(node, event) {
                this.$emit('nodeClick', event, node);
            },
            nodeDoubleClick(node, event) {
                if (!node.isLeaf) {
                    this.$refs.slvuetree.onToggleHandler(event, node);
                    return;
                }
                this.$emit('nodeDoubleClick', node);
            },
            nodeSelect(node) {
            },
            nodeToggle(node) {
            },
            nodeDrop(node) {
                this.$emit('nodeDrop', node);
            },
            nodeContextMenu(node, event) {
                this.contextMenuIsVisible = true;
                const $contextMenu = this.$refs.contextmenu;
                $contextMenu.style.left = event.clientX + 'px';
                $contextMenu.style.top = event.clientY + 'px';
            },
            /**
             * Close dropdown if clicked outside.
             * Derived from Buefy's b-dropdown
             * https://github.com/buefy/buefy/blob/dev/src/components/dropdown/Dropdown.vue
             */
            clickedOutside(event) {
                if (!this.isInWhiteList(event.target)) this.contextMenuIsVisible = false;
            },
            // If the "clickOutside" is on a target where we should ignore the click
            // then we should ignore this.
            // See: https://github.com/buefy/buefy/blob/dev/src/components/dropdown/Dropdown.vue
            isInWhiteList(el) {
                return false;
            },
            onExternalDropHandler(cursorPosition, event) {
                console.log('external drop', cursorPosition, util.inspect(event));
            },
            addPathToTree(fn, stat, isDir) {
                fn = path.normalize(fn);
                const basenm = path.basename(fn);
                const split = this.splitter(fn);
                let curnodes = this.nodes;
                for (let dir of split.dirs) {
                    if (dir === '.') continue;
                    let found = undefined;
                    for (let cur of curnodes) {
                        if (cur.isLeaf === false && cur.title === dir) {
                            found = cur;
                            break;
                        }
                    }
                    if (!found) {
                        let newnode = {
                            title: dir,
                            isLeaf: false,
                            children: [],
                            data: {
                                type: "DIRECTORY",
                                pathname: fn,
                                stat
                            }
                        };
                        curnodes.push(newnode);
                        curnodes = newnode.children;
                    } else {
                        curnodes = found.children;
                    }
                }
                let newnode = {
                    title: basenm,
                    isLeaf: !isDir,
                    data: {
                        type: mime.getType(fn),
                        pathname: fn,
                        stat
                    }
                };
                if (!newnode.data.type) newnode.data.type = "text/plain";
                if (newnode.data.type.startsWith('image/')) newnode.data.type = "IMAGE";
                if (fn.endsWith('.ejs')) newnode.data.type = "EJS";
                if (fn.endsWith('.vue')) newnode.data.type = "VUEJS";
                if (!newnode.isLeaf) newnode.children = [];
                curnodes.push(newnode);
            },
            clearTree() {
                this.nodes = [];
            },
            splitter(path2split) {
                path2split = normalize(path2split);
                if (path2split.match(/^[a-zA-Z]\:/)) {
                    this.isWin32 = true;
                    this.isPosix = false;
                }
                if (path2split.indexOf('\\') >= 0) {
                    this.isWin32 = true;
                    this.isPosix = false;
                }
                let zpath = this.isWin32 ? path.win32 : path.posix;
                if (!zpath) zpath = path;
                let parsed = zpath.parse ? zpath.parse(path2split) : parsePath(path2split);
                if (parsed.root === '') parsed.root = '.';
                let dir = parsed.dir;
                let dirz = [];
                do {
                    dirz.unshift(zpath.basename(dir));
                    dir = zpath.dirname(dir);
                } while (dir !== parsed.root);
                parsed.dirs = dirz;
                return parsed;
            }
        }
    }

</script>

<style>

    .vue-file-tree-contextmenu {
        position: absolute;
        background-color: white;
        color: black;
        border-radius: 2px;
        cursor: pointer;
    }

    .vue-file-tree-contextmenu > div {
        padding: 10px;
    }

    .vue-file-tree-contextmenu > div:hover {
        background-color: rgba(100, 100, 255, 0.5);
    }

    #vue-file-tree {
        height: 100%;
    }

    .sl-vue-tree {
        position: relative;
        cursor: default;
        user-select: none;
    }

    .sl-vue-tree.sl-vue-tree-root {
        background-color: transparent;
        border: 0;
        border-radius: 0;
        color: #232931;
    }

    .sl-vue-tree-root > .sl-vue-tree-nodes-list {
        overflow: hidden;
        position: relative;
        padding-bottom: 4px;
    }

    .sl-vue-tree-selected > .sl-vue-tree-node-item {
        /*if we need to override the selected property*/
        background-color: #8eabb0;
        color: #232931;
    }

    .sl-vue-tree-node-item:hover {
        color: #232931;
    }

    .sl-vue-tree-node-item {
        position: relative;
        display: flex;
        flex-direction: row;

        padding-left: 10px;
        padding-right: 10px;
        line-height: 28px;
        border: 1px solid transparent;
    }


    .sl-vue-tree-node-item.sl-vue-tree-cursor-inside {
        border: 1px solid rgba(255, 255, 255, 0.5);
    }

    .sl-vue-tree-gap {
        width: 25px;
        min-height: 1px;

    }

    .sl-vue-tree-toggle {
        display: inline-block;
        text-align: left;
        width: 20px;
    }

    .sl-vue-tree-sidebar {
        margin-left: auto;
    }

    .sl-vue-tree-cursor {
        position: absolute;
        border: 1px solid rgba(255, 255, 255, 0.5);
        height: 1px;
        width: 100%;
    }

    .sl-vue-tree-drag-info {
        position: absolute;
        background-color: rgba(0, 0, 0, 0.5);
        opacity: 0.5;
        margin-left: 20px;
        padding: 5px 10px;
    }

</style>