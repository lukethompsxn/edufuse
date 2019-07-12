<template>
    <div>
        <highcharts :options="chartOptions"></highcharts>
    </div>
</template>

<script>
    import {Chart} from 'highcharts-vue'

    export default {
        name: "Throughput",
        components: {
            highcharts: Chart
        },
        data() {
            return {
                chartOptions: {
                    series: [{
                        data: [1,2,3] // sample data
                    }],
                    credits: false,
                    chart: {
                        backgroundColor: "transparent"
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>