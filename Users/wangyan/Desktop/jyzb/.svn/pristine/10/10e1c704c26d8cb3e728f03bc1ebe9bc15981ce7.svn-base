<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="listQuery.delivery_no" size="small" placeholder="单号" :maxlength="50" clearable @keyup.enter.native="search"/>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="listQuery.delivery_type" placeholder="出库类型">
              <el-option label="调拨" :value="1"></el-option>
              <el-option label="领用" :value="2"></el-option>
              <el-option label="处置" :value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="出库开始日期" end-placeholder="出库结束日期" value-format="yyyy-MM-dd"></el-date-picker>
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
      <moduleTitle title="出库单列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
 highlight-current-row>
          <el-table-column label="序号" width="60" type="index" align="center">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="出库单号" width="200" prop="delivery_no" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="出库单名称" prop="delivery_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="出库类型" prop="delivery_type_desc" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="出库对象" align="center" show-overflow-tooltip>
            <template #default="{row}">
              {{ row.warehouse_name||row.use_dept_name }}
            </template>
          </el-table-column>
          <el-table-column label="出库时间" width="150" prop="delivery_time" align="center"></el-table-column>
          <el-table-column label="记录时间" width="150" prop="create_time" align="center"></el-table-column>
          <el-table-column label="操作人" width="100" prop="create_user_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="{row}">
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
  </div>
</template>

<script>
import Add from './components/add'
import Detail from './components/detail'
import {getOutOrderList} from '@/api/inventory'
export default {
  components: {
    Add,Detail
  },
  data() {
    return {
      total: 0,
      list: [],
      listLoading: false,
      dateRange:[],
      listQuery: {
        page: 1,
        limit: 20,
        delivery_no: '',
        delivery_type:'',
        delivery_start_time:'',
        delivery_end_time:'',
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
      this.dateRange = null;
      this.fetchData();
    },
    handleAdd(row){
      this.$refs.add.showEdit(row);
    },
    handleView(id){
      this.$refs.detail.showView(id);
    },
    setQuery({page,limit}){
      this.listQuery.page = page;
      this.listQuery.limit = limit;
      this.fetchData();
    },
    fetchData() {
      this.listLoading = true;
      if(this.dateRange&&this.dateRange.length){
        this.listQuery.delivery_start_time = this.dateRange[0];
        this.listQuery.delivery_end_time = this.dateRange[1];
      }else{
        this.listQuery.delivery_start_time = '';
        this.listQuery.delivery_end_time = '';
      }
      getOutOrderList(this.listQuery).then((response) => {
        this.list = response.data;
        this.total = response.count;
        this.listLoading = false;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  
</style>
