<template>
    <b-container class="bv-example-row" style="height: 100%">
        <b-row style="height: 100%;">
            <div>
                <highcharts :options="chartOptions" :updateArgs="updateArgs" ref="chart"></highcharts>
            </div>
        </b-row>
        <!--<b-row>-->
            <!--<div v-for="index in 4" :key="index">-->
                <!--<p>{{index}}</p>-->
                <!--<input v-model.number="points[index-1]" type="number">-->
            <!--</div>-->
        <!--</b-row>-->
        <div id="app">
            <multiselect
                    v-model="value"
                    :options="options"
                    :multiple="true"
                    track-by="language"
                    :custom-label="customLabel"
                    :close-on-select="false"
                    :max="7"
                    @select="onSelect"
                    @remove="onRemove"
            >
                    <template slot="selection" slot-scope="{ values, search, isOpen }"><span class="multiselect__single" v-if="values.length > 3 &amp;&amp; !isOpen">{{ values.length }} options selected</span></template>
            </multiselect>
            <pre>{{ points }}</pre>
            <pre>{{ chartOptions.series[0].data }}</pre>
            <pre>{{ options }}</pre>

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
                points: [0, 0, 0, 0],
                chartType: 'Column',
                seriesColor: '#6fcd98',
                colorInputIsSupported: null,
                animationDuration: 1000,
                updateArgs: [true, true, {duration: 1000}],
                xAxis: ['getattr', 'readdir', 'open', 'read'],
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
                },

                value: null,
                options: [
                    { language: 'getattr', display: false },
                    { language: 'readdir', display: false },
                    { language: 'open', display: false },
                    { language: 'read', display: false },
                ],
            }
        },
        methods: {
            updateValues(msg) {
                if (msg !== undefined && msg === 'getattr') this.points[0]++;
                else if (msg !== undefined && msg === 'readdir') this.points[1]++;
                else if (msg !== undefined && msg === 'open') this.points[2]++;
                else if (msg !== undefined && msg === 'read') this.points[3]++;

                let temp = [];
                let tempCat = [];

                for (let i = 0; i < this.points.length; i++) {
                    if (this.options[i].display === true) {
                        temp.push(this.points[i]);
                        tempCat.push(this.xAxis[i]);
                    } //this doesn't work

                }

                this.updateSeries(temp, tempCat);
            },
            updateSeries(newValue, newCat) {
                this.chartOptions.xAxis.categories = newCat;
                this.chartOptions.series[0].data = newValue;
                this.points.push();
            },

            customLabel (option) {
                // return `${option.library} - ${option.language}`
                return `${option.language}`
            },
            onSelect(option) { //loop through each option
                if (option.language === 'getattr') this.options[0].display = true;
                if (option.language === 'readdir') this.options[1].display = true;
                if (option.language === 'open') this.options[2].display = true;
                if (option.language === 'read') this.options[3].display = true;
            },
            onRemove(option) {
                if (option.language === 'getattr') this.options[0].display = false;
                if (option.language === 'readdir') this.options[1].display = false;
                if (option.language === 'open') this.options[2].display = false;
                if (option.language === 'read') this.options[3].display = false;
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

<!--<style scoped>-->
    <!---->

<!--</style>-->
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>