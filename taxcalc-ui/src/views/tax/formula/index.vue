<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目" prop="itemId">
        <treeselect v-model="queryParams.itemId" :options="calcitemOptions" :normalizer="normalizer" placeholder="请选择项目" style="width:200px" />
      </el-form-item>
      <el-form-item label="公式标签" prop="label">
        <el-input
          v-model="queryParams.label"
          placeholder="请输入公式标签"
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
          v-hasPermi="['tax:formula:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tax:formula:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tax:formula:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tax:formula:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="formulaList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公式ID" align="center" prop="formulaId" v-if="false"/>
      <el-table-column label="公式标签" align="left" prop="label" width="200" />
      <el-table-column label="优先级" align="left" prop="priority" width="80" />
      <el-table-column label="表达式" align="left" prop="expression" />
      <el-table-column label="是否启用" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tax:formula:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tax:formula:remove']"
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

    <!-- 添加或修改计算公式对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-row :gutter="20">
        <el-col :span="14">
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" label-position="top">
            <el-form-item label="计算项目" prop="itemId">
              <treeselect v-model="form.itemId" :options="calcitemOptions" :normalizer="normalizer" placeholder="请选择计算项目" @select="selectCalcItem" />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="公式标签" prop="label">
                  <el-input v-model="form.label" placeholder="请输入公式标签" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级 (使用时需按优先级排序)" prop="priority">
                  <el-input-number :min="0" v-model="form.priority" placeholder="请输入优先级" style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
            <el-form-item label="土地增值税方案">
              <el-select clearable v-model="form.zzsPlan" placeholder="请选择土地增值税方案" @change="handleZzsChange" style="width:100%">
                <el-option
                  v-for="dict in zzsPlanOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
              </el-form-item>
              </el-col>
              <el-col :span="6">
                  <el-form-item v-if="zzsConfigVisible"  label="是否有发票">
                    <el-select clearable v-model="form.zzsHasInvoice" placeholder="是否有发票" @change="handleZzsInvoiceChange" style="width:100%">
                      <el-option
                        v-for="dict in hasInvoiceOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      ></el-option>
                    </el-select>
                  </el-form-item>
              </el-col>
              <el-col :span="12" v-if="zzsInvoiceVisible && zzsConfigVisible">
                <el-form-item label="增值税表达式">
                  <span slot="label">增值税表达式（可使用结果标签)</span>
                  <el-input v-model="form.zzsAmountExpression" placeholder="请输入增值税表达式" />
                </el-form-item>
              </el-col>
              </el-row>
            </el-form-item>
            <el-row v-if="!zzsConfigVisible" :gutter="20">
              <el-col :span="12">
                <el-form-item label="计税金额表达式（使用 #E{计税金额} 标签）">
                  <span slot="label">计税金额表达式（使用
                    <el-button
                    type="text"
                    size="mini"
                    @click="handleAddToExpression('#E{计税金额}')"
                    >#E{计税金额} </el-button>
                  标签)</span>
                  <el-input v-model="form.taxAmountExpression" placeholder="请输入计税金额表达式" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="税率% (使用 #E{税率} 标签)" prop="taxRate">
                  <span slot="label">税率% (使用 
                    <el-button
                    type="text"
                    size="mini"
                    @click="handleAddToExpression('#E{税率}')"
                    >#E{税率} </el-button>
                  标签)</span>
                  <el-input-number v-model="form.taxRate" placeholder="请输入税率" :precision="5" style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item  v-if="!zzsConfigVisible" label="税额表达式 (使用右边【字段】或【其它计算结果】)" prop="expression">
              <el-input v-model="form.expression" placeholder="请输入表达式" />
            </el-form-item>
            
            <el-row v-if="zzsConfigVisible" :gutter="10">
              <el-col :span="6">
                <el-form-item label="评估金额字段" prop="zzsEvalAmountFieldName">
                <el-select clearable v-model="form.zzsEvalAmountFieldName" placeholder="请选择评估金额" style="width:100%">
                  <el-option
                    v-for="field in  filterField('N')"
                    :key="field.fieldName"
                    :label="field.fieldName"
                    :value="field.fieldName"
                  />
                </el-select>
              </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="发票金额" prop="zzsInvoiceAmountFieldName" v-if="zzsInvoiceVisible">
                <el-select clearable v-model="form.zzsInvoiceAmountFieldName" placeholder="请选择发票金额" style="width:100%">
                  <el-option
                    v-for="field in  filterField('N')"
                    :key="field.fieldName"
                    :label="field.fieldName"
                    :value="field.fieldName"
                  />
                </el-select>
              </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="发票日期字段" prop="zzsInvoiceDateFieldName" v-if="zzsInvoiceVisible">
                <el-select clearable v-model="form.zzsInvoiceDateFieldName" placeholder="请选择发票日期" style="width:100%">
                  <el-option
                    v-for="field in filterField('D')"
                    :key="field.fieldName"
                    :label="field.fieldName"
                    :value="field.fieldName"
                  />
                </el-select>
              </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="加计率%" prop="zzsAddChargeRate" v-if="zzsInvoiceVisible">
                  <el-input-number v-model="form.zzsAddChargeRate" placeholder="请输入加计率%" style="width:100%"></el-input-number>
                </el-form-item>
              </el-col>
              </el-row>
              <el-form-item  v-if="zzsConfigVisible" label="项目成本表达式 (使用右边【字段】或【其它计算结果】)" prop="expression">
                <span slot="label">项目成本表达式 (使用右边【字段】或【其它计算结果】或
                  <el-button
                    type="text"
                    size="mini"
                    @click="handleAddToExpression('#E{加计额}')"
                    >#E{加计额} </el-button> 标签)
                </span>
                <el-input v-model="form.expression" placeholder="请输入项目成本表达式" />
              </el-form-item>
              <el-form-item label="是否启用" prop="status">
                <el-select v-model="form.status" placeholder="请选择是否启用">
                  <el-option
                    v-for="dict in statusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="5">
          <el-table border :data="itemFieldList">
            <el-table-column label="字段名称" prop="fieldName" align="left">
              <template slot-scope="scope">
                <b>
                  <el-button
                    type="text"
                    size="mini"
                    @click="handleAddToExpression(scope.row.fieldName)"
                  >{{scope.row.fieldName}}</el-button>
                </b>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="5">
          <el-table border :data="formulaWithoutList">
            <el-table-column label="其它计算结果标签" prop="label" align="left">
              <template slot-scope="scope">
              <b>
                <el-button
                  type="text"
                  size="mini"
                  @click="handleAddToExpression('#{'+scope.row.label+'}')"
                >#{{'{'+scope.row.label+'}'}}</el-button>
              </b>
              </template>
            </el-table-column>
            <el-table-column label="优先级" prop="priority" align="center" />
          </el-table>
        </el-col>
      </el-row>
      
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCalcitem } from "@/api/tax/calcitem";
import { listFormula, listItemFields, getFormula, delFormula, addFormula, updateFormula, exportFormula } from "@/api/tax/formula";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Formula",
  components: {
    Treeselect
  },
  data() {
    return {
      copyContent:"",
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
      // 计算公式表格数据
      formulaList: [],
      formulaWithoutList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemId: undefined,
        label: undefined,
      },
      zzsPlanOptions:[],
      zzsConfigVisible:false,
      zzsInvoiceVisible:false,
      // 税费计算项目树选项
      calcitemOptions: [],
      // 是否启用字典
      statusOptions: [],
      //是否有发票字典
      hasInvoiceOptions:[],
      itemId:undefined,
      itemFieldList:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        itemId: [
          { required: true, message: "计费项目不能为空", trigger: "blur" }
        ],
        label:[{ required: true, message: "公式标签不能为空", trigger: "blur" }],
        expression:[{ required: true, message: "表达式不能为空", trigger: "blur" }],
        priority:[{ required: true, message: "优先级不能为空", trigger: "blur" }],
        zzsEvalAmountFieldName:[{ required: true, message: "评估金额字段不能为空", trigger: "blur" }],
        zzsInvoiceAmountFieldName:[{ required: true, message: "发票金额字段不能为空", trigger: "blur" }],
        zzsInvoiceDateFieldName:[{ required: true, message: "发票日期字段不能为空", trigger: "blur" }],
        zzsAddChargeRate:[{ required: true, message: "加计率不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("zzs_plan").then(response => {
      this.zzsPlanOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.statusOptions = response.data;
      this.hasInvoiceOptions=response.data;
    });
  },
  methods: {
    /** 查询计算公式列表 */
    getList() {
      this.loading = true;
      listFormula(this.queryParams).then(response => {
        this.formulaList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 是否启用字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    getFormulaListWithout(){
      let params={itemId:this.itemId};
      listFormula(params).then(response => {
        let data=response.rows;
        this.formulaWithoutList=data.filter(t=>t.label!=this.form.label);
      });
    },
    filterField(type) {
      return this.itemFieldList.filter(field => field.fieldType === type)
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
    selectCalcItem(val){
      // itemFieldList
      console.log(val);
      this.itemId=val.itemId;
      this.getItemFieldList();
      this.getFormulaListWithout();
    },
    getItemFieldList(){
      listItemFields(this.itemId).then(response=>{
        this.itemFieldList=response.data;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        formulaId: undefined,
        itemId: undefined,
        label: undefined,
        taxAmountExpression: undefined,
        expression: undefined,
        priority: 0,
        taxAmount:0,
        taxRate:0,
        zzsConfigVisible:false,
        zzsInvoiceVisible:false,
        zzsPlan: undefined,
        zzsInvoiceDateFieldName:undefined,
        zzsInvoiceAmountFieldName: undefined,
        zzsAddChargeRate:5
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
      this.ids = selection.map(item => item.formulaId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.getFormulaListWithout();
      this.open = true;
      this.title = "添加计算公式";
      this.zzsConfigVisible=false;
      this.zzsInvoiceVisible=false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const formulaId = row.formulaId || this.ids
      getFormula(formulaId).then(response => {
        this.form = response.data;
        this.zzsConfigVisible=response.data.zzsPlan!=null;
        this.zzsInvoiceVisible=response.data.zzsHasInvoice=="Y";
        this.open = true;
        this.title = "修改计算公式";
        this.itemId=this.form.itemId;
        this.getItemFieldList();
        this.getFormulaListWithout();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.formulaId != null) {
            updateFormula(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFormula(this.form).then(response => {
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
      const formulaIds = row.formulaId || this.ids;
      this.$confirm('是否确认删除计算公式编号为"' + formulaIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFormula(formulaIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有计算公式数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportFormula(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    handleAddToExpression(label){
      let that=this;
      // if(this.form.expression===undefined)
      //   this.form.expression="";
      // this.form.expression=this.form.expression+label;
      this.$copyText(label).then(function (e) {
        that.$message.success("已复制");
      }, function (e) {
        that.$message.error("复制失败");
        })
    },
    handleZzsChange(val){
      console.log(val);
      if(val!==""){
        this.zzsConfigVisible=true;
      }else{
        this.zzsConfigVisible=false;
      }
    },
    handleZzsInvoiceChange(val){
      if(val=="Y"){
        this.zzsInvoiceVisible=true;
      }else{
        this.zzsInvoiceVisible=false;
      }
    },
    clipboardSuccessHandler(){
      this.$message.success("已复制");
    }
  }
};
</script>
