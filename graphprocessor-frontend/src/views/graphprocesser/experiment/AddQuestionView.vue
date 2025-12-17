<template>
  <div id="addQuestionView">
    <el-form :model="form" :rules="rules" ref="formRef">
      <el-form-item label="题目标题" prop="title">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" v-model="form.description" />
      </el-form-item>
      <el-form-item label="难度" prop="difficulty">
        <el-select v-model="form.difficulty" placeholder="请选择难度">
          <el-option label="简单" value="easy" />
          <el-option label="中等" value="medium" />
          <el-option label="困难" value="hard" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-select v-model="form.tags" multiple placeholder="请选择标签">
          <el-option label="动态规划" value="动态规划" />
          <el-option label="数组" value="数组" />
          <el-option label="字符串" value="字符串" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">添加题目</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

const formRef = ref(null);
const form = ref({
  title: '',
  description: '',
  difficulty: '',
  tags: []
});

const rules = {
  title: [{ required: true, message: '请输入题目标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入题目描述', trigger: 'blur' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }],
  tags: [{ required: true, message: '请选择至少一个标签', trigger: 'change' }]
};

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      ElMessage.success('题目添加成功');
    } else {
      ElMessage.error('请填写完整信息');
      return false;
    }
  });
};

const resetForm = () => {
  formRef.value.resetFields();
};
</script>

<style scoped>
#addQuestionView {
  padding: 20px;
}
</style>
