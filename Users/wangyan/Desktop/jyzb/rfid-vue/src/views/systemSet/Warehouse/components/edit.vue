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
        <el-form-item label="名称" prop="warehouse_name">
          <el-input v-model="formInline.warehouse_name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="warehouse_code">
          <el-input v-model="formInline.warehouse_code" placeholder="请输入编码"></el-input>
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
import {addWarehouse,editWarehouse} from '@/api/system'
export default {
  name: "add",
  data() {
    return {
      loading: false,
      dialogFormVisible: false,
      deptList:[],
      formInline: {
        warehouse_name:'',
        warehouse_code:'',
        dept_id:'',
      },
      rules: {
        warehouse_name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        warehouse_code: [{ required: true, message: "请输入编码", trigger: "blur" }],
        dept_id: [{ required: true, message: "请选择所属机构", trigger: "change" }],
      }
    };
  },
  mounted(){
    
  },
  methods: {
    showEdit(row) {
      if (row) {
        this.formInline = Object.assign({}, row);
      } else {
        this.formInline = this.$options.data().formInline;
        this.formInline.dept_id = this.$store.state.user.department;
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
            editWarehouse(this.formInline).then(()=>{
              this.$message.success('操作成功');
              this.close();
              this.$emit("fetch-data");
            }).catch(() => {
              this.loading = false;
            });
          }else{
            addWarehouse(this.formInline).then(()=>{
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
