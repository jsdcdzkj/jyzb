<template>
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible" @close="close" width="540px">
        <span slot="title" class="drawer-title">
            <b>{{ formInline.id ? '编辑' : '新增' }}</b>
        </span>
        <div style="padding: 0 20px">
            <el-form ref="formInline" :rules="rules" :model="formInline" label-width="80px">
                <el-form-item label="姓名" prop="user_name">
                    <el-input v-model="formInline.user_name" placeholder="请输入姓名"></el-input>
                </el-form-item>
                <el-form-item label="登录名" prop="user_name">
                    <el-input v-model="formInline.login_name" placeholder="请输入登录名"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="formInline.password" placeholder="请输入密码" show-password></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex">
                    <el-select placeholder="请选择" clearable v-model="formInline.sex" style="width: 100%;">
                        <el-option :value="1" label="男"></el-option>
                        <el-option :value="2" label="女"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="联系方式" prop="telephone">
                    <el-input v-model="formInline.telephone" placeholder="请输入联系方式"></el-input>
                </el-form-item>
                <el-form-item label="用户角色" prop="post">
                    <el-select clearable placeholder="请选择" v-model="formInline.post">
                        <el-option v-for="el, index in roleList" :key="index" :label="el.post_name"
                            :value="el.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="所属机构" prop="department">
                    <el-cascader v-model="formInline.department" 
                    style="width: 100%;"
                    :options="deptList" 
                    filterable
                    :show-all-levels="false"
                    :props="{ checkStrictly: true,value:'id',label:'title',emitPath:false }" 
                    clearable placeholder="所属机构"></el-cascader>
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
import { addUser, editUser, getRoleList } from '@/api/system'
export default {
    name: "add",
    props: {
        deptList: {
            type: Array,
            default: []
        }
    },
    data() {
        return {
            loading: false,
            dialogFormVisible: false,
            roleList: [],
            formInline: {
                id: '',
                user_name: '',
                login_name: '',
                password: '',
                sex: '',
                post:'',
                telephone: '',
                department: ''
            },
            rules: {
                user_name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
                login_name: [{ required: true, message: "请输入登录名", trigger: "blur" }],
                password: [{ required: true, message: "请输入登录密码", trigger: "blur" }],
                post: [{ required: true, message: "请选择用户角色", trigger: "change" }],
                department: [{ required: true, message: "请选择所属机构", trigger: "change" }],
            }
        };
    },
    mounted() {
        getRoleList().then(res => {
            this.roleList = res.data;
        })
    },
    methods: {
        showEdit(row) {
            if (row) {
                this.formInline = {
                    id: row.id,
                    user_name: row.user_name,
                    login_name: row.login_name,
                    password:row.password,
                    sex: row.sex,
                    post:row.post,
                    telephone: row.telephone,
                    department: row.department
                };
            } else {
                this.formInline = this.$options.data().formInline;
            }
            this.loading = false;
            this.dialogFormVisible = true;
            this.$nextTick(() => {
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
                    if (this.formInline.id) {
                        editUser(this.formInline).then(() => {
                            this.$message.success('操作成功');
                            this.close();
                            this.$emit("fetch-data");
                        }).catch(() => {
                            this.loading = false;
                        });
                    } else {
                        addUser(this.formInline).then(() => {
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