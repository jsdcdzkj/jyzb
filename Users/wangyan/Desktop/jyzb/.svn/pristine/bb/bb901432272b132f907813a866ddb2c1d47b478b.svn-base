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
          <el-form-item label="角色名称" prop="post_name">
            <el-input v-if="!formInline.id" key="add" v-model="formInline.post_name" placeholder="请输入名称"></el-input>
            <el-input v-else key="edit" v-model="formInline.post_name" placeholder="请输入名称" :disabled="formInline.post_name=='超级管理员'"></el-input>
          </el-form-item>
          <el-form-item label="菜单权限">
            <div style="max-height: 400px;overflow: auto;">
                <el-tree default-expand-all node-key="id" show-checkbox :data="menuTree" ref="menuTree">
                </el-tree>
            </div>
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
  import {addRole,editRole,getMenuTree,detailRole} from '@/api/system'
  export default {
    name: "add",
    data() {
      return {
        loading: false,
        dialogFormVisible: false,
        menuTree:[],
        formInline: {
            id:'',
            post_name:''
        },
        rules: {
          post_name: [{ required: true, message: "请输入名称", trigger: "blur" }]
        }
      };
    },
    mounted(){
        getMenuTree({is_show:1}).then(res=>{
            this.menuTree = res.data;
        })
    },
    methods: {
      showEdit(id) {
        this.formInline = this.$options.data().formInline;
        this.dialogFormVisible = true;
        this.loading = false;
        this.$nextTick(()=>{
          this.$refs.formInline.clearValidate();
          if(id){
            detailRole({id}).then(res=>{
                this.formInline = {
                    id:res.data.id,
                    post_name:res.data.post_name
                }
                this.$refs.menuTree.setCheckedKeys(res.data.permissionIds.split(','));
                this.$refs.formInline.clearValidate();
            })
          }else{
            this.$refs.menuTree.setCheckedKeys([]);
          }
        })
      },
      close() {
        this.dialogFormVisible = false;
      },
      save(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const keys = this.$refs.menuTree.getCheckedKeys();
            if(keys.length==0){
                this.$message.error('请选择角色所属菜单权限');
                return
            }
            this.loading = true;
            if(this.formInline.id){
              editRole(Object.assign({},this.formInline,{permissionIds:keys.join(',')})).then(()=>{
                this.$message.success('操作成功');
                this.close();
                this.$emit("fetch-data");
              }).catch(() => {
                this.loading = false;
              });
            }else{
              addRole(Object.assign({},this.formInline,{permissionIds:keys.join(',')})).then(()=>{
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
  