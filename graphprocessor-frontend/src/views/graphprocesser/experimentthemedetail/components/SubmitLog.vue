<template>
  <div class="catalog">
    <!-- 实验记录表格 -->
    <el-table
        :data="dataSource"
        style="width: 100%"
        :loading="loading"
        :row-key="'id'"
        height="calc(100vh - 132px)"
    >
      <!-- 状态列 -->
      <el-table-column label="状态" align="center">
        <template #default="{ row }">
          <span :style="{ color: statusColors[row.status] }">{{ statusLabels[row.status] }}</span>
        </template>
      </el-table-column>

      <!-- ID 列 -->
      <el-table-column prop="id" label="id"/>

      <!-- 实验任务名称列 -->
      <el-table-column prop="taskName" label="实验任务名称" :show-overflow-tooltip="true" />

      <!-- 处理结果列 -->
      <el-table-column label="处理结果" align="center">
        <template #default="{ row }">
          <el-button
              v-if="row.status === STATUS.SUCCEED"
              type="primary"
              icon="el-icon-download"
              @click="downloadFile(row)"
          >
            下载
          </el-button>
          <span v-else>NULL</span>
        </template>
      </el-table-column>

      <!-- 时间列 -->
      <el-table-column label="时间" align="center">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-tooltip content="查看代码" placement="top">
            <el-button
                type="text"
                @click="clickInspect(row.id)"
                class="inspect-button"
            >
              <svg-icon icon-class="chakan" />
            </el-button>
          </el-tooltip>
<!--          <el-tooltip content="删除记录" placement="top">-->
<!--            <el-popconfirm-->
<!--                confirm-button-text="是"-->
<!--                cancel-button-text="否"-->
<!--                title="确定要删除该实验任务？"-->
<!--                @confirm="() => handleDelete(row)"-->
<!--            >-->
<!--              <el-button type="text">-->
<!--                <svg-icon icon-class="chakan" />-->
<!--              </el-button>-->
<!--            </el-popconfirm>-->
<!--          </el-tooltip>-->
          <el-tooltip content="删除记录" placement="top">
              <el-button type="text">
                <svg-icon icon-class="delete" />
              </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-if="total > 0"
        background
        layout="prev, pager, next"
        :current-page="pageNum"
        :page-size="10"
        :total="total"
        @current-change="changePage"
    />

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="reloadData"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { saveAs } from 'file-saver';
import moment from 'moment';
import { STATUS, COS_HOST } from '@/constants';
import {delExperimenttask, listExperimenttaskByTheme} from "@/api/graphprocesser/experimenttask.js";
import SvgIcon from "@/components/SvgIcon/index.vue";


// 组件属性
const props = defineProps({
  theme: {
    type: Object,
    required: true,
  },
});

// 路由和状态
const route = useRoute();
const router = useRouter();
const targetExperimentId = ref(Number(route.query.targetExperimentId) || -1);
const pageNum = ref(Number(route.query.pageNum) || 1);
const dataSource = ref([]);
const total = ref(0);
const loading = ref(true);

// 状态标签和颜色
const statusLabels = {
  [STATUS.WAIT]: '等待中',
  [STATUS.RUNNING]: '运行中',
  [STATUS.FAILED]: '运行错误',
  [STATUS.SUCCEED]: '处理完毕',
};
const statusColors = {
  [STATUS.WAIT]: '#FFC107',
  [STATUS.RUNNING]: '#2196F3',
  [STATUS.FAILED]: '#F44336',
  [STATUS.SUCCEED]: '#4CAF50',
};

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelName: null,
    tags: null,
    subtask: null,
    shortDescription: null,
    detailedDescription: null,
    datasets: null,
  },
  rules: {
    modelName: [
      { required: true, message: "模型名称不能为空", trigger: "blur" }
    ],
    subtask: [
      { required: true, message: "下游任务不能为空", trigger: "blur" }
    ],
    codePath: [
      { required: true, message: "模型代码存储路径不能为空", trigger: "blur" }
    ],
    datasets: [
      { required: true, message: "官方数据集列表不能为空", trigger: "blur" }
    ],
    requiredFiles: [
      { required: true, message: "个人数据集文件列表不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

// 数据加载
const reloadData = async () => {
  console.log("reloadData");
  console.log("props", props.theme.id);
  loading.value = true;
  const res = await listExperimenttaskByTheme(props.theme.id, queryParams);
  console.log("listExperimenttaskByTheme", res);
  if (res.code === 200) {
    console.log("res.rows", res.rows);
    dataSource.value = res.rows || [];
    total.value = res.total || 0;
    console.log("dataSource", dataSource.value);
    console.log("total", total.value);
  } else {
    ElMessage.error('获取实验记录失败');
  }
  loading.value = false;
};

// 文件下载
const downloadFile = async (row) => {
  const blob = await testDownloadFileUsingPost(
      { filepath: row.resultPath },
      { responseType: 'blob' }
  );
  const fullPath = COS_HOST + row.resultPath;
  saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
};

// 删除实验任务
const handleDelete = async (row) => {
  const hide = ElMessage.loading('正在删除');
  try {
    await delExperimenttask(row.id);
    hide.close();
    ElMessage.success('删除成功');
    reloadData();
  } catch (error) {
    hide.close();
    ElMessage.error(`删除失败：${error.message}`);
  }
};

// 查看实验任务详情
const clickInspect = (id) => {
  updateQueryParam(pageNum.value, id);
};

// 更新路径参数
const updateQueryParam = (newPageNum, newTargetExperimentId) => {
  const queryParams = { pageNum: newPageNum.toString(), tab: 'log', targetExperimentId: newTargetExperimentId.toString() };
  router.push({ query: queryParams });
};

// 分页改变
const changePage = (page) => {
  pageNum.value = page;
  updateQueryParam(page, targetExperimentId.value);
};

// 格式化时间
const formatTime = (time) => {
  return time ? moment(time).format('YYYY-MM-DD HH:mm:ss') : '';
};

// 初次加载
onMounted(() => {
  reloadData();
});
</script>

<style scoped>
.catalog {
  max-height: calc(100vh - 350px);
  overflow: auto;
}
.inspect-button {
  color: #FA541C;
}
</style>
