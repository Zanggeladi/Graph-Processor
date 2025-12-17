<template>
  <el-row style="width: 100%; margin: 0 auto">
    <!-- 左侧面板 -->
    <el-col :span="12" style="padding-right: 4px;">
      <el-card :body-style="{ padding: '0' }" :style="{ height: 'calc(100vh - 130px)', borderRadius: '4px' }">
        <el-tabs v-model="activeTab" @tab-click="handleTabChange">
          <el-tab-pane v-for="item in themeItems" :key="item.key" :label="item.label" :name="item.key" />
        </el-tabs>
        <div v-if="loading" style="padding: 0 20px">
          <el-skeleton :rows="10" />
        </div>
        <template v-else>
          <div v-if="activeTab === 'content'">
            <Content :theme="theme" :style="{ height: 'calc(100vh)' }" maxHeight="calc(100vh)" />
          </div>
          <div v-else-if="activeTab === 'log'">
            <SubmitLog :theme="theme" />
          </div>
          <div v-else-if="activeTab === 'curve'">
            <Curve :theme="theme" />
          </div>
        </template>
      </el-card>
    </el-col>

    <!-- 右侧面板 -->
    <el-col :span="12" style="padding-left: 4px">
      <div style="display: flex; flex-direction: column; height: calc(100vh - 130px)">
        <el-card :body-style="{ padding: 0, display: 'flex', flexDirection: 'column', height: '100%' }" style="flex-grow: 1; margin-bottom: 8px; border-radius: 4px">
          <el-card :body-style="{ padding: 0, display: 'flex', flexDirection: 'column', height: '100%' }" style="flex-grow: 1; margin-bottom: 8px; border-radius: 4px">
            <!-- 如果 targetExperimentId 大于 0，则渲染 LogDetail 组件 -->
            <LogDetail
                v-if="BigInt(targetExperimentId) > 0"
                :logHeight="logHeight"
                :coderHeight="codeEditorHeight"
                :code="code"
                @update:code="code = $event"
                :language="language"
                @update:language="language = $event"
            />

            <!-- 否则渲染 Editor 组件 -->
            <div v-else :style="coderHeight">
              <Editor
                  :code="code"
                  :setCode="updateCode"
                  :language="language"
                  :setLanguage="updateLanguage"
                  :coderHeight="codeEditorHeight"
              />
            </div>

          </el-card>

        </el-card>
<!--        控制台-->
        <el-card :body-style="{ padding: 0 }" style="border-radius: 4px">
          <transition name="fade-slide">
            <div v-if="terminalOpen" style="position: relative; height: 220px">
              <el-tabs v-model="activeTerminal" @tab-click="handleTerminalTabChange" style="padding: 0 16px">
                <el-tab-pane v-for="item in terminalItems" :key="item.key" :label="item.label" />
              </el-tabs>
              <div style="max-height: 300px;">
                <div style="margin: 0 20px 10px 20px">
                  <el-form label-width="80px" :label-position="'top'" style="margin: 0 20px 10px 20px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="任务名称">
                          <el-input v-model="name" placeholder="请输入任务名称" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="数据集">
                          <el-select v-model="selectedDatasetPath" placeholder="选择数据集" @change="onSelectedDatasetChange">
                            <el-option
                                v-for="item in datasetList"
                                :key="item.id"
                                :label="`${item.datasetName} - ${(item.size / (1024 * 1024)).toFixed(2)} MB - ${item.tags}`"
                                :value="item.id"
                            />
                          </el-select>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-form-item label="模型">
                          <el-select v-model="selectedModelCode" placeholder="选择模型" @change="onSelectedModelChange">
                            <el-option
                                v-for="(model, index) in modelList"
                                :key="index"
                                :label="model.modelName"
                                :value="model.code"
                                :title="model.description"
                            />
                          </el-select>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-form>

                  <!--                <el-form>-->
                  <!--                  <el-form-item label="任务名称">-->
                  <!--                    <el-input v-model="name" />-->
                  <!--                  </el-form-item>-->
                  <!--                  <el-form-item label="数据集">-->
                  <!--                    <el-select v-model="selectedDatasetPath" placeholder="选择数据集" @change="onSelectedDatasetChange">-->
                  <!--                      <el-option-->
                  <!--                          v-for="item in datasetList"-->
                  <!--                          :key="item.id"-->
                  <!--                          :label="`${item.datasetName} - ${(item.size / (1024 * 1024)).toFixed(2)} MB - ${item.tags}`"-->
                  <!--                          :value="item.id"-->
                  <!--                      />-->
                  <!--                    </el-select>-->
                  <!--                  </el-form-item>-->
                  <!--                  <el-form-item label="模型">-->
                  <!--                    <el-select v-model="selectedModelCode" placeholder="选择模型" @change="onSelectedModelChange">-->
                  <!--                      <el-option-->
                  <!--                          v-for="(model, index) in modelList"-->
                  <!--                          :key="index"-->
                  <!--                          :label="model.modelName"-->
                  <!--                          :value="model.code"-->
                  <!--                          :title="model.description"-->
                  <!--                      />-->
                  <!--                    </el-select>-->
                  <!--                  </el-form-item>-->
                  <!--                </el-form>-->
                  <!--                <el-button type="primary" @click="handleClick()" size="small">test</el-button>-->
                </div>
              </div>
            </div>
          </transition>


          <div style="padding: 8px">
            <el-button @click="toggleTerminal" type="text" size="small" style="width: 90px; height: 28px">
              控制台 <i :class="terminalOpen ? 'el-icon-arrow-down' : 'el-icon-arrow-up'"></i>
            </el-button>

            <el-space style="float: right">
              <el-button
                  :disabled="!theme || BigInt(targetExperimentId) > 0"
                  @click="handleSubmit"
                  size="small"
                  style="width: 66px; height: 28px"
                  type="primary"
              >
                提交
              </el-button>
              <el-button
                  :loading="isSubmitting"
                  :disabled="!theme || BigInt(targetExperimentId) > 0"
                  @click="handleSubmit"
                  size="small"
                  style="width: 66px; height: 28px"
                  type="primary"
              >
                提交
              </el-button>

            </el-space>
          </div>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import Content from "@/views/graphprocesser/experimentthemedetail/components/Content.vue";
import LogDetail from "@/views/graphprocesser/experimentthemedetail/components/LogDetail.vue";
import Curve from "@/views/graphprocesser/experimentthemedetail/components/Curve.vue";
import Editor from "@/views/graphprocesser/experimentthemedetail/components/Editor.vue";
import {getExperimenttheme} from "@/api/graphprocesser/experimenttheme.js";
import {listMyDataset} from "@/api/graphprocesser/dataset.js";
import {listMyModel} from "@/api/graphprocesser/model.js";
import {submitExperimentTask} from "@/api/graphprocesser/experimenttask.js";
import SubmitLog from "@/views/graphprocesser/experimentthemedetail/components/SubmitLog.vue";

// 路由和参数
const route = useRoute();
const router = useRouter();
const themeId = route.params.themeId || '';
const urlSearchParams = new URLSearchParams(location.search);

// 状态管理
const loading = ref(true);
const theme = ref(null);
const activeTab = ref(urlSearchParams.get('tab') || 'content');
const targetExperimentId = ref(urlSearchParams.get('targetExperimentId') || '-1');
const activeTerminal = ref('1');
const coderHeight = ref('100%');
const codeEditorHeight = ref('calc(100vh - 320px)');
const logHeight = ref('calc(100vh - 240px)');
const terminalOpen = ref(false);
const name = ref('');
const code = ref('# 保存需要对比的训练曲线如curve.png等到当前文件夹下\n\n\n\n');
const language = ref('python');
const selectedDatasetPath = ref('');
const selectedModelCode = ref('');
const datasetList = ref([]);
const modelList = ref([]);

// 列表项数据
const themeItems = reactive([
  { key: 'content', label: '主题详情' },
  { key: 'log', label: '提交记录' },
  { key: 'curve', label: '对比曲线' },
]);
const terminalItems = reactive([{ key: '1', label: '表单' }]);

// 数据请求
onMounted(async () => {
  await fetchData();
});

watch(activeTab.value, () => {
  console.log(activeTab.value);
});

const handleClick = () => {
  console.log('handleClick');
  code.value += '\n\n' + 'print("hello world")';
  ElMessage.success(code.value);
}

// 更新代码内容的函数
const updateCode = (newCode) => {
  code.value = newCode;
};

// 更新语言的函数
const updateLanguage = (newLanguage) => {
  language.value = newLanguage;
};

async function fetchData() {
  loading.value = true;
  try {
    const [themeData, modelData, datasetData] = await Promise.all([
      getExperimenttheme(themeId),
      listMyModel(),
      listMyDataset(),
    ]);
    if (themeData.code === 200) theme.value = themeData.data;
    datasetList.value = datasetData?.data || [];
    modelList.value = modelData?.data || [];
  } catch (error) {
    ElMessage.error('数据加载失败');
  }
  loading.value = false;
}

const handleTabChange = (newTab) => {
  activeTab.value = newTab.props.name;
}

const handleTerminalTabChange = (newTab) => (activeTerminal.value = newTab.props.name);

const toggleTerminal = () => {
  terminalOpen.value = !terminalOpen.value;
  codeEditorHeight.value = terminalOpen.value ? 'calc(100vh - 580px)' : 'calc(100vh - 320px)';
  logHeight.value = terminalOpen.value ? 'calc(100vh - 465px)' : 'calc(100vh - 215px)';
};

const onSelectedDatasetChange = (value) => {
  selectedDatasetPath.value = value;
}

const onSelectedModelChange = (value) => {
  code.value += `\n${value}`;
}

async function handleSubmit() {
  if (theme.value) {
    try {
      const res = await submitExperimentTask({
        taskName: name.value,
        modelCode: code.value,
        themeId: theme.value.id,
        datasetId: selectedDatasetPath.value,
      });
      if (res.code === 200) {
        ElMessage.success('任务提交成功');
        targetExperimentId.value = res.data.toString();
      } else {
        ElMessage.error('任务提交失败');
      }
    } catch (error) {
      ElMessage.error('任务提交失败');
    }
  }
}
</script>

<style scoped>
.el-form-item__label {
  color: #3c3c4399;
  font-size: 0.75rem;
  font-weight: 500;
  margin-bottom: 8px;
}

.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.3s ease;
}
.fade-slide-enter, .fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

</style>
