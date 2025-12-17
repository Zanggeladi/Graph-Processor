<template>
  <div class="catalog" :style="{ maxHeight: maxHeight }">
    <div class="header">
      <h1 class="name">{{ theme?.themeName }}</h1>
      <el-divider />
    </div>

    <!-- Markdown 编辑器预览 using custom Viewer component -->
    <div class="content">
<!--      <Viewer :value="theme.description" :plugins="plugins" />-->
      <MdPreview :editorId="id" :modelValue="theme.description" preview-theme="cyanosis" />
<!--      <MdCatalog :editorId="id" :scrollElement="scrollElement" />-->
    </div>

    <!-- 底部信息 -->
    <div class="footer">
      © DBIS 图数据处理分析平台
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Viewer } from '@bytemd/vue-next';

import { MdPreview, MdCatalog } from 'md-editor-v3';
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css';
const id = 'preview-only';
const scrollElement = document.documentElement;

// Define component props
const props = defineProps({
  theme: {
    type: Object,
    required: true,
  },
  maxHeight: {
    type: String,
    default: 'calc(80vh - 132px)',
  }
});

onMounted(() => {
  console.log("theme", props.theme);
});

// Plugin array
const plugins = [
  gfm(),
  highlight(),
  // Additional plugins can be added here
];
</script>

<style scoped>
.catalog {
  padding: 20px;
  background-color: #f9fafb;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: auto;
  font-family: Arial, sans-serif;
}

.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 12px;
}

.catalog .name {
  font-size: 1.25rem;
  line-height: 1.75rem;
  font-weight: bold;
  color: #333;
}

.content {
  margin-top: 12px;
  color: #4a4a4a;
}

.footer {
  margin-top: 20px;
  text-align: center;
  font-size: 12px;
  color: #888;
  border-top: 1px solid #eaeaea;
  padding-top: 10px;
}

::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 8px;
}

::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 8px;
}
</style>
