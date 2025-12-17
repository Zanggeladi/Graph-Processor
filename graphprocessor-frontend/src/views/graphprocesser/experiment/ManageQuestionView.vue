<template>
  <div id="manageQuestionView">
    <el-row :gutter="[24, 24]">
      <el-col :md="24" :xs="24">
        <el-table :data="questions" style="width: 100%">
          <el-table-column prop="title" label="题目" />
          <el-table-column prop="difficulty" label="难度" />
          <el-table-column prop="tags" label="标签" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="editQuestion(scope.row)" type="primary">编辑</el-button>
              <el-button @click="deleteQuestion(scope.row)" type="danger">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';

const questions = ref([
  { title: '示例题目1', difficulty: '简单', tags: ['动态规划', '数组'] },
  { title: '示例题目2', difficulty: '中等', tags: ['字符串', '哈希表'] }
]);

const editQuestion = (question) => {
  // 跳转到编辑页面或显示编辑对话框
  console.log('编辑题目:', question);
};

const deleteQuestion = (question) => {
  ElMessageBox.confirm('确定要删除此题目吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 删除题目逻辑
    questions.value = questions.value.filter(q => q !== question);
    ElMessage.success('删除成功');
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};
</script>

<style scoped>
#manageQuestionView {
  padding: 20px;
}
</style>
