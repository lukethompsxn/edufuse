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
    import {ipcRenderer} from 'electron';


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
                        margin: [20, 0, 60, 51],

                        // Need to enter real data in this function
                        events: {
                            load: function() {
                                // set up the updating of the chart each second
                                // let series = this.series;
                                let chart = this;

                                setInterval(function() {
                                    let x = new Date().getTime();
                                    let y = 0;
                                    let y2 = 0;

                                    let data = fs.readFileSync("/tmp/.readwrites.json");
                                    if (data !== undefined) {
                                        let parsedData = JSON.parse(data);
                                        console.log('read: ' + parsedData["read"] + ' write: ' + parsedData["write"]);
                                        chart.series[0].addPoint([x, parsedData["read"]], false, true);
                                        chart.series[1].addPoint([x, parsedData["write"]], true, true);

                                        let obj = {};
                                        obj.read = 0;
                                        obj.write = 0;

                                        let jsonString = JSON.stringify(obj);
                                        fs.writeFileSync("/tmp/.readwrites.json", jsonString);
                                    } else {
                                        chart.series[0].addPoint([x, y], false, true);
                                        chart.series[1].addPoint([x, y2], true, true);
                                    }
                                }, 3500);
                            }
                        },
                        height: 216

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
                            text: "Bytes"
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
                    legend: {
                        itemStyle: {
                            fontSize: '10px'
                        }
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
                                        x: time + i * 3500,
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
                                        x: time + i * 3500,
                                        y: 0
                                    });
                                }
                                return data1;
                            })()
                        }
                    ]
                },
                // mount: '/tmp/example'
            };
        },

        methods: {
            updateValues(call, path) {
                // let stats;
                // if (path !== undefined) {
                //     stats = fs.statSync(this.mount + path);
                // }
                //
                // let data = fs.readFileSync("/tmp/.readwrites.json");
                // if (data !== undefined) {
                //     let parsedData = JSON.parse(data);
                //     this.dataReadCurrent = parsedData["read"];
                //     this.dataWrittenCurrent = parsedData["write"];
                // }
                //
                // // Temp solution
                // if (call !== undefined && call === 'read') this.dataReadCurrent += stats.size;
                // else if (call !== undefined && call === 'write') this.dataWrittenCurrent += stats.size;
                // else return;
                //
                // let obj = {};
                // obj.read = this.dataReadCurrent;
                // obj.write = this.dataWrittenCurrent;
                //
                // let jsonString = JSON.stringify(obj);
                // fs.writeFileSync("/tmp/.readwrites.json", jsonString);

                // this.updateSeries();

            },
            updateSeries() {
                // this.chartOptions.series[0].data = newValue;
                // this.points.push();
            },
            test() {
            }
        },

        created: function () {
            messageBus.$on('READ_WRITE', (json) => {
                // Maybe need threads for these?
                // this.updateValues(json.syscall, json.file);
                ipcRenderer.send('timeline', json.syscall, json.file);
            });
            let obj = {};
            obj.read = this.dataReadCurrent;
            obj.write = this.dataWrittenCurrent;

            let jsonString = JSON.stringify(obj);
            fs.writeFileSync("/tmp/.readwrites.json", jsonString);
        },

        mounted() {
            this.logHistory.forEach((log) => {
                this.updateValues(log.syscall);
            });
            // this.mount = this.mountPoint;

        },
    };
</script>
