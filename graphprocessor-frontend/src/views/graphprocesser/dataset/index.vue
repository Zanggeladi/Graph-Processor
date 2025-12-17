<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="datasetName">
        <el-input
          v-model="queryParams.datasetName"
          placeholder="请输入名称"
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
      <el-form-item label="版本号" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dataset_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['graphprocesser:dataset:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:dataset:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:dataset:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:dataset:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="datasetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="datasetName" />
      <el-table-column label="描述信息" align="center" prop="description" />
      <el-table-column label="标签" align="center" prop="tags" />
      <el-table-column label="存储路径" align="center" prop="datasetPath" />
      <el-table-column label="数据集大小" align="center" prop="size" />
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="dataset_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:dataset:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:dataset:remove']">删除</el-button>
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

    <!-- 添加或修改数据集对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="datasetRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="datasetName">
          <el-input v-model="form.datasetName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签" />
        </el-form-item>
        <el-form-item label="存储路径" prop="datasetPath">
          <file-upload v-model="form.datasetPath"/>
        </el-form-item>
        <el-form-item label="数据集大小" prop="size">
          <el-input v-model="form.size" placeholder="请输入数据集大小" />
        </el-form-item>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="form.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dataset_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
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

<script setup name="Dataset">
import { listDataset, getDataset, delDataset, addDataset, updateDataset } from "@/api/graphprocesser/dataset";

const { proxy } = getCurrentInstance();
const { dataset_status } = proxy.useDict('dataset_status');

const datasetList = ref([]);
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
    datasetName: null,
    tags: null,
    version: null,
    status: null,
    userId: null,
  },
  rules: {
    datasetName: [
      { required: true, message: "名称不能为空", trigger: "blur" }
    ],
    datasetPath: [
      { required: true, message: "存储路径不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    userId: [
      { required: true, message: "用户id不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询数据集列表 */
function getList() {
  loading.value = true;
  listDataset(queryParams.value).then(response => {
    datasetList.value = response.rows;
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
    id: null,
    datasetName: null,
    description: null,
    tags: null,
    datasetPath: null,
    size: null,
    version: null,
    status: null,
    userId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("datasetRef");
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
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加数据集";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getDataset(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改数据集";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["datasetRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateDataset(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addDataset(form.value).then(response => {
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
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除数据集编号为"' + _ids + '"的数据项？').then(function() {
    return delDataset(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/dataset/export', {
    ...queryParams.value
  }, `dataset_${new Date().getTime()}.xlsx`)
}

getList();
</script>
