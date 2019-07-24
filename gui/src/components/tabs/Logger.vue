<template>
    <b-container class="bv-example-row" v-on:keydown.capture.prevent.stop>
        <b-row class="top card">
            <div class="title">
                <span>Options.</span>
            </div>
            <b-form-group class="options">
                <b-form-checkbox-group
                        id="checkbox-group-1"
                        v-model="selected"
                        :options="options"
                        name="flavour-1"
                        switches
                ></b-form-checkbox-group>
            </b-form-group>
        </b-row>
        <b-row class="bottom card">
            <div class="title">
                <span>Output.</span>
            </div>
            <VueTerminal console-sign="$"
                         height="375px"
                         ref="terminal">
            </VueTerminal>
        </b-row>
    </b-container>
</template>

<script>
    import VueTerminal from '../../ext_components/VueTerminal'
    import {messageBus} from '../../main.js';

    let first = true;

    export default {
        name: "Logger",
        components: {
            VueTerminal
        },
        data() {
            return {
                selected: ['syscall', 'file', 'fileInfo'],
                options: [
                    {text: 'System Calls', value: 'syscall'},
                    {text: 'File/Directory', value: 'file'},
                    {text: 'File/Directory Information', value: 'fileInfo'},
                ]
            }
        },
        methods: {
            logMessage(msg) {
                let terminal = this.$refs.terminal;
                if (msg !== undefined && terminal !== undefined) this.$refs.terminal.echo("$ " + msg);
            },
            handleJSON(json) {
                if (!first) {
                    this.logMessage('');
                } else {
                    first = false;
                }

                if (this.selected.includes('syscall')) {
                    this.logMessage('SYSCALL: ' + json.syscall);
                }
                if (this.selected.includes('file')) {
                    this.logMessage('PATH: ' + json.file);
                }
                if (this.selected.includes('fileInfo')) {
                    this.logMessage('INFO: ' + JSON.stringify(json.fileInfo, null, 4));
                }
            }
        },
        created: function () {
            messageBus.$on('LOG', (json) => {
                this.handleJSON(json);
            });
        },
        mounted() {
            this.logHistory.forEach((log) => {
                this.handleJSON(log);
            });
        }
    }
</script>

<style scoped>
    .top {
        margin: 16px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 85px;
    }

    .bottom {
        margin: 8px 16px 16px 16px;
        padding: 0 15px 0 15px;
        height: 442px;
    }

    .bv-example-row {
        padding: 0 !important;
    }

    .options {
        color: #232931;
        font-family: 'Montserrat', sans-serif;
        line-height: 24px;
        vertical-align: middle;
        padding-top: 9px;
    }

    .form-group {
        margin-bottom: 0 !important;
    }

</style>
<style src="../../assets/styles/styles.css"></style>