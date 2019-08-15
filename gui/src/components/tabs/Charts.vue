<template>
    <b-container height="100%">
        <b-row>
            <b-col class="left card top">
                <div style="position: relative;" class="title">
                    <span>Total Bytes Read/Written.</span>
                    <button type="button" @click="buttonClicked" onclick="this.blur();" tabindex="-1" class="resButton btn btn-outline-secondary btn-sm" style="">Clear</button>
                </div>
                <div class="content">
                    <RnWThroughput ref="rnwThroughput"></RnWThroughput>
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
                    <button type="button" @click="throughputReset" onclick="this.blur();" tabindex="-1" class="resButton btn btn-outline-secondary btn-sm" style="">Clear</button>
                </div>
                <div class="content">
                    <Throughput ref="throughput"></Throughput>
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
        },

        methods: {
            buttonClicked: function () {
                this.$refs.rnwThroughput.reset();
            },
            throughputReset: function () {
                this.$refs.throughput.reset();
            }
        },
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

    .resButton {
        float: right;
        margin-left: -50%;
        margin-top: 2px;
        margin-right: 0em;
    }

    .left {
        margin: 16px 8px 13px 16px;
    }

    .right {
        margin: 16px 16px 13px 8px;
    }
</style>