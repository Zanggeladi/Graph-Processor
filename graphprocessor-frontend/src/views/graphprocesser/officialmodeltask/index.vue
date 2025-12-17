<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型id" prop="officialModelId">
        <el-input
          v-model="queryParams.officialModelId"
          placeholder="请输入模型id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据集类型" prop="datasetType">
        <el-select v-model="queryParams.datasetType" placeholder="请选择数据集类型" clearable>
          <el-option
            v-for="dict in dataset_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据集名称" prop="datasetName">
        <el-input
          v-model="queryParams.datasetName"
          placeholder="请输入数据集名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
          <el-option
            v-for="dict in task_status"
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
          v-hasPermi="['graphprocesser:officialmodeltask:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:officialmodeltask:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:officialmodeltask:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:officialmodeltask:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="officialmodeltaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" />
      <el-table-column label="模型id" align="center" prop="officialModelId" />
      <el-table-column label="数据集类型" align="center" prop="datasetType">
        <template #default="scope">
          <dict-tag :options="dataset_type" :value="scope.row.datasetType"/>
        </template>
      </el-table-column>
      <el-table-column label="数据集名称" align="center" prop="datasetName" />
      <el-table-column label="个人数据集路径" align="center" prop="datasetPath" />
      <el-table-column label="参数" align="center" prop="parameters" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="task_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="执行信息" align="center" prop="execMessage" show-overflow-tooltip />
      <el-table-column label="结果id" align="center" prop="resultId" />
      <el-table-column label="结果路径" align="center" prop="resultPath" />
      <el-table-column label="image路径" align="center" prop="imgPath" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:officialmodeltask:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:officialmodeltask:remove']">删除</el-button>
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

    <!-- 添加或修改官方模型实验任务对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="officialmodeltaskRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模型id" prop="officialModelId">
          <el-input v-model="form.officialModelId" placeholder="请输入模型id" />
        </el-form-item>
        <el-form-item label="数据集类型" prop="datasetType">
          <el-select v-model="form.datasetType" placeholder="请选择数据集类型">
            <el-option
              v-for="dict in dataset_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据集名称" prop="datasetName">
          <el-input v-model="form.datasetName" placeholder="请输入数据集名称" />
        </el-form-item>
        <el-form-item label="个人数据集路径" prop="datasetPath">
          <el-input v-model="form.datasetPath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="参数" prop="parameters">
          <el-input v-model="form.parameters" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择任务状态">
            <el-option
              v-for="dict in task_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行信息" prop="execMessage">
          <el-input v-model="form.execMessage" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="结果id" prop="resultId">
          <el-input v-model="form.resultId" placeholder="请输入结果id" />
        </el-form-item>
        <el-form-item label="结果路径" prop="resultPath">
          <el-input v-model="form.resultPath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="image路径" prop="imgPath">
          <el-input v-model="form.imgPath" type="textarea" placeholder="请输入内容" />
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

<script setup name="Officialmodeltask">
import { listOfficialmodeltask, getOfficialmodeltask, delOfficialmodeltask, addOfficialmodeltask, updateOfficialmodeltask } from "@/api/graphprocesser/officialmodeltask";

const { proxy } = getCurrentInstance();
const { task_status, dataset_type } = proxy.useDict('task_status', 'dataset_type');

const officialmodeltaskList = ref([]);
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
    officialModelId: null,
    datasetType: null,
    datasetName: null,
    userId: null,
    status: null,
  },
  rules: {
    officialModelId: [
      { required: true, message: "模型id不能为空", trigger: "blur" }
    ],
    datasetType: [
      { required: true, message: "数据集类型不能为空", trigger: "change" }
    ],
    userId: [
      { required: true, message: "用户id不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "任务状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询官方模型实验任务列表 */
function getList() {
  loading.value = true;
  listOfficialmodeltask(queryParams.value).then(response => {
    officialmodeltaskList.value = response.rows;
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
    officialModelId: null,
    datasetType: null,
    datasetName: null,
    datasetPath: null,
    parameters: null,
    userId: null,
    status: null,
    execMessage: null,
    resultId: null,
    resultPath: null,
    imgPath: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("officialmodeltaskRef");
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
  title.value = "添加官方模型实验任务";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getOfficialmodeltask(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改官方模型实验任务";
  });
}

/** 提交按钮 */
function submitForm() {
  console.log(form.value)
  proxy.$refs["officialmodeltaskRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateOfficialmodeltask(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addOfficialmodeltask(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除官方模型实验任务编号为"' + _ids + '"的数据项？').then(function() {
    return delOfficialmodeltask(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/officialmodeltask/export', {
    ...queryParams.value
  }, `officialmodeltask_${new Date().getTime()}.xlsx`)
}

getList();
</script>
