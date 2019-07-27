<template>
    <b-container height="100%">
        <b-row class="">
            <b-col class="charttop card">
                <div class="">
                    <Throughput></Throughput>
                </div>
            </b-col>
        </b-row>
        <b-row class="">
            <b-col class="left card">
                <div>
                    <RnWThroughput></RnWThroughput>
                </div>
            </b-col>
            <b-col class="right card">
                <div>
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
</style>