<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="listQuery.supplier_name" size="small" placeholder="供应商名称" :maxlength="50" clearable @keyup.enter.native="search"/>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search" @click="search">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-button size="small" type="success" icon="el-icon-plus" @click="handleAdd()">新增</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="content-box">
      <moduleTitle title="供应商列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
 highlight-current-row>
          <el-table-column label="序号" width="60" type="index" align="center">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="名称" prop="supplier_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="编码" width="100" prop="supplier_code" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="公司全称" prop="company_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="地址" prop="address" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="联系人" width="80" prop="contact" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="联系方式" width="100" prop="telephone" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="邮箱" prop="email" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="备注" prop="remark" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template #default="{row}">
              <el-button type="primary" size="mini" @click="handleAdd(row)">
                编辑
              </el-button>
              <el-button type="danger" size="mini" @click="del(row.id)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="setQuery" />
    <edit ref="edit" @fetch-data="fetchData"></edit>
  </div>
</template>

<script>
import Edit from './components/edit'
import {getSupplierList,delSupplier} from '@/api/system'
export default {
  components: {
    Edit
  },
  data() {
    return {
      total: 0,
      list: [],
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        supplier_name: '',
      }
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
      this.fetchData();
    },
    handleAdd(row){
      this.$refs.edit.showEdit(row);
    },
    setQuery({page,limit}){
      this.listQuery.page = page;
      this.listQuery.limit = limit;
      this.fetchData();
    },
    fetchData() {
      this.listLoading = true;
      getSupplierList(this.listQuery).then((response) => {
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
        delSupplier({ id }).then(() => {
          this.$message({
              message: '删除成功',
              type: 'success'
            })
            if(this.list.length==1){
              this.listQuery.page = Math.max(1,this.listQuery.page-1);
            }
            this.fetchData();
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  
</style>
