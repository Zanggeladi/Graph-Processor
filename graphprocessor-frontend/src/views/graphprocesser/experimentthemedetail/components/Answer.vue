<template>
  <div class="catalog">
    <bytemd v-model="answer" :mode="'preview'" />
  </div>
</template>

<script setup>
import {computed} from 'vue';
import {Bytemd} from '@bytemd/vue-next';
import '@bytemd/vue-next/dist/index.css'; // 引入 Bytemd 样式

// 组件属性
const props = defineProps({
  answer: {
    type: String,
    required: true,
  },
});

const answer = computed(() => props.answer);
</script>

<style scoped>
.catalog {
  padding: 0 20px;
  max-height: calc(100vh - 132px);
  overflow: auto;
}

.catalog .title {
  font-size: 1.125rem;
  line-height: 1.75rem;
}
</style>
