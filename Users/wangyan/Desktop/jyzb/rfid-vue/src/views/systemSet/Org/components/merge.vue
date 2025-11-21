<template>
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="dialogFormVisible"
      @close="close"
      width="540px"
    >
          <span slot="title" class="drawer-title">
              <b>合并组织机构</b>
          </span>
      <div style="padding: 0 20px">
        <div class="tips">警告：单位合并后，原有单位将消失并入新单位下，原单位账号及权限将并入新单位下；合并后的相关数据会在5分钟内合并完成，且不可撤销！</div>
        <el-form ref="formInline" :model="formInline" label-width="auto">
          <el-form-item label="要合并的组织机构" required>
            <el-select v-model="formInline.source_ids" placeholder="请选择机构" multiple clearable filterable>
                <el-option v-for="el in threeDeptList" :key="el.id" :value="el.id" :label="el.dept_name"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="合并后的组织机构" required>
            <el-cascader v-model="formInline.new_id" style="width: 100%;"
                :options="mergeList" 
                :show-all-levels="false"
                :props="{ checkStrictly: true,value:'id',label:'title',emitPath:false }" 
                clearable placeholder="选择机构"></el-cascader>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" :loading="loading" @click="save">
          保存
        </el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import {getDeptTreeThree,getDeptTreeTwo,deptMerge} from '@/api/system'
  export default {
    name: "merge",
    props:{
      
    },
    data() {
      return {
        threeDeptList:[],
        mergeList:[],
        loading: false,
        dialogFormVisible: false,
        formInline: {
          source_ids:[],
          new_id:''
        }
      };
    },
    methods: {
      showEdit() {
        this.loading = false;
        this.dialogFormVisible = true;
        this.formInline = this.$options.data().formInline;
        getDeptTreeThree().then(res=>{
            this.threeDeptList = res.data;
        })
        getDeptTreeTwo().then(res=>{
            this.mergeList = this.handleTreeList(res.data);
        })
        this.$nextTick(()=>{
          this.$refs.formInline.clearValidate();
        })
      },
      close() {
        this.dialogFormVisible = false;
      },
      save() {
        if(!this.formInline.source_ids||this.formInline.source_ids.length==0){
          this.$message.error('请选择要合并的组织机构');
          return
        }
        if(!this.formInline.new_id){
          this.$message.error('请选择合并后的组织架构');
          return
        }
        this.$confirm('此操作将不可撤销, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.loading = true;
          deptMerge(this.formInline).then(()=>{
            this.$message.success('合并成功');
            this.$emit('refreshTree');
            this.close();
          }).catch(()=>{
            this.loading = false;
          })
        })
      }
    }
  };
  </script>
  <style scoped>
    .tips{
      color: red;
      margin-bottom: 10px;
      line-height: 1.6;
      font-weight: bold;
    }
  </style>