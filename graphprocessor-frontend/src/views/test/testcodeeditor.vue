<template>
  <div>
    <h2>代码编辑器示例</h2>
    <el-button type="primary" @click="addLine">添加一行代码</el-button>
    <Editor
        :code="code"
        :setCode="updateCode"
        :language="language"
        :setLanguage="updateLanguage"
        :coderHeight="'400px'"
    />
  </div>
</template>

<script setup>
import {ref} from 'vue';
import Editor from "@/views/graphprocesser/experimentthemedetail/components/Editor.vue";
// 初始代码内容
const code = ref(`console.log('Hello, world!');\n`);

// 当前语言
const language = ref('javascript');

// 更新代码内容的函数
const updateCode = (newCode) => {
  code.value = newCode;
};

// 更新语言的函数
const updateLanguage = (newLanguage) => {
  language.value = newLanguage;
};

// 每次点击按钮时向编辑器添加一行代码
const addLine = () => {
  code.value += `console.log('新的一行代码');\n`;
};
</script>
