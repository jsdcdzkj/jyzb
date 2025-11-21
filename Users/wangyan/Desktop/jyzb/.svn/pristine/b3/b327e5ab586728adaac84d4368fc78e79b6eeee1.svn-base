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
        <el-form-item label="名称" prop="supplier_name">
          <el-input v-model="formInline.supplier_name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item label="编码" prop="supplier_code">
          <el-input v-model="formInline.supplier_code" placeholder="请输入编码"></el-input>
        </el-form-item>
        <el-form-item label="公司全称" prop="company_name">
          <el-input v-model="formInline.company_name" placeholder="请输入公司全称"></el-input>
        </el-form-item>
        <el-form-item label="公司地址" prop="address">
          <el-input v-model="formInline.address" placeholder="请输入公司地址"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="formInline.contact" placeholder="请输入联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="telephone">
          <el-input v-model="formInline.telephone" placeholder="请输入联系方式"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formInline.email" placeholder="请输入邮箱号码"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :maxlength="200" :rows="5" v-model="formInline.remark" placeholder="请输入"></el-input>
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
import {addSupplier,editSupplier} from '@/api/system'
export default {
  name: "add",
  data() {
    return {
      loading: false,
      dialogFormVisible: false,
      formInline: {
        supplier_name:'',
        supplier_code:'',
        company_name:'',
        address:'',
        contact:'',
        telephone:'',
        email:'',
        remark: ''
      },
      rules: {
        supplier_name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        supplier_code: [{ required: true, message: "请输入编码", trigger: "blur" }],
      }
    };
  },
  methods: {
    showEdit(row) {
      if (row) {
        this.formInline = Object.assign({}, row);
      } else {
        this.formInline = this.$options.data().formInline;
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
            editSupplier(this.formInline).then(()=>{
              this.$message.success('操作成功');
              this.close();
              this.$emit("fetch-data");
            }).catch(() => {
              this.loading = false;
            });
          }else{
            addSupplier(this.formInline).then(()=>{
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
