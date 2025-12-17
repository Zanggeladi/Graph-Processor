<template>
  <div class="my-chart-page">
    <!-- 搜索框 -->
    <el-input
        v-model="searchParams.name"
        placeholder="请输入任务名称"
        class="search-input"
        clearable
        @keyup.enter.native="onSearch"
    >
      <template #append>
        <el-button :loading="loading" @click="onSearch">搜索</el-button>
      </template>
    </el-input>

    <el-divider />

    <!-- 实验列表 -->
    <el-row :gutter="16">
      <el-col
          v-for="item in experimentList"
          :key="item.id"
          :xs="24"
          :sm="24"
          :md="24"
          :lg="12"
          :xl="12"
          :xxl="12"
      >
        <el-card class="experiment-card" shadow="hover">
          <template #header>
            <span>{{ item.name }}</span>
            <el-button type="primary" @click="openDetailPage(item.id)">详情页</el-button>
          </template>
<!--          <el-avatar :src="currentUser?.userAvatar" />-->
          <div v-if="item.status === 0">
            <el-result status="warning" title="待生成" :sub-title="item.execMessage || '当前任务队列繁忙，请耐心等候'" />
          </div>
          <div v-else-if="item.status === 1">
            <el-result status="info" title="实验结果生成中" :sub-title="item.execMessage" />
          </div>
          <div v-else-if="item.status === 2">
            <img
                :src="COS_HOST + item.resultPath"
                alt="曲线加载失败"
                class="result-image"
            />
          </div>
          <div v-else-if="item.status === 3">
            <el-result status="error" title="图表生成失败" :sub-title="item.execMessage" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <el-pagination
        v-if="total > 0"
        class="pagination"
        background
        layout="prev, pager, next"
        :current-page="searchParams.current"
        :page-size="searchParams.pageSize"
        :total="total"
        @current-change="onPageChange"
        @size-change="onPageSizeChange"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { COS_HOST } from '@/constants';
import {listExperimenttaskByTheme} from "@/api/graphprocesser/experimenttask.js";

// 定义组件属性
const props = defineProps({
  theme: {
    type: Object,
    required: true,
  },
});

const initSearchParams = {
  current: 1,
  pageSize: 4,
  sortField: 'createTime',
  sortOrder: 'desc',
  themeId: props.theme.id,
  name: undefined
};

const searchParams = ref({ ...initSearchParams });
const experimentList = ref([]);
const total = ref(0);
const loading = ref(true);

// 模拟的用户信息，可以用 useUser() 之类的函数替代
const currentUser = ref({ userAvatar: '/path/to/avatar.png' });

const loadData = async () => {
  loading.value = true;
  try {
    const res = await listExperimenttaskByTheme(initSearchParams.themeId);
    // const res = await listExperimenttaskByTheme(searchParams.value);
    console.log("listExperimenttaskByTheme", res);
    if (res.rows) {
      experimentList.value = res.rows || [];
      total.value = res.rows.length || 0;
      console.log("experimentList", experimentList.value)
      console.log("total", total.value);
    } else {
      ElMessage.error('获取实验结果失败');
    }
  } catch (e) {
    ElMessage.error(`获取实验结果失败：${e.message}`);
  }
  loading.value = false;
};

// 搜索事件
const onSearch = () => {
  searchParams.value = { ...initSearchParams, name: searchParams.value.name };
  loadData();
};

// 分页事件
const onPageChange = (page) => {
  searchParams.value.current = page;
  loadData();
};
const onPageSizeChange = (size) => {
  searchParams.value.pageSize = size;
  loadData();
};

// 打开详情页
const openDetailPage = (id) => {
  window.open(`/result/${id}`, '_blank');
};

// 加载数据
onMounted(() => {
  console.log("initSearchParams", initSearchParams);
  loadData();
});
</script>

<style scoped>
.my-chart-page {
  padding: 16px;
}
.search-input {
  margin-bottom: 16px;
  width: 100%;
}
.experiment-card {
  width: 100%;
}
.result-image {
  width: 100%;
  height: auto;
  margin-top: 16px;
}
.pagination {
  text-align: center;
  margin-top: 16px;
}
</style>
