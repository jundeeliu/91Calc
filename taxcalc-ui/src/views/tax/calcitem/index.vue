<template>
  <div class="app-container">
<!-- <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="groupName"
            placeholder="请输入项目组名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="groupOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <el-col :span="20" :xs="24"> -->


    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入项目名称"
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
          v-hasPermi="['tax:calcitem:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border 
      v-loading="loading"
      :data="calcitemList"
      row-key="itemId"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="项目名称" align="left" prop="itemName">
        <template slot-scope="scope">
          {{scope.row.itemName}} 【{{scope.row.itemGroup}}】
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" prop="remark" width="300" />
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-operation"
            @click="handleField(scope.row)"
            v-hasPermi="['tax:calcitem:edit']"
            v-if="!scope.row.children"
          >字段设置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tax:calcitem:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tax:calcitem:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
      <!-- </el-col>
</el-row> -->

    <!-- 添加或修改税费计算项目对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" label-position="top">
        <!-- <el-form-item label="归属项目组" prop="groupId">
          <treeselect v-model="form.groupId" :options="groupOptions" :show-count="true" placeholder="请选择归属项目组" />
        </el-form-item> -->
        <el-form-item label="归属项目组" prop="itemGroup">
          <el-select v-model="form.itemGroup" placeholder="请选择归属项目组">
            <el-option
              v-for="dict in itemGroupOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上级项目" prop="parentId">
          <treeselect v-model="form.parentId" :options="calcitemOptions" :normalizer="normalizer" placeholder="请选择上级项目" />
        </el-form-item>
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入项目名称" />
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

    <item-field v-if="itemFieldVisible" ref="itemField" />
  </div>
</template>

<script>
import { listCalcitem, getCalcitem, delCalcitem, addCalcitem, updateCalcitem, exportCalcitem } from "@/api/tax/calcitem";
import { treeselect } from "@/api/tax/itemgroup";
import Treeselect from "@riophae/vue-treeselect";
import ItemField from "./field";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "calcitem",
  components: {
    Treeselect,
    ItemField
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 税费计算项目表格数据
      calcitemList: [],
      // 税费计算项目树选项
      calcitemOptions: [],
      groupName:undefined,
      groupOptions:[],
      itemGroupOptions:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        itemName: null,
        parentId: null,
        groupId: null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        itemName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        parentId: [
          { required: true, message: "父ID不能为空", trigger: "blur" }
        ],
        itemGroup: [
          { required: true, message: "归属项目组不能为空", trigger: "blur" }
        ],
      },
      itemFieldVisible:false
    };
  },
  watch: {
    // 根据名称筛选项目组树
    groupName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getGroupTreeselect();
    this.getDicts("item_group").then(response => {
      this.itemGroupOptions = response.data;
    });
  },
  methods: {
    /** 查询税费计算项目列表 */
    getList() {
      this.loading = true;
      listCalcitem(this.queryParams).then(response => {
        this.calcitemList = this.handleTree(response.data, "itemId", "parentId");
        this.loading = false;
      });
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
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.groupId = data.id;
      this.getList();
    },
    /** 查询项目组下拉树结构 */
    getGroupTreeselect() {
      treeselect().then(response => {
        this.groupOptions = response.data;
      });
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
        itemId: null,
        itemName: null,
        parentId: null,
        itemGroup: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
	  this.getTreeselect();
      this.open = true;
      this.title = "添加税费计算项目";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
	  this.getTreeselect();
      if (row != null) {
        this.form.parentId = row.itemId;
      }
      getCalcitem(row.itemId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改税费计算项目";
      });
    },
    handleField(row){
      this.itemFieldVisible=true;
      this.$nextTick(()=>{
        this.$refs.itemField.open(row.itemId);
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.itemId != null) {
            updateCalcitem(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCalcitem(this.form).then(response => {
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
      this.$confirm('是否确认删除税费计算项目编号为"' + row.itemId + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCalcitem(row.itemId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    }
  }
};
</script>
