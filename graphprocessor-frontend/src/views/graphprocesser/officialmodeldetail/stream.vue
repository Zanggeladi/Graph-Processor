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
          <el-row v-if="model.modelImgPath">
            <el-image

                :src="model.modelImgPath ? model.modelImgPath : ''"
                fit="contain"
                style="width: 100%; height: 300px; border-radius: 10px;"
                :alt="model.name"
            >
              <!-- 图片为空时显示 -->
<!--              <template #error>-->
<!--                <div class="image-placeholder">-->
<!--                  <el-icon><i class="el-icon-picture-outline"></i></el-icon>-->
<!--                  <p>暂无图片</p>-->
<!--                </div>-->
<!--              </template>-->
            </el-image>
          </el-row>

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
<!--          <el-form-item v-if="taskAddRequest.datasetType === 0" label="选择数据集" prop="dataset">-->
<!--            <el-select v-model="taskAddRequest.datasetName" placeholder="选择数据集" class="custom-select">-->
<!--              <el-option v-for="dataset in model.datasets" :key="dataset" :label="dataset" :value="dataset"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
          <el-form-item v-if="taskAddRequest.datasetType === 0" label="选择数据集" prop="dataset">
            <div style="display: flex; align-items: center;">
              <el-select v-model="taskAddRequest.datasetName" placeholder="选择数据集" class="custom-select">
                <el-option v-for="dataset in model.datasets" :key="dataset" :label="dataset" :value="dataset"></el-option>
              </el-select>
              <!-- 悬停提示问号图标 -->
<!--              <el-tooltip content="请选择一个合适的数据集以用于任务分析" placement="top">-->
<!--                <svg-icon icon-class="question"  style="margin-left: 5px;" />-->
<!--              </el-tooltip>-->
              <el-tooltip placement="top" effect="dark">
                <template #content>
                  <img
                      src="https://graphprocesser-cos-1324554477.cos.ap-beijing.myqcloud.com/assets/stream_datasets.png"
                      alt="提示内容"
                      style="max-width: 400px; max-height: 200px; display: block;"
                  />
                </template>
                <svg-icon icon-class="question" style="margin-left: 5px;" />
              </el-tooltip>
            </div>
                        <div style="margin-left: 20px;">
<!--              <div v-if="model.subtask === '边频次查询' || model.subtask === '路径可达性查询'">-->
              <div>
                <div class="example-container" style="background-color: #f9f9f9; border: 1px solid #e4e4e4; border-radius: 8px; padding: 15px; font-family: Arial, sans-serif; line-height: 1.6;">
                  <span>
                    <strong style="font-size: 14px; color: #333;">了解数据集：</strong><br />
                    点击下载数据集（部分），了解数据集的格式和内容。
                    <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                      <a
                          :href="COS_HOST + '/assets/phone.csv'"
                          download="数据集示例.csv"
                      >
                        <svg-icon icon-class="download" style="color: #67C23A; margin-left: 5px; margin-right: 5px;" />
                        <strong>phone数据集</strong>
                      </a>
                    </code>
                    <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                      <a
                          :href="COS_HOST + '/assets/road_sample.csv'"
                          download="数据集示例.csv"
                      >
                        <svg-icon icon-class="download" style="color: #67C23A; margin-left: 5px; margin-right: 5px;" />
                        <strong>road数据集</strong>
                      </a>
                    </code>
                    <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                      <a
                          :href="COS_HOST + '/assets/enron_sample.csv'"
                          download="数据集示例.csv"
                      >
                        <svg-icon icon-class="download" style="color: #67C23A; margin-left: 5px; margin-right: 5px;" />
                        <strong>enron数据集</strong>
                      </a>
                    </code>
                  </span>
                </div>
              </div>
            </div>

          </el-form-item>



          <!-- 个人上传数据集 -->
          <el-form-item v-if="taskAddRequest.datasetType === 1" label="上传数据集" prop="uploadFile">
            <div>
              <FileUpload
                  v-model="taskAddRequest.datasetPath"
                  :limit="1"
                  :fileSize="1024"
                  :fileType="fileTypeList"
                  :isShowTip="true"
              />
            </div>
            <div style="margin-left: 20px;">
<!--              <div v-if="model.subtask === '边频次查询' || model.subtask === '路径可达性查询'">-->
              <div>
                <div class="example-container" style="background-color: #f9f9f9; border: 1px solid #e4e4e4; border-radius: 8px; padding: 15px; font-family: Arial, sans-serif; line-height: 1.6;">
                  <span>
                    <strong style="font-size: 14px; color: #333;">csv文件格式：</strong><br />
                    行内,分隔
                    <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                      节点1,节点2,节点1标签,节点2标签,边标签,时间戳
                    </code>
                    <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                      src, dst, l_A, l_B, l_e, timestamp
                    </code>

                    <a
                        :href="COS_HOST + '/assets/phone_518.csv'"
                        download="数据集示例.csv"
                    >
                      <svg-icon icon-class="download" style="color: #67C23A; margin-left: 5px; margin-right: 5px;" />
                      <strong>点击下载示例文件</strong>
                    </a>
                  </span>
                </div>
              </div>
            </div>

          </el-form-item>

<!--          参数表单-->
<!--          <div v-if="model.subtask === '边频次查询' || model.subtask === '路径可达性查询'">-->
<!--            <el-form-item :label="'nodeId1'" :prop="'nodeId1'">-->
<!--              <el-input v-model="taskAddRequest.parameters['nodeId1']" :placeholder="'请输入nodeId1'" class="custom-input"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item :label="'nodeId2'" :prop="'nodeId2'">-->
<!--              <el-input v-model="taskAddRequest.parameters['nodeId2']" :placeholder="'请输入nodeId2'" class="custom-input"></el-input>-->
<!--            </el-form-item>-->
<!--          </div>-->

          <div v-if="model.subtask === '边频次查询' || model.subtask === '路径可达性查询'">
            <el-form-item :label="'nodeId1'" :prop="'nodeId1'">
              <el-input
                  v-model="taskAddRequest.parameters['nodeId1']"
                  placeholder="请输入nodeId1"
                  class="custom-input">
              </el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('nodeId1', '73')" />
<!--                <i class="el-icon-info" style="color: #67C23A; margin-right: 5px;"></i>-->
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 73</span>
              </div>
            </el-form-item>
            <el-form-item :label="'nodeId2'" :prop="'nodeId2'">
              <el-input
                  v-model="taskAddRequest.parameters['nodeId2']"
                  placeholder="请输入nodeId2"
                  class="custom-input">
              </el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('nodeId2', '34')" />
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 34</span>
              </div>
            </el-form-item>
          </div>




          <div v-else-if="model.subtask === '节点度查询'">
            <el-form-item :label="'nodeId'" :prop="'nodeId'">
              <el-input v-model="taskAddRequest.parameters['nodeId']" :placeholder="'请输入nodeId'" class="custom-input"></el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('nodeId', '34')" />
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 34</span>
              </div>
            </el-form-item>
          </div>
          <div v-else-if="model.subtask === '近似子图查询'">
<!--            一个输入域-->
            <el-form-item :label="'edges'" :prop="'edges'">
              <el-input v-model="taskAddRequest.parameters['edges']" :placeholder="'请输入子图的边，空格分隔'" class="custom-input" type="textarea" autosize ></el-input>
              <div class="example-container" style="background-color: #f9f9f9; border: 1px solid #e4e4e4; border-radius: 8px; padding: 15px; font-family: Arial, sans-serif; line-height: 1.6;">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 8px; cursor: pointer;" @click="setExampleValue('edges', '75 16 type1 type1 edge1;75 18 type1 type1 edge1;75 4 type1 type1 edge1;95 3 type1 type1 edge1;2 86 type1 type1 edge3')" />
                <span>
                  <strong style="font-size: 14px; color: #333;">输入边格式：</strong><br />
                  行内空格分隔,行间;分隔<br />
                  <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                    节点1 节点2 节点1标签 节点2标签 边标签
                  </code>
                  <code style="display: block; margin: 5px 0; font-size: 13px; color: #555; background-color: #f4f4f4; padding: 5px; border-radius: 4px;">
                    src, dst, l_A, l_B, l_e
                  </code>
                  <strong style="font-size: 14px; color: #333;">phone数据集示例输入值（请根据不同数据集进行调整）：</strong>
                  <pre style="background-color: #f7f7f7; border: 1px solid #ddd; padding: 10px; border-radius: 5px; font-family: 'Courier New', monospace; font-size: 13px; white-space: pre-wrap; color: #444; margin-top: 5px;">
75 16 type1 type1 edge1;
75 18 type1 type1 edge1;
75 4  type1 type1 edge1;
95 3  type1 type1 edge1;
2  86 type1 type1 edge3;
                  </pre>
                </span>
              </div>

            </el-form-item>

          </div>
          <div v-else-if="model.subtask === 'Top-k频繁项挖掘'">
            <el-form-item :label="'k'" :prop="'k'">
              <el-input v-model="taskAddRequest.parameters['k']" :placeholder="'请输入k'" class="custom-input"></el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('k', '100')" />
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 100</span>
              </div>
            </el-form-item>
          </div>
          <div v-else-if="model.subtask === 'Top-l条件频繁项挖掘'">
            <el-form-item :label="'k'" :prop="'k'">
              <el-input v-model="taskAddRequest.parameters['k']" :placeholder="'请输入k'" class="custom-input"></el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('k', '100')" />
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 100</span>
              </div>
            </el-form-item>
            <el-form-item :label="'l'" :prop="'l'">
              <el-input v-model="taskAddRequest.parameters['l']" :placeholder="'请输入l'" class="custom-input"></el-input>
              <div class="example-container">
                <svg-icon icon-class="link" style="color: #67C23A; margin-right: 5px;" @click="setExampleValue('l', '5')" />
                <span>phone数据集示例输入值（请根据不同数据集进行调整）: 5</span>
              </div>
            </el-form-item>
          </div>

          <el-form-item>
            <el-button type="primary" @click="submitTask">提交任务</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 查看结果 -->
      <div v-if="activeStep === 2">
        <h2>分析结果</h2>
        <div v-if="taskId !== null && (taskStatus === null || taskStatus === 0)"><el-alert title="任务正在排队，请稍候..." type="info" effect="dark" /></div>
        <div v-if="taskStatus === 1"><el-alert title="任务运行中，请耐心等待..." type="warning" effect="dark" /></div>
        <div v-if="taskStatus === 2">
          <el-alert title="任务执行完毕！执行结果如下：" type="success" effect="dark" />

          <MdPreview :editorId="id" :modelValue="resultDetail.outputMessage"/>

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

<script setup name="officialmodeldetail-stream">
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getOfficialmodel } from '@/api/graphprocesser/officialmodel.js';
import { submitTaskAPI, getTaskStatus } from '@/api/graphprocesser/officialmodeltask.js';
import {getOfficialmodeltaskresultByTaskId} from "@/api/graphprocesser/officialmodeltaskresult.js";


import { MdPreview, MdCatalog } from 'md-editor-v3';
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css';
import {COS_HOST} from "@/constants/index.js";
import SvgIcon from "@/components/SvgIcon/index.vue";
import FileUpload from "@/components/FileUpload/index.vue";
const id = 'preview-only';
const scrollElement = document.documentElement;


const route = useRoute();
const router = useRouter();
const activeStep = ref(0);
const taskStatus = ref(null);
const taskId = ref(null)

const taskAddRequest = ref({
  officialModelId: route.params.modelId,
  datasetType: 0,
  datasetName: '',
  datasetPath: '',
  parameters:{},
});

//定义文件后缀列表
const fileTypeList = ['csv'];

const fileList = ref([]);
const rules = ref({
  name: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  datasetName: [{ required: false, message: '请选择数据集', trigger: 'change' }],
});

const model = ref({});
const resultDetail = ref(null);

const getModelDetail = (id) => {
  getOfficialmodel(id).then((response) => {
    // console.log(response);
    response.data.tags = response.data.tags.split(',');
    response.data.datasets = response.data.datasets.split(',').map((item) => item.trim().toLowerCase());
    response.data.parameters = response.data.parameters.split(',').map((item) => item.trim());
    model.value = response.data;
    console.log(model.value);
  });

};
onMounted(() => {
  getModelDetail(route.params.modelId);
})
// getModelDetail(route.params.modelId);

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
    console.log(data);
    const response = await submitTaskAPI(data);
    taskId.value = response.data;
    console.log('taskId', taskId.value);
    if (response) {
      console.log(response);
      activeStep.value++;
      getTaskResult(taskId.value);
    }
  } catch (error) {
    console.error(error);
  }
};

const getTaskResult = (taskId) => {
  const interval = setInterval(async () => {
    const response = await getTaskStatus(taskId);
    console.log('getTaskStatus-response', response)
    taskStatus.value = response.data;
    if(taskStatus.value === 2){
      const response = await getOfficialmodeltaskresultByTaskId(taskId);
      console.log(response)
      // response.data中的outputMessage内容前后添加渲染
      response.data.outputMessage = '```txt\n' + response.data.outputMessage + '\n```';

      resultDetail.value = response.data;
    }
    if (taskStatus.value === 2 || taskStatus.value === 3) {
      console.log('清除计时器');
      clearInterval(interval);
    }
  }, 5000);
};

const nextStep = () => {
  activeStep.value++;
};

const prevStep = () => {
  activeStep.value--;
};

const goToResultList = () => {
  // router.push();
  router.push('/admin/official/officialmodeltask')
};

const goBack = () => {
  router.go(-1);
};

const handleUploadSuccess = (response, file) => {
  taskAddRequest.value.uploadFile = file;
};

function setExampleValue(paramKey, exampleValue) {
  taskAddRequest.value.parameters[paramKey] = exampleValue;
}
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

.example-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  display: block;
}



.form-card {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 15px;
  background-color: #ffffff;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.example-container {
  margin-top: 8px;
  background-color: #f0f9eb;
  padding: 5px 10px;
  border: 1px solid #e1f3d8;
  border-radius: 4px;
  font-size: 12px;
  color: #67C23A;
  display: flex;
  align-items: center;
}


</style>
