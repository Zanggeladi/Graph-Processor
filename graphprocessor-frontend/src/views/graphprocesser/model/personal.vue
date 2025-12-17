<template>
  <div class="app-container">
    <div style="height: 500px;">
      <CodeEditor v-model="form.modelCode" language="python" theme="vs-dark" />
    </div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型ID" prop="modelId">
        <el-input
          v-model="queryParams.modelId"
          placeholder="请输入模型ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input
          v-model="queryParams.modelName"
          placeholder="请输入模型名称"
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
          v-hasPermi="['graphprocesser:model:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:model:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:model:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:model:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模型ID" align="center" prop="modelId" />
      <el-table-column label="模型名称" align="center" prop="modelName" />
<!--      模型代码过长时显示省略号-->
      <el-table-column label="模型代码" align="center" prop="modelCode" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="登录时间" align="center" prop="loginTime" width="180">-->
<!--        <template #default="scope">-->
<!--          <span>{{ parseTime(scope.row.loginTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:model:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:model:remove']">删除</el-button>
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

    <!-- 添加或修改模型对话框 -->
<!--    <el-dialog :title="title" v-model="open" width="500px" append-to-body>-->
<!--      <el-form ref="modelRef" :model="form" :rules="rules" label-width="80px">-->
<!--        <el-form-item label="模型名称" prop="modelName">-->
<!--          <el-input v-model="form.modelName" placeholder="请输入模型名称" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="模型代码" prop="modelCode">-->
<!--          <el-input v-model="form.modelCode" type="textarea" placeholder="请输入内容" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <template #footer>-->
<!--        <div class="dialog-footer">-->
<!--          <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--          <el-button @click="cancel">取 消</el-button>-->
<!--        </div>-->
<!--      </template>-->
<!--    </el-dialog>-->
    <el-drawer
        v-model="open"
        :title="title"
        :before-close="cancel"
        direction="rtl"
        class="demo-drawer"
        size="50%"
    >
      <div class="demo-drawer__content">
        <el-form ref="modelRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="模型名称" prop="modelName">
            <el-input v-model="form.modelName" placeholder="请输入模型名称" />
          </el-form-item>
          <el-form-item label="模型代码" prop="modelCode">
<!--            <el-input v-model="form.modelCode" type="textarea" placeholder="请输入内容" />-->

            <template #default>
              <CodeEditor
                  :value="form.modelCode"
                  :language="form.language"
                  :handle-change="changeCode"
                  height="75vh"
                  width="100vw"
              />
            </template>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="demo-drawer__footer">
          <el-button @click="cancel">Cancel</el-button>
          <el-button type="primary" :loading="loading" @click="submitForm">
            {{ loading_drawer ? 'Submitting ...' : 'Submit' }}
          </el-button>
        </div>
      </template>
    </el-drawer>

  </div>
</template>

<script setup>
import { listModel, getModel, delModel, addModel, updateModel } from "@/api/graphprocesser/model";
import CodeEditor from "@/components/CodeEditor";

const { proxy } = getCurrentInstance();

const modelList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const loading_drawer = ref(false);


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    modelId: null,
    modelName: null,
    modelCode: null,
  },
  rules: {
    modelName: [
      { required: true, message: "模型名称不能为空", trigger: "blur" }
    ],
    // modelCode: [
    //   { required: true, message: "模型代码不能为空", trigger: "blur" }
    // ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询模型列表 */
function getList() {
  loading.value = true;
  listModel(queryParams.value).then(response => {
    modelList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

const changeCode = (value) => {
  form.value.modelCode = value;
};

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    modelId: null,
    modelName: null,
    modelCode: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("modelRef");
  console.log("reset完成",form.value)
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
  ids.value = selection.map(item => item.modelId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加模型";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _modelId = row.modelId || ids.value
  getModel(_modelId).then(response => {
    console.log(response)
    form.value = response.data;
    open.value = true;
    title.value = "修改模型";
    console.log("handleUpdate",form.value)
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["modelRef"].validate(valid => {
    if (valid) {
      if (form.value.modelId != null) {
        updateModel(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addModel(form.value).then(response => {
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
  const _modelIds = row.modelId || ids.value;
  proxy.$modal.confirm('是否确认删除模型编号为"' + _modelIds + '"的数据项？').then(function() {
    return delModel(_modelIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/model/export', {
    ...queryParams.value
  }, `model_${new Date().getTime()}.xlsx`)
}

getList();
</script>
