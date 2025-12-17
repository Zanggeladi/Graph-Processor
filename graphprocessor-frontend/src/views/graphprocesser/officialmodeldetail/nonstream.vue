<template>
  <div class="app-container">
    <!-- 步骤条部分 -->
    <el-card class="step-card step-header">
      <el-steps :active="activeStep" class="custom-steps" align-center>
        <el-step title="模型信息" description="Model information" class="step-font"></el-step>
        <el-step title="任务配置" description="Task configuration" class="step-font"></el-step>
        <el-step title="查看结果" description="View results" class="step-font"></el-step>
      </el-steps>
    </el-card>

    <!-- 内容部分 -->
    <el-card class="step-card step-content">
      <!-- 模型信息 -->
      <div v-if="activeStep === 0">
        <el-row :gutter="20">
          <!-- 左侧：模型图片 -->
<!--          <el-row>-->
<!--            <el-image-->
<!--                :src="model.modelImgPath ? model.modelImgPath : ''"-->
<!--                fit="contain"-->
<!--                style="width: 100%; height: 300px; border-radius: 10px;"-->
<!--                :alt="model.name"-->
<!--            >-->
<!--              &lt;!&ndash; 图片为空时显示 &ndash;&gt;-->
<!--              <template #error>-->
<!--                <div class="image-placeholder">-->
<!--                  <el-icon><i class="el-icon-picture-outline"></i></el-icon>-->
<!--                  <p>暂无图片</p>-->
<!--                </div>-->
<!--              </template>-->
<!--            </el-image>-->
<!--          </el-row>-->

          <!-- 右侧：模型描述信息 -->
          <el-row>
<!--            <h2>{{ model.name }}</h2>-->
<!--            <p><strong>任务类型：</strong>{{ model.subtask }}</p>-->
<!--            <p>{{ model.shortDescription }}</p>-->
            <div class="tags">
              <el-tag v-for="tag in model.tags" :key="tag" type="success" style="margin-right: 8px">{{ tag }}</el-tag>
            </div>
<!--            <MdEditor :value="model.detailedDescription" />-->
            <MdPreview :editorId="id" :modelValue="model.detailedDescription" preview-theme="cyanosis" />
          </el-row>
        </el-row>
      </div>

      <!-- 任务配置 -->
      <div v-if="activeStep === 1">
        <el-form :model="taskAddRequest" :rules="rules" ref="taskFormRef" label-width="120px" class="custom-form">
<!--          <el-form-item label="任务名称" prop="name">-->
<!--            <el-input v-model="taskAddRequest.name" placeholder="任务名称" class="custom-input"></el-input>-->
<!--          </el-form-item>-->

          <el-form-item label="选择数据集类型">
            <el-radio-group v-model="taskAddRequest.datasetType" @change="handleDatasetTypeChange">
              <el-radio-button :label="0">官方数据集</el-radio-button>
              <el-radio-button :label="1">个人上传数据集</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <!-- 官方数据集选择 -->
          <el-form-item v-if="taskAddRequest.datasetType === 0" label="选择数据集" prop="dataset">
            <el-select v-model="taskAddRequest.datasetName" placeholder="选择数据集" class="custom-select">
              <el-option v-for="dataset in model.datasets" :key="dataset" :label="dataset" :value="dataset"></el-option>
            </el-select>
          </el-form-item>

          <!-- 个人上传数据集 -->
          <el-form-item v-if="taskAddRequest.datasetType === 1" label="上传数据集" prop="uploadFile">
<!--            <el-upload action="/upload" list-type="text" :on-success="handleUploadSuccess" :file-list="fileList">-->
<!--              <el-button type="primary">选择数据集</el-button>-->
<!--            </el-upload>-->
            <FileUpload
              v-model="taskAddRequest.datasetPath"
              :limit="1"
              :fileSize="1024"
              :fileType="model.requiredFiles"
              :isShowTip="true"
            />

          </el-form-item>

          <!-- 动态生成表单项 -->
          <div v-if="model.parameters && model.parameters.length > 0">
            <div v-for="(param, index) in model.parameters" :key="index">
              <el-form-item :label="param" :prop="param">
                <el-input v-model="taskAddRequest.parameters[param]" :placeholder="'请输入' + param" class="custom-input"></el-input>
              </el-form-item>
            </div>
          </div>

          <el-form-item>
            <el-button type="primary" @click="submitTask">提交任务</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 查看结果 -->
      <div v-if="activeStep === 2">
        <MdPreview :editorId="id" modelValue="## 分析结果" preview-theme="cyanosis" />
        <div v-if="taskStatus === 0"><el-alert title="任务正在排队，请稍候..." type="info" effect="dark" /></div>
        <div v-if="taskStatus === 1"><el-alert title="任务运行中，请耐心等待..." type="warning" effect="dark" /></div>
        <div v-if="taskStatus === 2">
          <el-alert title="任务完成！分析结果如下：" type="success" effect="dark" />

          <el-row :gutter="10">
<!--            间隔-->
            <el-col :span="12">
              <!-- 训练曲线展示 -->
              <div class="result-display" style="height: 50vh; margin-top: 20px; text-align: center;">
    <!--            <img :src="COS_HOST + resultDetail.curvePath" alt="训练曲线" style="max-width: 100%; max-height: 100%;" />-->
                <ImagePreview
                  :src="COS_HOST + resultDetail.curvePath"
                  height="100%"
                >
                </ImagePreview>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="result-display" style="height: 50vh; margin-top: 20px; text-align: center;">
    <!--            <img :src="COS_HOST + resultDetail.curvePath" alt="训练曲线" style="max-width: 100%; max-height: 100%;" />-->
                <ImagePreview
                  :src="COS_HOST + resultDetail.tsnePath"
                  height="100%"
                >
                </ImagePreview>
              </div>
            </el-col>
          </el-row>
<!--          <el-row>-->
<!--            {{resultDetail.curvePath}}-->
<!--          </el-row>-->
          <el-row>
            <MdPreview :editorId="id" :modelValue="'```\n' + resultDetail.outputMessage"/>
          </el-row>


        </div>
        <div v-if="taskStatus === 3">
          <el-alert title="任务失败，请重新尝试。" type="error" effect="dark" />
          <el-alert
              title="Execution failed with error:"
              type="info"
              description="More text description"
              show-icon
              style="margin-top: 10px"
          />
        </div>
      </div>
    </el-card>

    <!-- 按钮部分 -->
    <div class="button-group">
      <el-button @click="prevStep" :disabled="activeStep === 0" class="custom-button">上一步</el-button>
      <el-button @click="nextStep" v-if="activeStep < 2" class="custom-button">下一步</el-button>
      <el-button @click="goToResultList" v-if="activeStep === 2" class="custom-button">查看任务列表</el-button>
      <el-button type="primary" @click="goBack" class="custom-button">返回</el-button>
    </div>
  </div>
</template>

<script setup name="officialmodeldetail-nonstream">
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getOfficialmodel } from '@/api/graphprocesser/officialmodel.js';
import { submitTaskAPI, getTaskStatus } from '@/api/graphprocesser/officialmodeltask.js';
import {getOfficialmodeltaskresultByTaskId} from "@/api/graphprocesser/officialmodeltaskresult.js";
import FileUpload from '@/components/FileUpload'

import { MdPreview, MdCatalog } from 'md-editor-v3';
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css';
import {COS_HOST} from "@/constants/index.js";
const id = 'preview-only';
const scrollElement = document.documentElement;


const route = useRoute();
const router = useRouter();
const activeStep = ref(0);
const taskStatus = ref('pending');

const taskAddRequest = ref({
  officialModelId: route.params.modelId,
  datasetType: 0,
  datasetName: '',
  datasetPath: '',
  parameters:{},
});

const fileList = ref([]);
const rules = ref({
  name: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  datasetName: [{ required: false, message: '请选择数据集', trigger: 'change' }],
});

const model = ref({});
const taskDetail = ref(null);
const resultDetail = ref(null);

const getModelDetail = (id) => {
  getOfficialmodel(id).then((response) => {
    console.log("response", response);
    model.value = response.data;
    model.value.tags = model.value.tags.split(',');
    model.value.datasets = model.value.datasets.split(',').map((item) => item.trim().toLowerCase());
    model.value.parameters = model.value.parameters.split(',').map((item) => item.trim());
    model.value.requiredFiles = model.value.requiredFiles.split(',').map((item) => item.trim());
    //去掉空项
    model.value.parameters = model.value.parameters.filter(item => item !== '');
    model.value.requiredFiles = model.value.requiredFiles.filter(item => item !== '');
    console.log("modelinfo", model.value);
  });
};

onMounted(() => {
  getModelDetail(route.params.modelId);
})

watch(taskAddRequest, (newValue) => {
  console.log(newValue);
}, { deep: true }
);

const handleDatasetTypeChange = () => {
  if (taskAddRequest.value.datasetType === '官方数据集') {
    taskAddRequest.value.dataset = null;
    fileList.value = [];
  }
};

const submitTask = async () => {
  try {
    const data = {
      ...taskAddRequest.value,
      parameters: JSON.stringify(taskAddRequest.value.parameters)
    };
    console.log('data', data);
    const response = await submitTaskAPI(data);
    const taskId = response.data;
    console.log('taskId', taskId);
    if (response) {
      console.log(response);
      activeStep.value++;
      getTaskResult(taskId);
    }
  } catch (error) {
    console.error(error);
  }
};

const getTaskResult = (taskId) => {
  const interval = setInterval(async () => {
    const response = await getTaskStatus(taskId);
    console.log('getTaskStatus-response', response)
    // taskStatus.value = response.data;
    const tempStatus = response.data;
    if(response.data === 2){
      getOfficialmodeltaskresultByTaskId(taskId).then((res) => {
        console.log('getOfficialmodeltaskresultByTaskId', res)
        resultDetail.value = res.data;
        console.log('resultDetail', resultDetail.value)
        taskStatus.value = tempStatus;
      })
      // const response = await getOfficialmodeltaskresultByTaskId(taskId);
      // console.log('getOfficialmodeltaskresultByTaskId', response)
      // resultDetail.value = response.data;
      // console.log('resultDetail', resultDetail.value)
    }else{
      taskStatus.value = tempStatus;
    }
    if (taskStatus.value === 2 || taskStatus.value === 3) {
      console.log('清除计时器');
      clearInterval(interval);
    }
  }, 1000);
};

const nextStep = () => {
  activeStep.value++;
};

const prevStep = () => {
  activeStep.value--;
};

const goToResultList = () => {
  router.push('/admin/official/officialmodeltask');
};

const goBack = () => {
  router.go(-1);
};

const handleUploadSuccess = (response, file) => {
  taskAddRequest.value.uploadFile = file;
};
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 90vh;
  padding: 20px;
}

.step-card {
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  margin-bottom: 20px;
}

.custom-steps {
  margin-bottom: 20px;
}

.step-header {
  flex-shrink: 0;
}

.step-content {
  flex-grow: 1;
  overflow-y: auto;
}

.custom-input {
  margin-top: 10px;
  width: 100%;
}

.result-display {
  padding: 20px;
  background-color: #e0f7fa;
  border-radius: 5px;
  border: 1px solid #009688;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.custom-button {
  padding: 10px 20px;
  background-color: #409eff;
  color: #fff;
  border-radius: 4px;
  border: none;
}

.custom-button:hover {
  background-color: #66b1ff;
}

.tags {
  margin-top: 10px;
}
</style>
