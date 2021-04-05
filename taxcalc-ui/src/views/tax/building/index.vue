<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="省市区" prop="province">
        <el-cascader clearable size="small" :options="regionOptions" v-model="selectedOptions" @change="handleChange" @keyup.enter.native="handleQuery" style="width:230px">
        </el-cascader>
      </el-form-item>
      <el-form-item label="小区" prop="community">
        <el-input
          v-model="queryParams.community"
          placeholder="请输入小区"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="栋号" prop="buildingNo">
        <el-input
          v-model="queryParams.buildingNo"
          placeholder="请输入栋号"
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
          v-hasPermi="['tax:building:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['tax:building:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tax:building:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tax:building:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="buildingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="楼栋ID" align="center" prop="buildingId" v-if="false"/>
      <el-table-column label="省" align="left" prop="province" width="80" />
      <el-table-column label="市" align="left" prop="city" width="80" />
      <el-table-column label="区" align="left" prop="district"  width="80"/>
      <!-- <el-table-column label="地址" align="center" prop="address" /> -->
      <el-table-column label="小区" align="left" prop="community" />
      <el-table-column label="栋号" align="left" prop="buildingNo" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tax:building:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tax:building:remove']"
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

    <!-- 添加或修改楼栋信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="省市区" prop="district">
          <el-cascader :options="regionOptions" v-model="selectedRegion" @change="handleRegionChange" style="width:100%">
          </el-cascader>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="小区" prop="community">
          <el-input v-model="form.community" placeholder="请输入小区" />
        </el-form-item>
        <el-form-item label="栋号" prop="buildingNo">
          <el-input v-model="form.buildingNo" placeholder="请输入栋号" />
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
import { listBuilding, getBuilding, delBuilding, addBuilding, updateBuilding, exportBuilding } from "@/api/tax/building";

import { regionData,CodeToText, TextToCode} from 'element-china-area-data'

export default {
  name: "Building",
  components: {
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
      // 楼栋信息表格数据
      buildingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        province: undefined,
        city: undefined,
        district: undefined,
        address: undefined,
        community: undefined,
        buildingNo: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        
      },
      regionOptions: regionData,
      selectedOptions: [],
      selectedRegion:["440000", "441300", "441302"]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询楼栋信息列表 */
    getList() {
      this.loading = true;
      listBuilding(this.queryParams).then(response => {
        this.buildingList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        buildingId: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
        area: undefined,
        address: undefined,
        community: undefined,
        buildingNo: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      };
      this.resetForm("form");
      this.selectedRegion=["440000", "441300", "441302"];
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.selectedOptions=[];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.buildingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加楼栋信息";
      this.handleRegionOption();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const buildingId = row.buildingId || this.ids
      getBuilding(buildingId).then(response => {
        this.form = response.data;

        let provinceCode=TextToCode[this.form.province].code;
        let cityCode=TextToCode[this.form.province][this.form.city].code;
        let districtCode=TextToCode[this.form.province][this.form.city][this.form.district].code;
        this.selectedRegion=[];
        this.selectedRegion.push(provinceCode);
        this.selectedRegion.push(cityCode);
        this.selectedRegion.push(districtCode);
        this.open = true;
        this.title = "修改楼栋信息";
        console.log(this.selectedRegion);
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.buildingId != null) {
            updateBuilding(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBuilding(this.form).then(response => {
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
      const buildingIds = row.buildingId || this.ids;
      this.$confirm('是否确认删除楼栋信息编号为"' + buildingIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBuilding(buildingIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有楼栋信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBuilding(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    handleChange(value) {
      this.queryParams.province=CodeToText[value[0]];
      this.queryParams.city=CodeToText[value[1]];
      this.queryParams.district=CodeToText[value[2]];
    },
    handleRegionChange(value){
      this.form.province=CodeToText[value[0]];
      this.form.city=CodeToText[value[1]];
      this.form.district=CodeToText[value[2]];
    },
    handleRegionOption(){
      this.form.province=CodeToText[this.selectedRegion[0]];
      this.form.city=CodeToText[this.selectedRegion[1]];
      this.form.district=CodeToText[this.selectedRegion[2]];
    }
  }
};
</script>
