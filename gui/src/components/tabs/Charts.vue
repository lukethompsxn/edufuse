<template>
    <div>
        <p>charts placeholder</p>
        <Throughput></Throughput>
        <RnWThroughput></RnWThroughput>
        <TimeLine></TimeLine>
    </div>
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

</style>