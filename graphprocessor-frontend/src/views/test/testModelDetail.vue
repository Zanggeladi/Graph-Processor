<template>
  <div class="model-detail">
    <el-card v-if="model" :header="model.name">
      <div class="model-info">
        <el-image :src="model.image" fit="contain"></el-image>
        <p>{{ model.description }}</p>
        <div class="model-tags">
          <el-tag v-for="tag in model.tags" :key="tag">{{ tag }}</el-tag>
        </div>
      </div>

      <el-divider></el-divider>

      <!-- 任务表单 -->
      <div class="task-form">
        <el-form :model="taskForm" :rules="rules" ref="taskFormRef" label-width="100px">
          <el-form-item label="任务名称" prop="name">
            <el-input v-model="taskForm.name"></el-input>
          </el-form-item>

          <el-form-item label="选择数据集" prop="dataset">
            <el-select v-model="taskForm.dataset" placeholder="选择数据集">
              <el-option v-for="dataset in datasets" :key="dataset.id" :label="dataset.name" :value="dataset.id"></el-option>
            </el-select>
          </el-form-item>

          <!-- 动态生成的参数表单 -->
          <div v-for="param in model.parameters" :key="param.name">
            <el-form-item :label="param.label" :prop="param.name">
              <el-input v-model="taskForm.params[param.name]"></el-input>
            </el-form-item>
          </div>

          <!-- 上传数据集 -->
          <el-form-item label="上传数据集" prop="file">
            <el-upload
                action="/api/upload"
                :on-success="handleFileUpload"
                :file-list="fileList">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>

          <!-- 提交按钮 -->
          <el-form-item>
            <el-button type="primary" @click="submitTask">提交任务</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-divider></el-divider>

      <!-- 任务结果展示 -->
      <div class="task-results" v-if="taskResults">
        <h3>任务结果</h3>
        <div>指标一：{{ taskResults.metric1 }}</div>
        <div>指标二：{{ taskResults.metric2 }}</div>
        <!-- 展示 TSNE 图 -->
        <el-image :src="taskResults.tsneImage" fit="contain"></el-image>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import {getOfficialmodel as getModelDetail} from '@/api/graphprocesser/officialmodel.js'
import {submitTaskAPI as submitTask} from '@/api/graphprocesser/officialmodeltask.js';

export default {
  setup() {
    const route = useRoute();
    const model = ref(null);
    const taskForm = ref({
      name: '',
      dataset: null,
      params: {},
    });
    const fileList = ref([]);
    const taskResults = ref(null);
    const rules = ref({
      name: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
      dataset: [{ required: true, message: '请选择数据集', trigger: 'change' }],
    });

    const datasets = ref([
      // 假设从后端获取的数据集
      { id: 1, name: '官方数据集1' },
      { id: 2, name: '官方数据集2' },
    ]);

    // onMounted(async () => {
    //   const { id } = route.params;
    //   console.log(id);
    //   model.value = await getModelDetail(id);
    // });

    onMounted(() => {
      model.value = {
        id: 1,
        name: '经典图模型',
        description: '这是一个经典的图模型，用于图数据处理。',
        image: 'https://via.placeholder.com/300x150', // 示例图片URL
        tags: ['图处理', '机器学习', '经典模型'],
        parameters: [
          {
            name: 'param1',
            label: '参数一',
            type: 'text',
            default: '默认值1',
          },
          {
            name: 'param2',
            label: '参数二',
            type: 'number',
            default: 10,
          },
        ],
      };
    });


    const handleFileUpload = (response, file, fileList) => {
      taskForm.value.file = file;
    };

    const submitTask = async () => {
      const taskData = {
        ...taskForm.value,
      };
      const result = await submitTask(taskData);
      taskResults.value = result.data;
    };

    return {
      model,
      taskForm,
      rules,
      fileList,
      handleFileUpload,
      submitTask,
      taskResults,
      datasets,
    };
  },
};
</script>

<style scoped>
.model-detail {
  padding: 20px;
}

.model-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.model-tags {
  margin-top: 10px;
}

.task-form {
  margin-top: 20px;
}

.task-results {
  margin-top: 20px;
}
</style>
