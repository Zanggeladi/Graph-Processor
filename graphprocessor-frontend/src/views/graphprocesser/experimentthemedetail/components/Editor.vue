<template>
  <div>
    <!-- 语言选择器 -->
    <el-select
        size="small"
        v-model="language"
        @change="handleLanguageChange"
        style="width: 100px; margin: 8px 0 0 8px"
    >
      <el-option
          v-for="lang in languages"
          :key="lang.value"
          :label="lang.label"
          :value="lang.value"
      />
    </el-select>

    <el-divider style="margin: 8px 0 0 0" />

    <!-- Monaco 编辑器容器 -->
<!--    <div ref="editorContainer" :style="{ height: coderHeight, marginBottom: '2px' }"></div>-->

    <div ref="editorContainer" :style="{ height: coderHeight || '400px', marginBottom: '2px' }"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { ElMessage } from 'element-plus';
import * as monaco from "monaco-editor";

// 定义组件属性
const props = defineProps({
  code: {
    type: String,
    required: true,
  },
  setCode: {
    type: Function,
    required: true,
  },
  language: {
    type: String,
    required: true,
  },
  setLanguage: {
    type: Function,
    required: true,
  },
  coderHeight: {
    type: String,
    required: true,
  },
});

// 定义语言选项
const languages = [
  { value: 'java', label: 'Java' },
  { value: 'cpp', label: 'C++' },
  { value: 'javascript', label: 'JavaScript' },
  { value: 'python', label: 'Python' },
];

// 配置 Monaco 编辑器的选项
const options = {
  automaticLayout: true,
  selectOnLineNumbers: true,
  roundedSelection: false,
  readOnly: false,
  cursorStyle: 'line',
  scrollBeyondLastLine: false,
  scrollbar: {
    horizontalScrollbarSize: 8,
    verticalScrollbarSize: 8,
  },
  fontSize: 13,
  tabSize: 2,
  minimap: {
    enabled: false,
  },
};

// 状态变量
const editorContainer = ref(null);
const language = ref(props.language);
let editorInstance = null;

// 初始化 Monaco 编辑器
onMounted(() => {
  editorInstance = monaco.editor.create(editorContainer.value, {
    value: props.code,
    language: language.value,
    theme: 'vs-dark',
    ...options,
  });

  // 设置内容变化监听
  editorInstance.onDidChangeModelContent(() => {
    props.setCode(editorInstance.getValue());
  });
});

// 清理 Monaco 编辑器实例
onBeforeUnmount(() => {
  if (editorInstance) {
    editorInstance.dispose();
  }
});

// 语言更改处理
const handleLanguageChange = (value) => {
  props.setLanguage(value);
  if (value !== 'python') {
    ElMessage.info('当前仅支持Python语言！');
  }
  monaco.editor.setModelLanguage(editorInstance.getModel(), value);
};

// 监听语言的更改
watch(() => props.language, (newVal) => {
  language.value = newVal;
  if (editorInstance) {
    monaco.editor.setModelLanguage(editorInstance.getModel(), newVal);
  }
});

watch(() => props.code, (newCode) => {
  if (editorInstance && editorInstance.getValue() !== newCode) {
    editorInstance.setValue(newCode);
  }
});
</script>

<style scoped>
/* 在这里添加必要的样式 */
</style>
