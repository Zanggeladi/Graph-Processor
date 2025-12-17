<template>
  <div id="questionsView">
    <el-row :gutter="[24, 24]">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>主题列表</span>
            <el-button style="float: right;" type="primary" @click="addQuestion">新增</el-button>
          </div>
          <el-table :data="questions" style="width: 100%" stripe>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-checkbox v-model="scope.row.statusChecked" :disabled="true">{{ scope.row.status }}</el-checkbox>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="主题名称" />
            <el-table-column prop="description" label="简介" />
            <el-table-column prop="time" label="时间" />
            <el-table-column label="操作">
              <template #default="scope">
                <el-button @click="viewQuestion(scope.row)" type="primary">查看</el-button>
                <el-button @click="editQuestion(scope.row)" type="success">编辑</el-button>
                <el-button @click="deleteQuestion(scope.row)" type="danger">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';

const questions = ref([
  { title: '第一个主题', status: '已完成', statusChecked: true, description: '第一个测试主题的描述', time: '2024-04-03 20:30:10' },
  { title: '测试主题1', status: '等待', statusChecked: false, description: '## 测试主题1描述', time: '2024-04-05 15:16:20' },
  { title: '节点分类任务的对比实验', status: '等待', statusChecked: false, description: '## 节点分类任务，是指对图中的节点进行分类的任务。', time: '2024-04-07 23:41:29' },
  { title: '主题0429', status: '等待', statusChecked: false, description: '## 节点分类 `python code ....`', time: '2024-04-29 20:29:05' },
  { title: 'Wiki数据集上的对比实验任务', status: '等待', statusChecked: false, description: '## Wiki数据集上的对比实验任务', time: '2024-05-30 00:05:15' }
]);

const router = useRouter();

const viewQuestion = (question) => {
  router.push({ name: 'ViewQuestion', params: { title: question.title } });
};

const addQuestion = () => {
  router.push({ name: 'AddQuestion' });
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
#questionsView {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}
</style>
