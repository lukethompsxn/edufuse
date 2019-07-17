<template>
    <b-container class="bv-example-row" style="height: 100%">
        <b-row style="height: 100%;">
            <div>
                <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
            </div>
            <div v-for="index in 4" :key="index">
                <p>{{index}}</p>
                <input v-model.number="points[index-1]" type="number">
            </div>
        </b-row>
    </b-container>
</template>

<script>
    import {Chart} from 'highcharts-vue'
    import {messageBus} from '../../../main.js';

    export default {
        name: "Throughput",
        components: {
            highcharts: Chart
        },
        data() {
            return {
                title: '',
                points: [0, 0, 0, 0],
                chartType: 'Column',
                seriesColor: '#6fcd98',
                colorInputIsSupported: null,
                animationDuration: 1000,
                updateArgs: [true, true, {duration: 1000}],
                chartOptions: {
                    chart: {
                        type: 'column',
                        backgroundColor: 'transparent'
                    },
                    title: {
                        text: 'Test Chart'
                    },
                    credits: false,
                    xAxis: {
                        categories: ['getattr', 'readdir', 'open', 'read']
                    },
                    series: [{
                        data: [0, 0, 0, 0],
                        color: '#6fcd98',
                    }]
                }
            }
        },
        methods: {
            updateValues(msg) {
                if (msg !== undefined && msg === 'getattr') this.points[0]++;
                else if (msg !== undefined && msg === 'readdir') this.points[1]++;
                else if (msg !== undefined && msg === 'open') this.points[2]++;
                else if (msg !== undefined && msg === 'read') this.points[3]++;

                //this.points.push();
                this.updateSeries(this.points);

                console.log(msg);
            },
            updateSeries(newValue) {
                console.log(newValue);
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },
        },
        created: function () {
            messageBus.$on('LOG', (json) => {
                this.updateValues(json.syscall);

            });
        },
        watch: {
            points (newValue) {
                this.chartOptions.series[0].data = newValue;
            },
        }
    }
</script>

<style scoped>

</style>