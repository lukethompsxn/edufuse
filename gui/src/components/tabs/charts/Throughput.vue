<template>
    <b-container class="bv-example-row" style="height: 100%">
        <b-row style="height: 100%;">
            <div>
                <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
            </div>
        </b-row>
        <div id="app">
            <multiselect
                    v-model="value"
                    :options="options"
                    :multiple="true"
                    track-by="syscall"
                    :custom-label="customLabel"
                    :close-on-select="false"
                    :max="7"
                    :max-height="300"
                    @select="onSelect"
                    @remove="onRemove"
            >
                <template slot="selection" slot-scope="{ values, search, isOpen }"><span class="multiselect__single" v-if="values.length > 3 &amp;&amp; !isOpen">{{ values.length }} options selected</span></template>
            </multiselect>
            <!--<pre>{{ chartOptions.series[0].data }}</pre>-->
        </div>
    </b-container>
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
                        backgroundColor: 'transparent'
                    },
                    title: {
                        text: 'Count of System Calls'
                    },
                    credits: false,
                    xAxis: {
                        categories: ['getattr', 'readdir', 'open', 'read']
                    },
                    yAxis: {
                        title: {
                            text: 'Number of Calls'
                        }
                    },
                    series: [{
                        data: [0, 0, 0, 0],
                        color: '#6fcd98',
                    }]
                },

                value: [
                    { syscall: 'getattr', display: true },
                    { syscall: 'readdir', display: true },
                    { syscall: 'open', display: true },
                    { syscall: 'read', display: true },
                ],
                options: [
                    { syscall: 'getattr', display: true },
                    { syscall: 'readdir', display: true },
                    { syscall: 'open', display: true },
                    { syscall: 'read', display: true },
                    { syscall: 'rename', display: false },
                    { syscall: 'unlink', display: false },
                    { syscall: 'rmdir', display: false },
                    { syscall: 'symlink', display: false },
                    { syscall: 'link', display: false },
                    { syscall: 'release', display: false },
                    { syscall: 'write', display: false },
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
        },

        created: function () {
            messageBus.$on('LOG', (json) => {
                this.updateValues(json.syscall);

            });
        },

        watch: {
            points () {
                // this.chartOptions.series[0].data = newValue;
            },
        }
    }
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>