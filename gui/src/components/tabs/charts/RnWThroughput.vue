<template>
    <div>
        <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
    </div>
</template>

<script>
    import {Chart} from 'highcharts-vue'
    import {messageBus} from '../../../main.js';
    import fs from 'fs';

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
                            text: "Bytes Read/Written"
                        }
                    },
                    series: [{
                        name: 'System Call',
                        data: [0, 0],
                        color: '#6fcd98',
                    }]
                },
            }
        },

        methods: {
            updateValues(msg) {
                let stats = fs.statSync('/tmp/example/file');

                // Temp solution
                if (msg !== undefined && msg === 'read') this.points[0]+= stats.size;
                else if (msg !== undefined && msg === 'write') this.points[1]+= stats.size;

                this.updateSeries(this.points);

            },
            updateSeries(newValue) {
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },
        },

        created: function () {
            messageBus.$on('READ_WRITE', (json) => {
                //this.updateValues(json.amount);
                this.updateValues(json.syscall);
            });
        },

        mounted() {
            this.logHistory.forEach((log) => {
                this.updateValues(log.syscall);
            });
        },
    }
</script>

<style scoped>

</style>