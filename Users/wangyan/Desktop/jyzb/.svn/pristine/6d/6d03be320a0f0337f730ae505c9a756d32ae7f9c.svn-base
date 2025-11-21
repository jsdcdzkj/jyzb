<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="listQuery.enter_no" size="small" placeholder="单号" :maxlength="50" clearable
              @keyup.enter.native="search" />
          </el-form-item>
          <el-form-item>
            <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="入库开始日期"
              end-placeholder="入库结束日期" value-format="yyyy-MM-dd"></el-date-picker>
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
      <moduleTitle title="入库单列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
          highlight-current-row>
          <el-table-column label="序号" width="60" type="index" align="center">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="入库单号" width="200" prop="enter_no" align="center"
            show-overflow-tooltip></el-table-column>
          <el-table-column label="入库单名称" prop="enter_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="入库数量" width="150" prop="equip_num" align="center"></el-table-column>
          <el-table-column label="供应商名称" prop="supplier_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="入库时间" width="150" prop="enter_time" align="center"></el-table-column>
          <el-table-column label="记录时间" width="150" prop="create_time" align="center"></el-table-column>
          <el-table-column label="操作人" width="100" prop="create_user_name" align="center"
            show-overflow-tooltip></el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="{ row }">
              <el-button type="primary" size="mini" @click="handleView(row.id)">
                明细详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="setQuery" />
    <add ref="add" @fetch-data="fetchData"></add>
    <detail ref="detail"></detail>
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
import Add from './components/add'
import Detail from './components/detail'
import { downTemplate, importFile, getInOrderList } from '@/api/inventory'
let loadingModal = null;
export default {
  components: {
    Add, Detail
  },
  data() {
    return {
      dialogVisible: false,
      errorInfo: [],
      btn_loading: false,
      fileId: '',
      total: 0,
      uploadUrl: process.env.VUE_APP_BASE_API + '/file/uploadFiles',
      list: [],
      dateRange: null,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        enter_no: '',
        enter_start_time: '',
        enter_end_time: ''
      }
    }
  },
  computed: {
    token() {
      return this.$store.getters.token
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    search() {
      this.listQuery.page = 1;
      this.fetchData();
    },
    refresh() {
      this.listQuery = this.$options.data().listQuery;
      this.dateRange = null;
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
        importFile({ fileId: this.fileId }).then(res => {
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
    downTemplate() {
      var that = this;
      downTemplate().then(res => {
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
            link.download = decodeURI('入库管理模版.xls');
            link.href = objectUrl;
            link.click();
            that.$message.success("下载成功！");
          }
        };
      })
    },
    handleAdd(row) {
      this.$refs.add.showEdit(row);
    },
    handleView(id) {
      this.$refs.detail.showView(id);
    },
    setQuery({ page, limit }) {
      this.listQuery.page = page;
      this.listQuery.limit = limit;
      this.fetchData();
    },
    fetchData() {
      this.listLoading = true;
      if (this.dateRange && this.dateRange.length) {
        this.listQuery.enter_start_time = this.dateRange[0];
        this.listQuery.enter_end_time = this.dateRange[1];
      } else {
        this.listQuery.enter_start_time = '';
        this.listQuery.enter_end_time = '';
      }
      getInOrderList(this.listQuery).then((response) => {
        this.list = response.data;
        this.total = response.count;
        this.listLoading = false;
      })
    }
  }
}
</script>

<style lang="scss" scoped></style>
