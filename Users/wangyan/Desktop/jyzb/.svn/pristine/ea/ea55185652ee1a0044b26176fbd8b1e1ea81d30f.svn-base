<template>
    <div class="app-container">
        <div class="filter_container">
            <moduleTitle title="条件筛选"></moduleTitle>
            <div class="filter_box">
                <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
                    <el-form-item>
                        <el-input v-model="listQuery.user_name" size="small" placeholder="姓名" :maxlength="50" clearable
                            @keyup.enter.native="search" />
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="listQuery.telephone" size="small" placeholder="手机号码" :maxlength="50" clearable
                            @keyup.enter.native="search" />
                    </el-form-item>
                    <el-form-item>
                        <el-cascader v-model="listQuery.department"
                            :options="deptList"
                            filterable
                            :show-all-levels="false"
                            :props="{ checkStrictly: true,value:'id',label:'title',emitPath:false }"
                            clearable placeholder="所属机构"></el-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
                    </el-form-item>
                    <el-form-item>
                      <el-upload ref="uploadFile" :limit="1"
                                 accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                                 :show-file-list="false" :action="uploadUrl" :on-error="handleError" :before-upload="handleBeforeUpload"
                                 :headers="{ 'accesstoken': token }" :on-success="handleSuccess">
                        <el-button size="small" type="success" icon="el-icon-upload">导入</el-button>
                      </el-upload>
                    </el-form-item>
                    <el-form-item>
                      <el-button size="small" type="warning" icon="el-icon-download" @click="downTemplate">模版下载</el-button>
                    </el-form-item>
                    <el-form-item style="float: right;">
                        <el-button size="small" type="success" icon="el-icon-plus" @click="handleAdd()">新增</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div class="content-box">
            <moduleTitle title="用户列表"></moduleTitle>
            <div class="el-table-box">
                <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit
                    height="100%" highlight-current-row>
                    <el-table-column label="序号" width="60" type="index" align="center">
                        <template slot-scope="scope">
                            {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column label="姓名" prop="user_name" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="登录名" prop="login_name" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="角色" prop="post_name" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="性别" align="center" show-overflow-tooltip>
                        <template #default="{row}">
                            <span v-if="row.sex">{{ row.sex==1?'男':'女' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="手机号码" prop="telephone" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="所属机构" prop="dept_name" align="center" show-overflow-tooltip></el-table-column>
                    <el-table-column label="创建时间" prop="create_time" align="center"
                        show-overflow-tooltip></el-table-column>
                    <el-table-column label="操作" width="120" align="center">
                        <template #default="{ row }">
                            <div v-if="row.login_name!='admin'">
                                <el-button type="primary" size="mini" @click="handleAdd(row)">
                                    编辑
                                </el-button>
                                <el-button type="danger" size="mini" @click="del(row.id)">
                                    删除
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
            @pagination="setQuery" />
        <edit ref="edit" @fetch-data="fetchData" :deptList="deptList"></edit>
      <el-dialog title="异常提示" :visible.sync="dialogVisible" width="500px">
        <div class="tips_box">
          <div v-for="el, index in errorInfo" :key="index">
            第{{ el.rowNum }}行：{{ el.error.join(',') }}
          </div>
        </div>
        <div slot="footer" style="text-align: center">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" :loading="btn_loading" @click="sureImport">跳过异常，继续导入</el-button>
        </div>
      </el-dialog>

    </div>
</template>

<script>
import Edit from './components/edit'
import { getUserPage, delUser,getDeptTree, userImportFile,toExportTemplate } from '@/api/system'
let loadingModal = null;
export default {
    components: {
        Edit
    },
    data() {
        return {
            dialogVisible: false,
            errorInfo: [],
            btn_loading: false,
            fileId: '',
            total: 0,
            uploadUrl: process.env.VUE_APP_BASE_API + '/file/uploadFiles',
            deptList:[],
            list: [],
            listLoading: false,
            listQuery: {
                page: 1,
                limit: 20,
                user_name: '',
                telephone:'',
                department:''
            }
        }
    },
    computed: {
      token() {
        return this.$store.getters.token
      }
    },
    created() {
        getDeptTree().then(res=>{
            this.deptList = this.handleTreeList(res.data);
        })
        this.fetchData();
    },
    methods: {
        search() {
            this.listQuery.page = 1;
            this.fetchData();
        },
        refresh() {
            this.listQuery = this.$options.data().listQuery;
            this.fetchData();
        },
        handleBeforeUpload() {
          loadingModal = this.$loading({
            lock: true,
            text: '上传中，请稍后',
            spinner: 'el-icon-loading',
            background: 'rgba(255, 255, 255, 0.7)'
          });
        },
        handleSuccess(res) {
          if (res.code === 0) {
            this.fileId = res.data[0].id;
            userImportFile({ fileId: this.fileId }).then(res => {
              if (res.data) {
                this.errorInfo = res.data;
                this.dialogVisible = true;
              } else {
                this.$message.success('导入成功');
                this.fetchData();
              }
              loadingModal && loadingModal.close();
            }).catch(err => {
              console.log(err);
              loadingModal && loadingModal.close();
            })
          } else {
            this.$message.error('上传失败');
            loadingModal && loadingModal.close();
          }
          this.$nextTick(() => {
            this.$refs.uploadFile.clearFiles();
          })
        },
        sureImport() {
          this.btn_loading = true;
          importFile({ fileId: this.fileId, is_save: 1 }).then(res => {
            this.$message.success('导入成功');
            this.fetchData();
            this.dialogVisible = false;
            this.btn_loading = false;
          }).catch(err => {
            this.btn_loading = false;
          })
        },
        handleError() {
          this.$message.error('上传失败');
          loadingModal && loadingModal.close();
        },
        handleAdd(row) {
            this.$refs.edit.showEdit(row);
        },
        setQuery({ page, limit }) {
            this.listQuery.page = page;
            this.listQuery.limit = limit;
            this.fetchData();
        },
        fetchData() {
            this.listLoading = true;
            getUserPage(this.listQuery).then((response) => {
                this.list = response.data
                this.total = response.count
                this.listLoading = false
            })
        },
        del(id) {
            this.$confirm('您是否确认删除该项？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                delUser({ id }).then(() => {
                    this.$message({
                        message: '删除成功',
                        type: 'success'
                    })
                    if (this.list.length == 1) {
                        this.listQuery.page = Math.max(1, this.listQuery.page - 1);
                    }
                    this.fetchData();
                })
            })
        },
        downTemplate() {
          var that = this;
          toExportTemplate().then(res => {
            debugger;
            const reader = new FileReader();
            reader.readAsText(res.data, "utf-8");
            reader.onload = function () {
              try {
                const jsondata = JSON.parse(reader.result);
                if (jsondata && jsondata.code) {
                  if (jsondata.code == 500) {
                    that.$message.error(jsondata.msg);
                  }
                } else {
                  // 异常处理
                }
              } catch (err) {
                // 成功
                const objectUrl = URL.createObjectURL(new Blob([res.data]));
                const link = document.createElement("a");
                link.download = decodeURI('用户管理模版.xls');
                link.href = objectUrl;
                link.click();
                that.$message.success("下载成功！");
              }
            };
          })
        },
    }
}
</script>

<style lang="scss" scoped></style>
