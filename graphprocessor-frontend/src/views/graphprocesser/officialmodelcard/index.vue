<template>
  <div class="app-container">
    <!-- 卡片列表 -->
    <el-row :gutter="20" v-if="officialmodelList.length > 0">
      <el-col
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          v-for="(model, index) in officialmodelList"
          :key="model.id"
      >
        <el-card
            shadow="hover"
            class="custom-card"
            @mouseenter="hover = index"
            @mouseleave="hover = null"
        >
          <div slot="header" class="card-header">
            <span>{{ model.name }}</span>
            <el-tag
                v-if="model.subtask"
                type="warning"
                class="subtask-tag"
            >{{ model.subtask }}</el-tag>
          </div>
          <div class="card-content">
            <p class="description">{{ model.shortDescription }}</p>
            <div class="tag-container">
              <el-tag
                  v-for="tag in model.tags"
                  :key="tag"
                  :type="randomTagType()"
                  class="custom-tag"
              >
                {{ tag }}
              </el-tag>
            </div>
            <router-link
                v-if="model.type === 0"
                :to="`/officialmodel/stream/${model.id}`"
            >
              <el-button class="custom-button">查看详情</el-button>
            </router-link>
            <router-link
                v-else
                :to="`/officialmodel/nonstream/${model.id}`"
            >
              <el-button class="custom-button">查看详情</el-button>
            </router-link>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 加载动画 -->
    <div v-else>
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { listAllOfficialmodel } from "@/api/graphprocesser/officialmodel.js";

const officialmodelList = ref([]); // 存储模型列表
const hover = ref(null); // 控制悬停效果

/** 查询官方模型列表 */
function getList() {
  listAllOfficialmodel().then((response) => {
    officialmodelList.value = response.data.map((item) => ({
      ...item,
      tags: item.tags ? item.tags.split(",").filter((tag) => tag) : [],
    }));
  });
}

// 随机选择标签颜色
function randomTagType() {
  const types = ["success", "info", "warning", "danger"];
  return types[Math.floor(Math.random() * types.length)];
}

// 初次加载数据
getList();
</script>

<style scoped>
/* 卡片的基本样式 */
.custom-card {
  margin-bottom: 15px;
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  background: linear-gradient(135deg, #f3f3f3 0%, #e9e9e9 100%);
  position: relative;
  height: 280px;
}

/* 卡片悬停效果 */
.custom-card:hover {
  transform: scale(1.02);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.15);
}

/* 标题样式 */
.card-header {
  font-weight: bold;
  color: #333;
  font-size: 18px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 内容文字样式 */
.card-content {
  padding: 10px;
  padding-bottom: 20px;
}

.description {
  font-size: 14px;
  line-height: 1.5;
  height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  white-space: normal;
}

.tag-container {
  min-height: 87px;
}

/* 标签样式 */
.custom-tag {
  margin-right: 5px;
  margin-bottom: 5px;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

.custom-tag:hover {
  background-color: #409eff;
  color: white;
}

/* subtask 标签样式 */
.subtask-tag {
  margin-left: 10px;
}

/* 按钮样式 */
.custom-button {
  background-image: linear-gradient(135deg, #42b983, #2c7d5b);
  border: none;
  transition: background-color 0.3s, box-shadow 0.3s;
  float: right;
  z-index: 2;
  position: relative;
  padding: 8px 16px;
  height: 36px;
  line-height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
  margin-bottom: 10px;
}

.custom-button:hover {
  background-color: #36a073;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
  color: white;
}

/* 移除伪元素以防覆盖内容 */
.custom-card::before {
  display: none;
}
</style>
