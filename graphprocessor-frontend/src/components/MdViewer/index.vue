<template>
  <Viewer :value="value" :plugins="plugins" />
</template>

<script setup>
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Viewer } from "@bytemd/vue-next";
import { defineProps, withDefaults } from "vue";

// 定义组件属性
const props = withDefaults(defineProps(), {
  value: () => "",
});

// 插件数组
const plugins = [
  gfm(),
  highlight(),
  // 你可以在此处添加更多插件
];
</script>

<style></style>
