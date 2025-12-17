<template>
  <div id="ThemeDetail">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 240px">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
        :ref="tableRef"
        :columns="columns"
        :data="dataList"
        :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
        @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green"
          >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${
              record.submitNum ? record.acceptedNum / record.submitNum : "0"
          }% (${record.acceptedNum}/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestionPage(record)">
            做题
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { onMounted, ref, watchEffect } from "vue";

import { register } from "@/api/login";
import { ElMessage } from 'element-plus';
import * as querystring from "querystring";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref({
  title: "",
  tags: [],
  pageSize: 8,
  current: 1,
});

const loadData = async () => {
  const res = await register().listQuestionVoByPageUsingPost(
      searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    ElMessage({
      message: 'This is a message from a method',
      type: 'info',
    });
  }
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

// {id: "1", title: "A+ D", content: "新的题目内容", tags: "["二叉树"]", answer: "新的答案", submitNum: 0,…}

const columns = [
  {
    title: "Id",
    dataIndex: "id",
  },
  {
    title: "主题名称",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "创建时间",
    slotName: "acceptedRate",
  },
  {
    title: "更新时",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];

const onPageChange = (page) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const router = useRouter();

/**
 * 跳转到
 */
const toQuestionPage = (question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

/**
 * 确认搜索，重新加载数据
 */
const doSubmit = () => {
  // 这里需要重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>

<style scoped>
#ThemeDetail {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
