<template>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="提交时间">
          <el-date-picker
            v-model="daterangeCreateTime"
            size="small"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
  
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['tax:report:edit']"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['tax:report:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['tax:report:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table border v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户openid" align="left" prop="openid" show-overflow-tooltip width="220"/>
        <el-table-column label="地址" align="left" prop="address" />
        <el-table-column label="面积(元)" align="right" prop="area" />
        <el-table-column label="评估单价(元)" align="right" prop="evalPrice" />
        <el-table-column label="评估金额(元)" align="right" prop="evalAmount" />
        <el-table-column label="票据图片" align="left" prop="imageFiles">
            <template slot-scope="scope">
                <ul class="img-ul">
                    <li class="img-li" v-for="json in JSON.parse(scope.row.imageFiles)" :key="json"><el-image :src="getImageUrl(json)" :preview-src-list="getImages(scope.row.imageFiles)" style="width:100%; height:100%;"/></li>
                </ul>
                
            </template>
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="createTime" />
        <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormat" width="80" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <!-- <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['tax:report:edit']"
            >修改</el-button> -->
            <el-button
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleAudit(scope.row)"
              v-hasPermi="['tax:report:audit']"
            v-if="scope.row.status=='N'">审核</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['tax:report:remove']"
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
  
      <!-- 添加或修改上报地税评估对话框 -->
      <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address" placeholder="请输入地址" />
          </el-form-item>
          <el-form-item label="面积" prop="area">
            <el-input v-model="form.area" placeholder="请输入面积" />
          </el-form-item>
          <el-form-item label="评估单价" prop="evalPrice">
            <el-input v-model="form.evalPrice" placeholder="请输入评估单价" />
          </el-form-item>
          <el-form-item label="评估金额" prop="evalAmount">
            <el-input v-model="form.evalAmount" placeholder="请输入评估金额" />
          </el-form-item>
          <el-form-item label="图片地址" prop="imageFiles">
            <el-input v-model="form.imageFiles" type="textarea" placeholder="请输入内容" />
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

      <el-dialog width="400px" :visible.sync="imgVisible" class="img-dialog">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="dialogImgUrl" width="100%" height="100%">
        </el-card>
      </el-dialog>
    </div>
  </template>

  <style scoped>
      .img-ul, .img-li{
          list-style:none;
          list-style-type: none;
      }

      .img-li{
          float: left;
          margin:5px;
          width:50px; 
          height:50px;
      }
  </style>
  
  <script>
  import { listReport, getReport, delReport, addReport, updateReport, exportReport, auditReport } from "@/api/tax/report";
  
  export default {
    name: "Report",
    components: {
    },
    data() {
      return {
        imgVisible:false,
        dialogImgUrl:'',
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
        // 上报地税评估表格数据
        reportList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 审核状态(N未审 Y已审)字典
        statusOptions: [],
        // 创建时间时间范围
        daterangeCreateTime: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          openid: undefined,
          address: undefined,
          area: undefined,
          evalPrice: undefined,
          evalAmount: undefined,
          imageFiles: undefined,
          createTime: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          openid: [
            { required: true, message: "用户openid不能为空", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_yes_no").then(response => {
        this.statusOptions = response.data;
        });
    },
    methods: {
      /** 查询上报地税评估列表 */
      getList() {
        this.loading = true;
        this.queryParams.params = {};
        if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
          this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
          this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
        }
        listReport(this.queryParams).then(response => {
          this.reportList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 审核状态(N未审 Y已审)字典翻译
        statusFormat(row, column) {
            return this.selectDictLabel(this.statusOptions, row.status);
        },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          reportId: undefined,
          openid: undefined,
          address: undefined,
          area: undefined,
          evalPrice: undefined,
          evalAmount: undefined,
          imageFiles: undefined,
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
        this.daterangeCreateTime = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.reportId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加上报地税评估";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const reportId = row.reportId || this.ids
        getReport(reportId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改上报地税评估";
        });
      },
      //审核
      handleAudit(row) {
        const reportIds = row.reportId || this.ids;
        this.$confirm('是否审核该笔记录?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return auditReport(reportIds);
          }).then(() => {
            this.getList();
            this.msgSuccess("审核成功");
          })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.reportId != null) {
              updateReport(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addReport(this.form).then(response => {
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
        const reportIds = row.reportId || this.ids;
        this.$confirm('是否确认删除上报地税评估编号为"' + reportIds + '"的数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delReport(reportIds);
          }).then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有上报地税评估数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return exportReport(queryParams);
          }).then(response => {
            this.download(response.msg);
          })
      },
      getImageUrl(filePath) {
        let imageUrl=process.env.VUE_APP_BASE_API +filePath;
        return imageUrl;
      },
      getImages(imageFiles){
        let files=JSON.parse(imageFiles);
        let list=[];
        files.forEach((item, index)=>{
            let filePath=this.getImageUrl(item);
            list.push(filePath);
        })
        return list;
      }
    }
  };
  </script>