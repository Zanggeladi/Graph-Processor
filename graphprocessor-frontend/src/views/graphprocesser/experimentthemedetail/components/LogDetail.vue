<template>
  <div>
    <!-- 关闭按钮 -->
    <div class="header">
      <el-icon class="close-icon" @click="closeDetail">
        <Close />
      </el-icon>
      <el-divider class="divider" />
    </div>

    <!-- 日志详情内容 -->
    <div :style="{ height: logHeight, overflow: 'scroll' }">
      <div v-if="!loading && experiment" class="content">
        <!-- 用户信息 -->
        <div class="user-info">
          <el-avatar size="large" :src="currentUser?.userAvatar" />
          <div class="user-details">
            <div class="user-name">{{ currentUser?.userName }}</div>
            <div class="timestamp">{{ formattedTime }}</div>
          </div>
        </div>

        <!-- 错误信息 -->
        <div v-if="experiment.status === STATUS.FAILED" class="error-box">
          <div>错误信息</div>
          <el-divider style="margin: 4px 0" />
          <div class="error-message">{{ experiment.execMessage }}</div>
        </div>

        <!-- 代码展示 using custom Viewer component -->
        <Viewer :value="formattedCode" :plugins="plugins" />
      </div>

      <!-- 加载动画 -->
      <div v-else class="skeleton">
        <el-skeleton :rows="10" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Close } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import moment from 'moment';
import { STATUS } from '@/constants';
import { getExperimenttask } from "@/api/graphprocesser/experimenttask.js";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Viewer } from '@bytemd/vue-next';

// 定义组件属性
const props = defineProps({
  logHeight: {
    type: String,
    required: true,
  },
});

// 状态管理
const route = useRoute();
const router = useRouter();
const currentUser = ref({ userAvatar: '/path/to/avatar.png', userName: 'User Name' });
const targetExperimentId = ref(route.query.targetExperimentId || '-1');
const experiment = ref(null);
const loading = ref(true);

// 格式化时间
const formattedTime = computed(() => {
  return experiment.value
      ? moment(experiment.value.createTime).format('YYYY-MM-DD HH:mm:ss')
      : '';
});

// 格式化代码
const formattedCode = computed(() => {
  return `\`\`\`python\n${experiment.value?.code || ''}\n\`\`\``;
});

// Plugin array for Viewer
const plugins = [
  gfm(),
  highlight(),
  // Additional plugins can be added here
];

// 获取实验数据
const loadExperiment = async () => {
  loading.value = true;
  const id = BigInt(targetExperimentId.value);
  if (id > 0) {
    try {
      const res = await getExperimenttask(id);
      if (res.code === 0) {
        experiment.value = res.data;
      } else {
        ElMessage.error('获取实验详情失败');
      }
    } catch (error) {
      ElMessage.error(`获取实验详情失败：${error.message}`);
    }
  }
  loading.value = false;
};

// 关闭详情视图
const closeDetail = () => {
  const params = new URLSearchParams({
    tab: route.query.tab || 'content',
    targetSubmitId: '-1',
  });
  if (route.query.pageNum) {
    params.append('pageNum', route.query.pageNum);
  }
  router.push({ query: Object.fromEntries(params) });
};

// 加载实验数据
onMounted(() => {
  loadExperiment();
});
</script>

<style scoped>
.header {
  margin: 5px 16px;
}
.close-icon {
  color: rgb(140, 140, 140);
  cursor: pointer;
}
.close-icon:hover {
  color: black;
}
.divider {
  margin: 5px 0 0 0;
}
.content {
  margin: 16px 20px;
}
.user-info {
  display: flex;
}
.user-details {
  margin-left: 4px;
  flex: 1 1 auto;
}
.user-name {
  font-weight: 600;
}
.timestamp {
  font-size: 11px;
  color: #3c3c4399;
}
.error-box {
  margin-top: 16px;
  border-radius: 8px;
  background-color: #000a2008;
  padding: 16px;
  font-size: 13px;
  color: #262626bf;
}
.error-message {
  color: #d9534f;
}
.skeleton {
  margin: 16px 20px;
}
</style>
