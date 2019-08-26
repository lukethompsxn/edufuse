<template>
    <div>
        <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
    </div>
</template>

<script>
    import {Chart} from 'highcharts-vue'
    import {messageBus} from '../../../main.js';
    import fs from 'fs';

    // let mount = '';

    export default {
        name: "RnWThroughput",
        components: {
            highcharts: Chart,
        },

        data() {
            return {
                title: '',
                points: [0, 0],
                chartType: 'Column',
                seriesColor: '#6fcd98',
                colorInputIsSupported: null,
                animationDuration: 500,
                updateArgs: [true, true, {duration: 1000}],

                chartOptions: {
                    chart: {
                        type: 'column',
                        backgroundColor: 'transparent',
                        height: 216,
                        margin: [20, 0, 50, 60],
                        // inverted: true,
                    },
                    title: {
                        text: null
                    },
                    credits: false,
                    xAxis: {
                        categories: ['read', 'write'],
                        title: {
                        }
                    },
                    yAxis: {
                        title: {
                            minPadding: 0,
                            maxPadding: 0,
                            text: "Bytes"
                        }
                    },
                    legend: {
                        enabled: false,
                        itemStyle: {
                            fontSize: '10px'
                        }
                    },
                    series: [{
                        name: 'System Call',
                        data: [0, 0],
                        color: '#6fcd98',
                    }],
                },
                mount: '/tmp/example'
            }
        },

        methods: {
            updateValues(call, path) {
                let stats;
                if (path !== undefined) {
                    stats = fs.statSync(this.mount + path);
                }

                // Temp solution
                if (call !== undefined && call === 'read') this.points[0]+= stats.size;
                else if (call !== undefined && call === 'write') {
                    this.points[1]+= stats.size;
                }

                this.updateSeries(this.points);

            },
            updateSeries(newValue) {
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },
            reset: function () {
                this.points = [0, 0];
                this.updateSeries(this.points);
            }
        },

        created: function () {
            messageBus.$on('READ_WRITE', (json) => {
                //this.updateValues(json.amount);
                this.updateValues(json.syscall, json.file);
            });
            messageBus.$on('MOUNT', (json) => {
                if (json.dir !== null && json.dir !== '') {
                    this.mount = json.dir;
                }
            });
        },

        mounted() {
            this.logHistory.forEach((log) => {
                this.updateValues(log.syscall);
            });

            this.mount = this.mountPoint;
        },
    }
</script>

<style scoped>

</style>