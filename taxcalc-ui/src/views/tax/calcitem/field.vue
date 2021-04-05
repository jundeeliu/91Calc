<template>
    <el-dialog :close-on-click-modal="false" :title="title" v-if="openField" :visible.sync="openField" width="500px" append-to-body @close="closeDialog">
      <el-table border v-loading="loading" :data="fieldList" @select="handleSelectionChange"
        @select-all="handleSelectionChange"
        :row-key="(row)=>{ return row.fieldId}"
      ref="multipleTable">
          <el-table-column :reserve-selection="true" type="selection" width="50" align="center" />
          <el-table-column label="字段标签" align="left" prop="fieldLabel" />
          <el-table-column label="字段类型" align="center" prop="fieldType" :formatter="fieldTypeFormat" />
          <el-table-column label="是否必填">
              <template slot-scope="scope">
                  <el-select v-model="scope.row.isRequired">
                    <el-option
                        v-for="dict in isRequiredOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                        />
                </el-select>
              </template>
          </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </el-dialog>
</template>

<script>
import { listCalcfield } from "@/api/tax/calcfield";
import { updateItemField,listItemField, getCalcitem } from "@/api/tax/calcitem";
export default {
    data(){
        return{
            openField:false,
            itemId:undefined,
            title:'设置税费字段',
            loading:false,
            fieldList:[],
            fieldTypeOptions:[],
            isRequiredOptions:[],
            selectedFields:[],
            calcitem:{},
            itemFields:[]
        }
    },
    created(){
      this.getDicts("sys_yes_no").then(response => {
        this.isRequiredOptions = response.data;
      });
      this.getDicts("field_type").then(response => {
        this.fieldTypeOptions = response.data;
      });
    },
    methods:{
        open(itemId){
            this.itemId=itemId;
            this.openField=true;
            this.init();
            
        },
        init(){
            this.getCalcitemInfo();
            
        },
        getCalcitemInfo(){
            getCalcitem(this.itemId).then(response=>{
                this.calcitem=response.data;
                this.itemFields=response.itemFields;

                this.getFieldList();
            });
        },
        getFieldList(){
            this.loading = true;
            listCalcfield().then(response=>{
                for(let i of response.rows){
                    i.itemId=this.itemId;
                    i.isRequired='N';
                    if(this.itemFields.length>0){
                        let obj = this.itemFields.find(o => o.fieldId === i.fieldId);
                        if(obj!==undefined){
                            i.isRequired = obj.isRequired;
                            this.$refs.multipleTable.toggleRowSelection(i);
                            this.selectedFields.push(i);
                        }
                    }
                }
                this.fieldList=response.rows;
                this.loading = false;
            });
        },
        fieldTypeFormat(row, column){
            return this.selectDictLabel(this.fieldTypeOptions, row.fieldType);
        },
        // 是否必填字典翻译
        isRequiredFormat(row, column) {
            return this.selectDictLabel(this.isRequiredOptions, row.isRequired);
        },
        handleSelectionChange(selection) {
            this.selectedFields=selection.map(item=>item);
            this.single = selection.length!==1
            this.multiple = !selection.length;
        },
        close() {
            this.openField = false;
            this.fieldList=[];
            this.itemFields=[];
            this.selectedFields=[];
            this.$refs.multipleTable.clearSelection();
        },
        closeDialog(){
            this.fieldList=[];
            this.itemFields=[];
            this.selectedFields=[];
            this.$refs.multipleTable.clearSelection();
        },
        submitForm(){
            updateItemField(this.itemId, this.selectedFields).then(response=>{
                if (response.code === 200) {
                    this.msgSuccess("设置成功");
                    this.close();
                } else {
                    this.msgError(response.msg);
                }
            });
        }
    }
}
</script>