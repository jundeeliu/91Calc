<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字段标签" prop="fieldLabel">
        <el-input
          v-model="queryParams.fieldLabel"
          placeholder="请输入字段标签"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['tax:calcfield:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tax:calcfield:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tax:calcfield:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tax:calcfield:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="calcfieldList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="字段标签" align="left" prop="fieldLabel" />
      <el-table-column label="字段类型" align="center" prop="fieldType" :formatter="fieldTypeFormat" />
      <el-table-column label="字段单位" align="center" prop="fieldUnit" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tax:calcfield:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tax:calcfield:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改税费计算字段对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" label-position="top">
        <el-form-item label="字段名称(唯一)" prop="fieldName">
          <el-input v-model="form.fieldName" placeholder="请输入字段字段名称" />
        </el-form-item>
        <el-form-item label="字段标签" prop="fieldLabel">
          <el-input v-model="form.fieldLabel" placeholder="请输入字段标签" />
        </el-form-item>
        <el-form-item label="字段类型" prop="fieldType">
          <el-select v-model="form.fieldType" placeholder="请选择字段类型">
            <el-option
              v-for="dict in fieldTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字段单位" prop="fieldUnit">
          <el-input v-model="form.fieldUnit" placeholder="请输入字段单位" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { listCalcitem} from "@/api/tax/calcitem";
import { listCalcfield, getCalcfield, delCalcfield, addCalcfield, updateCalcfield, exportCalcfield } from "@/api/tax/calcfield";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";


export default {
  name: "Calcfield",
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 税费计算项目树选项
      calcitemOptions: [],
      // 税费计算字段表格数据
      calcfieldList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      fieldTypeOptions:[],
      // 是否必填字典
      isRequiredOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemId: undefined,
        fieldName: undefined,
        fieldLabel: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fieldName: [
          { required: true, message: "字段名称不能为空", trigger: "blur" }
        ],
        fieldLabel: [
          { required: true, message: "字段标签不能为空", trigger: "blur" }
        ],
        fieldType: [
          { required: true, message: "字段类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("field_type").then(response => {
      this.fieldTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询税费计算字段列表 */
    getList() {
      this.loading = true;
      listCalcfield(this.queryParams).then(response => {
        this.calcfieldList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    fieldTypeFormat(row, column){
      return this.selectDictLabel(this.fieldTypeOptions, row.fieldType);
    },
    /** 转换税费计算项目数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.itemId,
        label: node.itemName,
        children: node.children
      };
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      listCalcitem().then(response => {
        this.calcitemOptions = [];
        const data = { itemId: 0, itemName: '顶级节点', children: [] };
        data.children = this.handleTree(response.data, "itemId", "parentId");
        this.calcitemOptions.push(data);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        fieldId: undefined,
        itemId: undefined,
        fieldLabel: undefined,
        fieldType: undefined,
        isRequired: "0",
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.fieldId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.title = "添加税费计算字段";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const fieldId = row.fieldId || this.ids
      getCalcfield(fieldId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改税费计算字段";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.fieldId != null) {
            updateCalcfield(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCalcfield(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const fieldIds = row.fieldId || this.ids;
      this.$confirm('是否确认删除税费计算字段编号为"' + fieldIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCalcfield(fieldIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有税费计算字段数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCalcfield(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
