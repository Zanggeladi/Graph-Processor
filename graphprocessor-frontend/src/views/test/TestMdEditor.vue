<template>
  <div class="test-page">
    <h1>Markdown Viewer 测试页面</h1>
    <!-- 使用 Viewer 组件，传入 markdown 字符串 -->
    <MdEditor :value="testMarkdown" />
  </div>
</template>

<script setup>
// 引入你已经创建的 Markdown Viewer 组件
import MdEditor from '@/components/MdViewer'; // 假设你的组件路径是这样
import { ref } from 'vue';

// 测试用的 Markdown 内容
const testMarkdown = ref(`
# 测试 Markdown 渲染

**粗体文本**

_斜体文本_

- 列表项1
- 列表项2

[链接](https://www.example.com)

\`\`\`javascript
// 代码示例
function hello() {
  console.log("Hello, world!");
}
\`\`\`

> 引用文本

![图片示例](https://www.example.com/image.png)
`);
</script>

<style scoped>
.test-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #409eff;
  text-align: center;
  margin-bottom: 20px;
}
</style>
