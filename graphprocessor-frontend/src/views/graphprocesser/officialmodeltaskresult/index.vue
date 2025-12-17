<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务id" prop="taskId">
        <el-input
          v-model="queryParams.taskId"
          placeholder="请输入任务id"
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
          v-hasPermi="['graphprocesser:officialmodeltaskresult:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['graphprocesser:officialmodeltaskresult:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['graphprocesser:officialmodeltaskresult:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['graphprocesser:officialmodeltaskresult:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="officialmodeltaskresultList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="任务id" align="center" prop="taskId" />
      <el-table-column label="运行输出" align="center" prop="outputMessage" show-overflow-tooltip />
      <el-table-column label="结果文件" align="center" prop="resultFilePath" />
<!--      <el-table-column label="训练曲线" align="center" prop="curvePath" />-->
      <el-table-column label="训练曲线" align="center" prop="curvePath" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.curvePath" :width="50" :height="50"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="节点嵌入可视化" align="center" prop="tsnePath" />-->
      <el-table-column label="节点嵌入可视化" align="center" prop="tsnePath" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.tsnePath" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:officialmodeltaskresult:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:officialmodeltaskresult:remove']">删除</el-button>
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

    <!-- 添加或修改官方模型图数据处理任务结果对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="officialmodeltaskresultRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务id" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务id" />
        </el-form-item>
        <el-form-item label="运行输出" prop="outputMessage">
          <el-input v-model="form.outputMessage" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="结果文件" prop="resultFilePath">
          <el-input v-model="form.resultFilePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="训练曲线" prop="curvePath">
          <el-input v-model="form.curvePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="训练曲线" prop="curvePath"  >
          <image-upload v-model="form.curvePath"/>
        </el-form-item>
        <el-form-item label="节点嵌入可视化" prop="tsnePath">
          <el-input v-model="form.tsnePath" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="节点嵌入可视化" prop="tsnePath"  >
          <image-upload v-model="form.tsnePath"/>
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

<script setup name="Officialmodeltaskresult">
import { listOfficialmodeltaskresult, getOfficialmodeltaskresult, delOfficialmodeltaskresult, addOfficialmodeltaskresult, updateOfficialmodeltaskresult } from "@/api/graphprocesser/officialmodeltaskresult";
import {COS_HOST} from "@/constants/index.js";

const { proxy } = getCurrentInstance();

const officialmodeltaskresultList = ref([]);
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
    taskId: null,
    outputMessage: null,
  },
  rules: {
    taskId: [
      { required: true, message: "任务id不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询官方模型图数据处理任务结果列表 */
function getList() {
  loading.value = true;
  listOfficialmodeltaskresult(queryParams.value).then(response => {
    officialmodeltaskresultList.value = response.rows;
    // 对每一条数据 的curvePath 添加cos前缀
    officialmodeltaskresultList.value.forEach(item => {
      item.curvePath = COS_HOST + item.curvePath;
    });
    officialmodeltaskresultList.value.forEach(
        item => {
          item.tsnePath = COS_HOST + item.tsnePath;
        }
    )
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
    taskId: null,
    outputMessage: null,
    resultFilePath: null,
    curvePath: null,
    tsnePath: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("officialmodeltaskresultRef");
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
  title.value = "添加官方模型图数据处理任务结果";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getOfficialmodeltaskresult(_id).then(response => {
    // response.data.curvePath = COS_HOST + response.data.curvePath;
    // response.data.tsnePath = COS_HOST + response.data.tsnePath;
    form.value = response.data;
    open.value = true;
    title.value = "修改官方模型图数据处理任务结果";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["officialmodeltaskresultRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateOfficialmodeltaskresult(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addOfficialmodeltaskresult(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除官方模型图数据处理任务结果编号为"' + _ids + '"的数据项？').then(function() {
    return delOfficialmodeltaskresult(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/officialmodeltaskresult/export', {
    ...queryParams.value
  }, `officialmodeltaskresult_${new Date().getTime()}.xlsx`)
}

getList();
</script>
