<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主题名称" prop="themeName">
        <el-input
            v-model="queryParams.themeName"
            placeholder="请输入对比实验主题名称"
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
            v-hasPermi="['graphprocesser:experimenttheme:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['graphprocesser:experimenttheme:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['graphprocesser:experimenttheme:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['graphprocesser:experimenttheme:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="experimentthemeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" />
      <el-table-column label="对比实验主题名称" align="center" prop="themeName" />
<!--      <el-table-column label="描述信息" align="center" prop="description" show-overflow-tooltip />-->
      <el-table-column label="描述信息" align="center" prop="description" show-overflow-tooltip>
        <template #default="scope">
          <span>{{ scope.row.description }}</span>
          <el-button type="primary" size="mini" icon="Search" @click="handleViewDescription(scope.row)">详情</el-button>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['graphprocesser:experimenttheme:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['graphprocesser:experimenttheme:remove']">删除</el-button>
          <el-button link type="primary" icon="Search" @click="handleView(scope.row)">详情</el-button>
<!--          <el-button type="text" icon="Refresh" @click="console.log(scope.row)" >test</el-button>-->
<!--          <router-link :to="`/experimenttheme/${scope.row.id}`">-->
<!--            <el-button link type="primary" icon="Search">详情</el-button>-->
<!--          </router-link>-->
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

    <!-- 添加或修改对比实验主题对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="experimentthemeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="对比实验主题名称" prop="themeName">
          <el-input v-model="form.themeName" placeholder="请输入对比实验主题名称" />
        </el-form-item>
        <el-form-item label="对比实验主题描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="关联的用户id，引用sys_user表(id)" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入关联的用户id，引用sys_user表(id)" />
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

<script setup name="Experimenttheme">
import { listExperimenttheme, getExperimenttheme, delExperimenttheme, addExperimenttheme, updateExperimenttheme } from "@/api/graphprocesser/experimenttheme";

const { proxy } = getCurrentInstance();

const experimentthemeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const router = useRouter();

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    themeName: null,
    description: null,
    userId: null,
  },
  rules: {
    themeName: [
      { required: true, message: "对比实验主题名称不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询对比实验主题列表 */
function getList() {
  loading.value = true;
  listExperimenttheme(queryParams.value).then(response => {
    experimentthemeList.value = response.rows;
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
    themeName: null,
    description: null,
    userId: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("experimentthemeRef");
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
  title.value = "添加对比实验主题";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getExperimenttheme(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改对比实验主题";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["experimentthemeRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateExperimenttheme(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addExperimenttheme(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除对比实验主题编号为"' + _ids + '"的数据项？').then(function() {
    return delExperimenttheme(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

// 查看详情
function handleView(row) {
  const _id = row.id || ids.value
  console.log(_id)
  router.push({ path: `/experimenttheme/${_id}` })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('graphprocesser/experimenttheme/export', {
    ...queryParams.value
  }, `experimenttheme_${new Date().getTime()}.xlsx`)
}

getList();
</script>
