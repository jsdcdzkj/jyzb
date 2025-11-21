<template>
  <el-dialog
    :close-on-click-modal="false"
    :visible.sync="dialogFormVisible"
    @close="close"
    width="540px"
  >
        <span slot="title" class="drawer-title">
            <b>{{ formInline.id?'编辑':'新增' }}</b>
        </span>
    <div style="padding: 0 20px">
      <el-form ref="formInline" :rules="rules" :model="formInline" label-width="80px">
        <el-form-item label="名称" prop="dept_name">
          <el-input v-model="formInline.dept_name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="dept_code">
          <el-input v-model="formInline.dept_code" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="所属上级" prop="parent_dept">
          <el-select v-model="formInline.parent_dept" placeholder="请选择" clearable>
            <el-option v-for="el,index in selectList" :key="index" :label="el.title" :value="el.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" :loading="loading" @click="save('formInline')">
        保存
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {addDept,editDept} from '@/api/system'
export default {
  name: "add",
  props:{
    selectList:{
      type:Array,
      default:[]
    },
    select:{
      type:Number|String,
      default:''
    }
  },
  data() {
    return {
      loading: false,
      dialogFormVisible: false,
      formInline: {
        dept_name:'',
        dept_code:'',
        parent_dept:'',
        level:''
      },
      rules: {
        dept_name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        dept_code: [{ required: true, message: "请输入编码", trigger: "blur" }],
        parent_dept:[{ required: true, message: "请选择所属上级", trigger: "blur" }]
      }
    };
  },
  methods: {
    showEdit(row) {
      if (row.id) {
        this.formInline = {
          id:row.id,
          parent_dept:row.parent_id,
          dept_name:row.title,
          dept_code:row.code,
          level:Number(row.level)
        }
      } else {
        this.formInline = this.$options.data().formInline;
        this.formInline.parent_dept = this.select;
        this.formInline.level = Number(row.level)+1;
      }
      this.loading = false;
      this.dialogFormVisible = true;
      this.$nextTick(()=>{
        this.$refs.formInline.clearValidate();
      })
    },
    close() {
      this.dialogFormVisible = false;
    },
    save(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true;
          if(this.formInline.id){
            editDept(this.formInline).then(()=>{
              this.$message.success('操作成功');
              this.close();
              this.$emit("fetch-data");
            }).catch(() => {
              this.loading = false;
            });
          }else{
            addDept(this.formInline).then(()=>{
              this.$message.success('操作成功');
              this.close();
              this.$emit("fetch-data");
            }).catch(() => {
              this.loading = false;
            });
          }
        }
      });
    }
  }
};
</script>
