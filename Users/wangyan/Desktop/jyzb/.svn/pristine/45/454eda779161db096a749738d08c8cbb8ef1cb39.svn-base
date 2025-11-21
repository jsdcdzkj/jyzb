<template>
  <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="dialogFormVisible" width="600px"
             @close="close">
    <el-form ref="formData" :model="form" :rules="rules" label-width="auto">
      <el-form-item label="菜单名称" prop="permission_name">
        <el-input v-model.trim="form.permission_name" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="父级菜单" prop="parent_permission">
        <el-select v-model="form.parent_permission" clearable placeholder="请选择" style="width: 100%;">
          <el-option :disabled="form.id==el.id" v-for="el in treeData" :key="el.id" :value="el.id" :label="el.permission_name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="路由名称" prop="route_name">
        <el-input v-model.trim="form.route_name" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="路由链接" prop="route_url">
        <el-input v-model.trim="form.route_url" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="vue文件路径" prop="vue_path">
        <el-input v-model.trim="form.vue_path" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="重定向类型">
        <el-input v-model.trim="form.redirect_type" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="图标">
        <el-input v-model.trim="form.icon" autocomplete="off" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="是否显示">
        <el-radio-group v-model="form.is_show">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model.trim="form.sort" autocomplete="off" placeholder="请输入" type="number"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" :disabled="disabledBtn">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {addMenu,editMenu} from '@/api/system'
const _temp = {
  id: '',
  permission_name: '',
  permission_type: 0,
  parent_permission: '',
  route_name:'',
  route_url:'',
  vue_path:'',
  redirect_type:'',
  is_show:1,
  icon:'',
  sort: '',
};
export default {
  name: 'menuEdit',
  props:{
    treeData:{
      type:Array,
      default:[]
    }
  },
  data() {
    return {
      isClearable: true, // 可清空（可选）
      isAccordion: true, // 可收起（可选）
      form: Object.assign({}, _temp),
      rules: {
        permission_name: [{required: true, trigger: 'blur', message: '请输入菜单名称'}],
        route_url: [{required: true, trigger: 'blur', message: '请输入路由地址'}],
        vue_path: [{required: true, trigger: 'blur', message: '请输入vue地址'}],
      },
      title: '',
      dialogFormVisible: false,
      disabledBtn: false,
    }
  },
  methods: {
    resetTemp() {
      this.form = Object.assign({}, _temp);
    },
    // 取值
    getValue(value) {
      this.form.parentId = value;
    },
    showEdit(row) {
      if (!row) {
        this.title = '添加'
      } else {
        this.title = '编辑';
        if(row.parent_permission===0){
          row.parent_permission = '';
        }
        this.form = Object.assign({}, row);
      }
      this.disabledBtn = false;
      this.dialogFormVisible = true;
    },
    close() {
      this.resetTemp();
      this.$nextTick(() => {
        this.$refs.formData.clearValidate();
      });
      this.dialogFormVisible = false;
    },
    save() {
      this.$refs.formData.validate((valid) => {
        if (valid) {
          this.disabledBtn = true;
          if(this.form.id){
            editMenu(this.form).then(() => {
              this.$message({message: "操作成功", type: 'success'});
              this.$emit('fetch-data');
              this.close();
            }).catch(() => {
              this.disabledBtn = false;
            })
          }else{
            addMenu(this.form).then(() => {
              this.$message({message: "操作成功", type: 'success'});
              this.$emit('fetch-data');
              this.close();
            }).catch(() => {
              this.disabledBtn = false;
            })
          }
        }
      })
    }
  },
}
</script>
