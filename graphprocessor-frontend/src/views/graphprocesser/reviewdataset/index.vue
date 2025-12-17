<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="数据集ID" prop="datasetId">
        <el-input
          v-model="queryParams.datasetId"
          placeholder="请输入数据集ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据集名称" prop="datasetName">
        <el-input
          v-model="queryParams.datasetName"
          placeholder="请输入数据集名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-input
          v-model="queryParams.tags"
          placeholder="请输入标签"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据集大小" prop="size">
        <el-input
          v-model="queryParams.size"
          placeholder="请输入数据集大小"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据集状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择数据集状态" clearable>
          <el-option
            v-for="dict in dataset_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['graphprocesser:reviewdataset:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:reviewdataset:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:reviewdataset:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:reviewdataset:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reviewdatasetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据集ID" align="center" prop="datasetId" />
      <el-table-column label="数据集名称" align="center" prop="datasetName" />
      <el-table-column label="数据集描述" align="center" prop="description" />
      <el-table-column label="标签" align="center" prop="tags" />
      <el-table-column label="数据集存储路径" align="center" prop="datasetPath"  :show-overflow-tooltip="true"/>
      <el-table-column label="数据集大小" align="center" prop="size" />
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column label="数据集状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="dataset_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:reviewdataset:edit']">开始审核</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:reviewdataset:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改审核数据集对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="reviewdatasetRef" :model="form" :rules="rules" label-width="115px">
        <el-form-item label="数据集名称" prop="datasetName">
          <el-input v-model="form.datasetName" placeholder="请输入数据集名称" />
        </el-form-item>
        <el-form-item label="数据集描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签" />
        </el-form-item>
        <el-form-item label="数据集存储路径" prop="datasetPath">
          <file-upload v-model="form.datasetPath"/>
        </el-form-item>
        <el-form-item label="数据集大小" prop="size">
          <el-input v-model="form.size" placeholder="请输入数据集大小" />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="form.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="数据集状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择数据集状态">
            <el-option
              v-for="dict in dataset_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Reviewdataset">
import { listReviewdataset, getReviewdataset, delReviewdataset, addReviewdataset, updateReviewdataset } from "@/api/graphprocesser/reviewdataset";

const { proxy } = getCurrentInstance();
const { dataset_status } = proxy.useDict('dataset_status');

const reviewdatasetList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasetId: null,
    datasetName: null,
    description: null,
    tags: null,
    datasetPath: null,
    size: null,
    version: null,
    status: null,
  },
  rules: {
    datasetName: [
      { required: true, message: "数据集名称不能为空", trigger: "blur" }
    ],
    description: [
      { required: true, message: "数据集描述不能为空", trigger: "blur" }
    ],
    tags: [
      { required: true, message: "标签不能为空", trigger: "blur" }
    ],
    datasetPath: [
      { required: true, message: "数据集存储路径不能为空", trigger: "blur" }
    ],
    size: [
      { required: true, message: "数据集大小不能为空", trigger: "blur" }
    ],
    version: [
      { required: true, message: "版本号不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "数据集状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询审核数据集列表 */
function getList() {
  loading.value = true;
  listReviewdataset(queryParams.value).then(response => {
    reviewdatasetList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    datasetId: null,
    datasetName: null,
    description: null,
    tags: null,
    datasetPath: null,
    size: null,
    version: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("reviewdatasetRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.datasetId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加审核数据集";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _datasetId = row.datasetId || ids.value
  getReviewdataset(_datasetId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改审核数据集";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["reviewdatasetRef"].validate(valid => {
    if (valid) {
      if (form.value.datasetId != null) {
        updateReviewdataset(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addReviewdataset(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _datasetIds = row.datasetId || ids.value;
  proxy.$modal.confirm('是否确认删除审核数据集编号为"' + _datasetIds + '"的数据项？').then(function() {
    return delReviewdataset(_datasetIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/reviewdataset/export', {
    ...queryParams.value
  }, `reviewdataset_${new Date().getTime()}.xlsx`)
}

getList();
</script>
