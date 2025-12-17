<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主题id" prop="themeId">
        <el-input
          v-model="queryParams.themeId"
          placeholder="请输入主题id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据集id" prop="datasetId">
        <el-input
          v-model="queryParams.datasetId"
          placeholder="请输入数据集id"
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
          v-hasPermi="['graphprocesser:experimenttask:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:experimenttask:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:experimenttask:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:experimenttask:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="experimenttaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="任务名称" align="center" prop="taskName" />
      <el-table-column label="任务描述" align="center" prop="description" />
      <el-table-column label="主题id" align="center" prop="themeId" />
      <el-table-column label="数据集id" align="center" prop="datasetId" />
<!--      <el-table-column label="模型代码" align="center" prop="modeCode" show-overflow-tooltip />-->
      <el-table-column label="模型代码" align="center" prop="modeCode">
        <template #default="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.modeCode" placement="top">
            <span>{{ scope.row.modeCode.length > 10 ? scope.row.modeCode.slice(0, 10) + '...' : scope.row.modeCode }}</span>
          </el-tooltip>
          <el-button type="text" icon="View" @click="viewFullCode(scope.row)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="实验超参数" align="center" prop="hyperparameters" />
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="task_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="执行信息" align="center" prop="execMessage" show-overflow-tooltip />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:experimenttask:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:experimenttask:remove']">删除</el-button>
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

<!--    <el-dialog title="完整模型代码" v-model="showCodeDialog" width="50%">-->
<!--      <el-input type="textarea" v-model="selectedCode" rows="10" readonly />-->
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="showCodeDialog = false">关闭</el-button>-->
<!--      </span>-->
<!--    </el-dialog>-->
    <el-dialog v-model="showCodeDialog" width="50%">
<!--      <el-input type="textarea" v-model="selectedCode" rows="10" readonly />-->
      <MdPreview :editor-id="selectedTask.id" :modelValue="'```\n' + selectedTask.code"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showCodeDialog = false">关闭</el-button>
      </span>
    </el-dialog>


    <!-- 添加或修改对比实验任务对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="experimenttaskRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主题id" prop="themeId">
          <el-input v-model="form.themeId" placeholder="请输入主题id" />
        </el-form-item>
        <el-form-item label="数据集id" prop="datasetId">
          <el-input v-model="form.datasetId" placeholder="请输入数据集id" />
        </el-form-item>
        <el-form-item label="模型代码" prop="modeCode">
          <el-input v-model="form.modeCode" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="实验超参数" prop="hyperparameters">
          <el-input v-model="form.hyperparameters" type="textarea" placeholder="请输入内容" />
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
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-divider content-position="center">实验结果信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="Plus" @click="handleAddTbExperimentResult">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="Delete" @click="handleDeleteTbExperimentResult">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="tbExperimentResultList" :row-class-name="rowTbExperimentResultIndex" @selection-change="handleTbExperimentResultSelectionChange" ref="tbExperimentResult">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
        </el-table>
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

<script setup name="Experimenttask">
import { listExperimenttask, getExperimenttask, delExperimenttask, addExperimenttask, updateExperimenttask } from "@/api/graphprocesser/experimenttask";
import {MdPreview} from "md-editor-v3";

const { proxy } = getCurrentInstance();
const { task_status } = proxy.useDict('task_status');

const experimenttaskList = ref([]);
const tbExperimentResultList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const checkedTbExperimentResult = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const showCodeDialog = ref(false);
const selectedTask = ref("");

const viewFullCode = (task) => {
  selectedTask.value = task;
  showCodeDialog.value = true;
};

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    taskName: null,
    themeId: null,
    datasetId: null,
    status: null,
    userId: null,
  },
  rules: {
    taskName: [
      { required: true, message: "任务名称不能为空", trigger: "blur" }
    ],
    themeId: [
      { required: true, message: "主题id不能为空", trigger: "blur" }
    ],
    datasetId: [
      { required: true, message: "数据集id不能为空", trigger: "blur" }
    ],
    modeCode: [
      { required: true, message: "模型代码不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "任务状态不能为空", trigger: "change" }
    ],
    userId: [
      { required: true, message: "用户id不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询对比实验任务列表 */
function getList() {
  loading.value = true;
  listExperimenttask(queryParams.value).then(response => {
    experimenttaskList.value = response.rows;
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
    taskName: null,
    description: null,
    themeId: null,
    datasetId: null,
    modeCode: null,
    hyperparameters: null,
    status: null,
    execMessage: null,
    userId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  tbExperimentResultList.value = [];
  proxy.resetForm("experimenttaskRef");
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
  title.value = "添加对比实验任务";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getExperimenttask(_id).then(response => {
    form.value = response.data;
    tbExperimentResultList.value = response.data.tbExperimentResultList;
    open.value = true;
    title.value = "修改对比实验任务";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["experimenttaskRef"].validate(valid => {
    if (valid) {
      form.value.tbExperimentResultList = tbExperimentResultList.value;
      if (form.value.id != null) {
        updateExperimenttask(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addExperimenttask(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除对比实验任务编号为"' + _ids + '"的数据项？').then(function() {
    return delExperimenttask(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 实验结果序号 */
function rowTbExperimentResultIndex({ row, rowIndex }) {
  row.index = rowIndex + 1;
}

/** 实验结果添加按钮操作 */
function handleAddTbExperimentResult() {
  let obj = {};
  obj.metrics = "";
  obj.resultFilePath = "";
  obj.remark = "";
  tbExperimentResultList.value.push(obj);
}

/** 实验结果删除按钮操作 */
function handleDeleteTbExperimentResult() {
  if (checkedTbExperimentResult.value.length == 0) {
    proxy.$modal.msgError("请先选择要删除的实验结果数据");
  } else {
    const tbExperimentResults = tbExperimentResultList.value;
    const checkedTbExperimentResults = checkedTbExperimentResult.value;
    tbExperimentResultList.value = tbExperimentResults.filter(function(item) {
      return checkedTbExperimentResults.indexOf(item.index) == -1
    });
  }
}

/** 复选框选中数据 */
function handleTbExperimentResultSelectionChange(selection) {
  checkedTbExperimentResult.value = selection.map(item => item.index)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/experimenttask/export', {
    ...queryParams.value
  }, `experimenttask_${new Date().getTime()}.xlsx`)
}

getList();
</script>
