<template>
  <div id="viewQuestionView">
    <el-row :gutter="24">
      <el-col :md="12" :xs="24">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="题目" name="question">
            <el-card v-if="question" :header="question.title">
              <el-descriptions title="判题条件" :column="{ xs: 1, md: 2, lg: 3 }">
                <el-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </el-descriptions-item>
                <el-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </el-descriptions-item>
              </el-descriptions>
              <MdViewer :value="question.content || ''" />
              <template #extra>
                <el-space>
                  <el-tag
                      v-for="(tag, index) in question.tags"
                      :key="index"
                      type="success"
                  >
                    {{ tag }}
                  </el-tag>
                </el-space>
              </template>
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="评论" name="comment" disabled>
            评论区
          </el-tab-pane>
          <el-tab-pane label="答案" name="answer">
            暂时无法查看答案
          </el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :md="12" :xs="24">
        <el-form :model="form" label-position="top">
          <el-form-item label="编程语言" prop="language" style="min-width: 240px">
            <el-select v-model="form.language" style="width: 320px" placeholder="选择编程语言">
              <el-option label="java" value="java"></el-option>
              <el-option label="cpp" value="cpp"></el-option>
              <el-option label="go" value="go"></el-option>
              <el-option label="html" value="html"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div id="code-editor" ref="codeEditorRef" style="min-height: 400px; height: 60vh" />
        <el-divider />
        <el-button type="primary" style="min-width: 200px" @click="doSubmit">
          提交代码
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps, withDefaults, watch, toRaw } from "vue";
import { ElMessage } from "element-plus";
import * as monaco from "monaco-editor";
import MdViewer from "@/components/MdViewer";
// import { QuestionControllerService, QuestionSubmitAddRequest, QuestionVO } from "../../../generated";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: "",
});

const question = ref<QuestionVO | null>(null);
const activeTab = ref("question");

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

const codeEditorRef = ref();
const codeEditor = ref<monaco.editor.IStandaloneCodeEditor | null>(null);

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(props.id);
  if (res.code === 0) {
    question.value = res.data;
  } else {
    ElMessage.error("加载失败，" + res.message);
  }
};

const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }

  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    ElMessage.success("提交成功");
  } else {
    ElMessage.error("提交失败," + res.message);
  }
};

const changeCode = (value: string) => {
  form.value.code = value;
};

watch(
    () => form.value.language,
    () => {
      if (codeEditor.value) {
        monaco.editor.setModelLanguage(
            toRaw(codeEditor.value).getModel(),
            form.value.language
        );
      }
    }
);

onMounted(() => {
  loadData();

  if (codeEditorRef.value) {
    codeEditor.value = monaco.editor.create(codeEditorRef.value, {
      value: form.value.code,
      language: form.value.language,
      automaticLayout: true,
      colorDecorators: true,
      minimap: {
        enabled: true,
      },
      readOnly: false,
      theme: "vs-dark",
    });

    codeEditor.value.onDidChangeModelContent(() => {
      form.value.code = toRaw(codeEditor.value).getValue();
    });
  }
});
</script>

<style scoped>
#viewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
}

#viewQuestionView .el-space {
  margin-bottom: 0 !important;
}
</style>
