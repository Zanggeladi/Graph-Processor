<!-- MonacoEditor.vue -->
<template>
  <div ref="container" class="monaco-editor-container"></div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import * as monaco from 'monaco-editor';

const container = ref(null);

onMounted(() => {
  monaco.editor.create(container.value, {
    value: 'console.log("Hello, Monaco Editor!");',
    language: 'javascript',
    theme: 'vs-dark'
  });
});
</script>

<style>
.monaco-editor-container {
  height: 600px;
}
</style>
