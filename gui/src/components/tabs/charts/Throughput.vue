<template>
    <div>
        <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
        <multiselect
                style="font-size: 10px"
                v-model="value"
                :tabindex="-1"
                :options="options"
                :multiple="true"
                track-by="syscall"
                :custom-label="customLabel"
                :close-on-select="false"
                :max="7"
                :max-height="300"
                @select="onSelect"
                @remove="onRemove"
                :option-height="10">
            <template slot="selection" slot-scope="{ values, search, isOpen }"><span class="multiselect__single" v-if="values.length > 3 &amp;&amp; !isOpen">{{ values.length }} options selected</span></template>
        </multiselect>
    </div>
</template>

<script>
    import {Chart} from 'highcharts-vue'
    import {messageBus} from '../../../main.js';
    import Multiselect from 'vue-multiselect'

    export default {
        name: "Throughput",
        components: {
            highcharts: Chart,
            Multiselect
        },

        data() {
            return {
                title: '',
                points: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                chartType: 'Column',
                seriesColor: '#6fcd98',
                height: 200,
                colorInputIsSupported: null,
                animationDuration: 500,
                updateArgs: [true, true, {duration: 1000}],

                sysCalls: ['getattr', 'readdir', 'open', 'read', 'rename', 'unlink', 'rmdir', 'symlink', 'link', 'release',
                    'write', 'fsync', 'flush', 'statfs', 'opendir', 'fsyncdir', 'releasedir', 'create', 'lock', 'chmod',
                    'chown', 'truncate', 'utimens', 'access', 'readlink', 'mknod', 'mkdir', 'setxattr', 'getxattr', 'removexattr',
                    'bmap', 'ioctl', 'poll'],

                chartOptions: {
                    chart: {
                        type: 'column',
                        backgroundColor: 'transparent',
                        height: 180,
                        margin: [20, 0, 30, 60],

                    },
                    title: {
                        text: null
                    },
                    credits: false,
                    xAxis: {
                        categories: ['getattr', 'read', 'write', 'open']
                    },
                    yAxis: {
                        title: {
                            text: 'Number of Calls'
                        }
                    },
                    legend: {
                        enabled: false,
                        itemStyle: {
                            fontSize: '10px'
                        }
                    },
                    series: [{
                        style: {
                            font: '2000pt',
                        },
                        name: 'System Call',
                        data: [0, 0, 0, 0],
                        color: '#6fcd98',
                    }]
                },

                value: [
                    { syscall: 'getattr', display: true },
                    { syscall: 'read', display: true },
                    { syscall: 'write', display: true },
                    { syscall: 'open', display: true },
                ],
                options: [
                    { syscall: 'getattr', display: true },
                    { syscall: 'readdir', display: false },
                    { syscall: 'open', display: true },
                    { syscall: 'read', display: true },
                    { syscall: 'rename', display: false },
                    { syscall: 'unlink', display: false },
                    { syscall: 'rmdir', display: false },
                    { syscall: 'symlink', display: false },
                    { syscall: 'link', display: false },
                    { syscall: 'release', display: false },
                    { syscall: 'write', display: true },
                    { syscall: 'fsync', display: false },
                    { syscall: 'flush', display: false },
                    { syscall: 'statfs', display: false },
                    { syscall: 'opendir', display: false },
                    { syscall: 'fsyncdir', display: false },
                    { syscall: 'releasedir', display: false },
                    { syscall: 'create', display: false },
                    { syscall: 'lock', display: false },
                    { syscall: 'chmod', display: false },
                    { syscall: 'chown', display: false },
                    { syscall: 'truncate', display: false },
                    { syscall: 'utimens', display: false },
                    { syscall: 'access', display: false },
                    { syscall: 'readlink', display: false },
                    { syscall: 'mknod', display: false },
                    { syscall: 'mkdir', display: false },
                    { syscall: 'setxattr', display: false },
                    { syscall: 'getxattr', display: false },
                    { syscall: 'listxattr', display: false },
                    { syscall: 'removexattr', display: false },
                    { syscall: 'bmap', display: false },
                    { syscall: 'ioctl', display: false },
                    { syscall: 'poll', display: false },

                ],
            }
        },

        methods: {
            updateValues(msg) {

                for (const call of this.sysCalls) {
                    if (msg !== undefined && msg === call) {
                        let index = this.sysCalls.indexOf(call);
                        this.points[index]++;
                    }
                }

                let temp = [];
                let tempCat = [];

                for (let i = 0; i < this.points.length; i++) {
                    if (this.options[i].display === true) {
                        temp.push(this.points[i]);
                        tempCat.push(this.sysCalls[i]);
                    }

                }

                this.updateSeries(temp, tempCat);
            },
            updateSeries(newValue, newCat) {
                this.chartOptions.xAxis.categories = newCat;
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },
            customLabel (option) {
                return `${option.syscall}`
            },
            onSelect(option) {
                for(const call of this.sysCalls) {
                    if (option.syscall === call) {
                        let index = this.sysCalls.indexOf(call);
                        this.options[index].display = true;
                    }
                }

                this.updateValues();
            },
            onRemove(option) {
                for(const call of this.sysCalls) {
                    if (option.syscall === call) {
                        let index = this.sysCalls.indexOf(call);
                        this.options[index].display = false;
                    }
                }

                this.updateValues();
            },
            reset: function () {
                this.points = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
                this.updateValues();
            }
        },

        created: function () {
            messageBus.$on('CALL_INFO', (json) => {
                if (json.syscall !== "read" && json.syscall !== "write") {
                    this.updateValues(json.syscall);
                }
            });
            messageBus.$on('READ_WRITE', (json) => {
                this.updateValues(json.syscall);

            });
        },

        activated: function () {
            this.updateValues();
        },

        mounted() {
            this.logHistory.forEach((log) => {
                this.updateValues(log.syscall);
            });
        },
    }
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>