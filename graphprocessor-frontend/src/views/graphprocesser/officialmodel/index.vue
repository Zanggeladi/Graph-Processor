<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable>
          <el-option
            v-for="dict in official_model_typoe"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="下游任务" prop="subtask">
        <el-input
          v-model="queryParams.subtask"
          placeholder="请输入下游任务"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可选数据集列表" prop="datasets">
        <el-input
          v-model="queryParams.datasets"
          placeholder="请输入可选数据集列表"
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
          v-hasPermi="['graphprocesser:officialmodel:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:officialmodel:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:officialmodel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:officialmodel:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="officialmodelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :options="official_model_typoe" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tags" />
      <el-table-column label="下游任务" align="center" prop="subtask" />
      <el-table-column label="简述" align="center" prop="shortDescription" show-overflow-tooltip />
      <el-table-column label="详述" align="center" prop="detailedDescription" show-overflow-tooltip />
      <el-table-column label="模型图片" align="center" prop="modelImgPath" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.modelImgPath" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="模型代码" align="center" prop="codePath" show-overflow-tooltip />
      <el-table-column label="参数列表" align="center" prop="parameters" />
      <el-table-column label="可选数据集列表" align="center" prop="datasets" />
      <el-table-column label="个人数据集文件格式" align="center" prop="requiredFiles" />
      <el-table-column label="数据集文件简述" align="center" prop="datasetsDescription" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:officialmodel:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:officialmodel:remove']">删除</el-button>
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

    <!-- 添加或修改官方模型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="officialmodelRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option
              v-for="dict in official_model_typoe"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签" />
        </el-form-item>
        <el-form-item label="下游任务" prop="subtask">
          <el-input v-model="form.subtask" placeholder="请输入下游任务" />
        </el-form-item>
        <el-form-item label="简述" prop="shortDescription">
          <el-input v-model="form.shortDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="详述" prop="detailedDescription">
          <el-input v-model="form.detailedDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="模型图片" prop="modelImgPath"  >
          <image-upload v-model="form.modelImgPath"/>
        </el-form-item>
        <el-form-item label="模型代码" prop="codePath">
          <el-input v-model="form.codePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="参数列表" prop="parameters">
          <el-input v-model="form.parameters" placeholder="请输入参数列表" />
        </el-form-item>
        <el-form-item label="可选数据集列表" prop="datasets">
          <el-input v-model="form.datasets" placeholder="请输入可选数据集列表" />
        </el-form-item>
        <el-form-item label="个人数据集文件格式" prop="requiredFiles">
          <el-input v-model="form.requiredFiles" placeholder="请输入个人数据集文件格式" />
        </el-form-item>
        <el-form-item label="数据集文件简述" prop="datasetsDescription">
          <el-input v-model="form.datasetsDescription" placeholder="请输入数据集文件简述" />
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

<script setup name="Officialmodel">
import { listOfficialmodel, getOfficialmodel, delOfficialmodel, addOfficialmodel, updateOfficialmodel } from "@/api/graphprocesser/officialmodel";

const { proxy } = getCurrentInstance();
const { official_model_typoe } = proxy.useDict('official_model_typoe');

const officialmodelList = ref([]);
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
    name: null,
    type: null,
    subtask: null,
    datasets: null,
  },
  rules: {
    name: [
      { required: true, message: "名称不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "类型不能为空", trigger: "change" }
    ],
    subtask: [
      { required: true, message: "下游任务不能为空", trigger: "blur" }
    ],
    codePath: [
      { required: true, message: "模型代码不能为空", trigger: "blur" }
    ],
    datasets: [
      { required: true, message: "可选数据集列表不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询官方模型列表 */
function getList() {
  loading.value = true;
  listOfficialmodel(queryParams.value).then(response => {
    officialmodelList.value = response.rows;
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
    name: null,
    type: null,
    tags: null,
    subtask: null,
    shortDescription: null,
    detailedDescription: null,
    modelImgPath: null,
    codePath: null,
    parameters: null,
    datasets: null,
    requiredFiles: null,
    datasetsDescription: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("officialmodelRef");
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
  title.value = "添加官方模型";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getOfficialmodel(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改官方模型";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["officialmodelRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateOfficialmodel(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addOfficialmodel(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除官方模型编号为"' + _ids + '"的数据项？').then(function() {
    return delOfficialmodel(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/officialmodel/export', {
    ...queryParams.value
  }, `officialmodel_${new Date().getTime()}.xlsx`)
}

getList();
</script>
