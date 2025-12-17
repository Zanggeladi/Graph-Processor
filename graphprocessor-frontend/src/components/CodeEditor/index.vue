<template>
  <div
      id="code-editor"
      ref="codeEditorRef"
      style="min-height: 400px; width: 100%"
      :style="{ height: props.height }"
  />
</template>

<script setup>
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, defineProps, watch, onBeforeUnmount, onUnmounted } from "vue";

/**
 * 定义组件属性
 */
const props = defineProps({
  value: {
    type: String,
    default: ""
  },
  language: {
    type: String,
    default: "python"
  },
  handleChange: {
    type: Function,
    default: (v) => {
      console.log(v);
    }
  },
  width: {
    type: String,
    default: "50vw"
  },
  height: {
    type: String,
    default: "60vh"
  }
});

const codeEditorRef = ref();
const codeEditor = ref();

// 监听语言的变化
watch(
    () => props.language,
    () => {
      if (codeEditor.value) {
        monaco.editor.setModelLanguage(
            toRaw(codeEditor.value).getModel(),
            props.language
        );
      }
    }
);

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  // 创建 monaco 编辑器
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: props.language,
    automaticLayout: true,
    colorDecorators: true,
    minimap: {
      enabled: true,
    },
    readOnly: false,
    theme: "vs-dark",
  });

  // 监听内容变化
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});

// 在组件销毁时清理编辑器实例
// onBeforeUnmount(() => {
//   console.log("codeEditor.value", codeEditor.value);
//   codeEditor.value.dispose();
//   console.log("销毁了编辑器");
// })
</script>

<style scoped></style>
