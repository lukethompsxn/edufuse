<template>
    <b-container height="100%">
        <b-row>
            <b-col class="left card top">
                <div class="title">
                    <span>Total Bytes Read/Written.</span>
                </div>
                <div class="content">
                    <RnWThroughput></RnWThroughput>
                </div>
            </b-col>
            <b-col class="right card top">
                <div class="title">
                    <span>Read/Writes Per Second.</span>
                </div>
                <div class="content">
                    <TimeLine></TimeLine>
                </div>
            </b-col>
        </b-row>
        <b-row>
            <b-col class="lower card bottom">
                <div class="title">
                    <span>System Calls.</span>
                </div>
                <div class="content">
                    <Throughput></Throughput>
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
    .lower {
        margin: 0 16px 16px 16px;
    }

    .content {
        margin-top: 10px;
    }

    .top {
        height: 254px
    }

    .bottom {
        height: 276px;
    }
</style>