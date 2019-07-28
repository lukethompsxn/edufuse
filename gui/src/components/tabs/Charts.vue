<template>
    <b-container height="100%">
        <b-row class="">
            <b-col class="charttop card">
                <div class="title">
                    <span>System Calls.</span>
                </div>
                <div class="content">
                    <Throughput></Throughput>
                </div>
            </b-col>
        </b-row>
        <b-row class="">
            <b-col class="left card">
                <div class="title">
                    <span>Total Bytes Read/Written.</span>
                </div>
                <div class="content">
                    <RnWThroughput></RnWThroughput>
                </div>
            </b-col>
            <b-col class="right card">
                <div class="title">
                    <span>Read/Writes Per Second.</span>
                </div>
                <div class="content">
                    <TimeLine></TimeLine>
                </div>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
    import Throughput from './charts/Throughput';
    import RnWThroughput from "./charts/RnWThroughput";
    import TimeLine from "./charts/TimeLine";

    export default {
        name: "Charts",
        components: {
            RnWThroughput,
            Throughput,
            TimeLine
        }
    }
</script>

<style scoped>
    .charttop {
        margin: 16px 16px 0 16px;
    }

    .content {
        margin-top: 10px;
        margin-bottom: 10px;
    }
</style>