<template>
    <div>
        <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
    </div>
</template>

<script>
    import {Chart} from 'highcharts-vue'
    import {messageBus} from '../../../main.js';
    import {ipcRenderer} from 'electron';


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
                updateArgs: [true, false, {duration: 1000}],

                chartOptions: {
                    chart: {
                        type: 'column',
                        backgroundColor: 'transparent',
                        height: 216,
                        margin: [20, 0, 50, 60],
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
            }
        },

        methods: {
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
                ipcRenderer.send('read-write', json.syscall, json.file);
            });
            messageBus.$on('read1', (read_size) => {
                this.points[0] += read_size;
                this.updateSeries(this.points);
            });
            messageBus.$on('write1', (write_size) => {
                this.points[1] += write_size;
                this.updateSeries(this.points);
            });
        },

        activated: function () {
            this.updateSeries(this.points);
        },

        mounted() {
            this.logHistory.forEach((log) => {
            });
            this.updateSeries(this.points);
        },
    }
</script>

<style scoped>

</style>