<template>
    <section class="charts">
        <vue-highcharts :options="chartOptions" ></vue-highcharts>
    </section>
</template>
<script>
    import VueHighcharts from "vue2-highcharts";
    import Highcharts from "highcharts";
    import {messageBus} from '../../../main.js';
    import fs from 'fs';

    export default {
        components: {
            VueHighcharts
        },
        data() {
            return {
                dataReadCurrent: 0,
                dataWrittenCurrent: 0,

                chartOptions: {
                    chart: {
                        type: "areaspline",
                        animation: Highcharts.svg, // don't animate in old IE
                        marginRight: 10,
                        backgroundColor: 'transparent',

                        // Need to enter real data in this function
                        events: {
                            load: function() {
                                // set up the updating of the chart each second
                                let series = this.series;

                                setInterval(function() {
                                    let x = new Date().getTime(), // current time
                                        y = Math.random(), // replace
                                        y2 = Math.random(); // replace
                                    // y = this.dataReadCurrent;
                                    // y2 = this.dataWrittenCurrent;
                                    // this.dataReadCurrent = 0;
                                    // this.dataWrittenCurrent = 0;

                                    series[0].addPoint([x, y], false, true);
                                    series[1].addPoint([x, y2], true, true);
                                }, 2500);
                            }
                        }
                    },
                    title: {
                        text: null
                    },
                    credits: false,
                    tooltip: {
                        formatter: function() {
                            return (
                                "<b>" +
                                this.series.name +
                                "</b><br/>" +
                                Highcharts.dateFormat("%Y-%m-%d %H:%M:%S", this.x) +
                                "<br/>" +
                                Highcharts.numberFormat(this.y, 2)
                            );
                        }
                    },
                    xAxis: {
                        type: "datetime",
                        tickPixelInterval: 150
                    },
                    yAxis: {
                        title: {
                            text: "Value"
                        },
                        softMax: 1,
                        min: 0,
                        plotLines: [
                            {
                                value: 0,
                                width: 1,
                                color: "#808080"
                            }
                        ]
                    },
                    // Will start empty
                    series: [
                        {
                            name: 'Reads',
                            data: (function() {
                                // generate an array of random data
                                let data = [],
                                    time = new Date().getTime(),
                                    i;

                                for (i = -19; i <= 0; i += 1) {
                                    data.push({
                                        x: time + i * 2500,
                                        y: 0
                                    });
                                }
                                return data;
                            })()
                        },
                        {
                            name: 'Writes',
                            data: (function() {
                                // generate an array of random data
                                let data1 = [],
                                    time = new Date().getTime(),
                                    i;

                                for (i = -19; i <= 0; i += 1) {
                                    data1.push({
                                        x: time + i * 2500,
                                        y: 0
                                    });
                                }
                                return data1;
                            })()
                        }
                    ]
                },
            };
        },

        methods: {
            updateValues(msg) {
                let stats = fs.statSync('/tmp/example/file');

                // Temp solution
                if (msg !== undefined && msg === 'read') {
                    //increase current
                    this.dataReadCurrent += stats.size;
                }
                else if (msg !== undefined && msg === 'write') {
                    //increase current
                    this.dataWrittenCurrent += stats.size;

                }

                this.updateSeries(this.points);

            },
            updateSeries(newValue) {
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },
        },

        created: function () {
            messageBus.$on('READ_WRITE', (json) => {
                // Maybe need threads for these?
                this.updateValues(json.syscall);
            });
        },

        mounted() {
            this.logHistory.forEach((log) => {
                this.handleJSON(log);
            });
        },
    };
</script>
