<template>
  <div class="app-container">
    <div class="formBox">
      <el-form :inline="true" class="topsearch" :model="queryParams" @submit.native.prevent ref="queryParams">
        <el-form-item>
          <el-input v-model="queryParams.user_name" size="small" placeholder="用户名" :maxlength="50" clearable/>
        </el-form-item>

        <el-form-item prop="role_id" label="角色名称:">
          <el-select
            v-model="queryParams.role_id"
            placeholder="角色名称"
            clearable
          >
            <el-option
              v-for="item in roleList"
              :key="item.id"
              :label="item.role_name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="el-icon-search" @click="search">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="rightBox"><el-button size="small" type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
    </div>


    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="elementLoadingText"
      stripe
      border
      fit
      height="100%"
      class="table-container mar"
      highlight-current-row
    >
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope">
          {{
            (queryParams.pageIndex - 1) * queryParams.pageSize + scope.$index + 1
          }}
        </template>
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        prop="real_name"
        label="姓名"
        width="150"
        align="center"
      ></el-table-column>
      <el-table-column
        show-overflow-tooltip
        prop="user_name"
        label="用户名"
        width="200"
        align="center"
      ></el-table-column>
      <el-table-column
        show-overflow-tooltip
        prop="role_names"
        label="角色"
        align="center"
      ></el-table-column>
      <el-table-column
        show-overflow-tooltip
        prop="area_names"
        label="所属监区"
        align="center"
      ></el-table-column>
      <el-table-column prop="states" width="100" label="账号状态" align="center">
        <template slot-scope="scope">
          <el-tag type="success" size="small" v-if="scope.row.states == 0" effect="plain">启用</el-tag>
          <el-tag type="danger" size="small" v-else effect="plain">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        prop="creation_time"
        label="创建时间"
        align="center"
      ></el-table-column>
      <el-table-column
        show-overflow-tooltip
        width="340"
        label="操作"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            plain
            @click="handleAdd(scope.row)"
            icon="el-icon-edit"
          >编辑
          </el-button
          >
          <el-button
            v-if="scope.row.states == 1"
            plain
            type="success"
            size="mini"
            icon="el-icon-video-play"
            @click="handleOperate(scope.row)"
          >启用
          </el-button>
          <el-button icon="el-icon-video-pause" plain type="warning" v-else size="mini"
                     @click="handleOperate(scope.row)"
          >停用
          </el-button
          >

          <el-button
            type="danger"
            size="mini"
            plain
            icon="el-icon-delete"
            @click="handleDel(scope.row.id)"
          >删除
          </el-button
          >
          <el-button icon="el-icon-refresh" plain size="mini" @click="resetPassword(scope.row.id)"
          >重置密码
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageIndex" :pageSizes="sizes"
                :limit.sync="queryParams.pageSize"
                @pagination="handleQuery" style="padding-bottom: 10px;"/>
    <edit ref="edit" :area-list="areaList" :role-list="roleList" @fetch-data="fetchData"></edit>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import Edit from './components/edit'
const _temp = {
  id: '',
  content: '',
  title: ''
}

export default {
  components: {
    Pagination,Edit
  },
  data() {
    return {
      queryParams: {
        pageIndex: 1,
        pageSize: 20,
        user_name: "",
        area_id: "",
        role_id: "",
      },
      sizes: [2, 5, 10, 20, 50],
      total: 0,
      list: [],
      listLoading: false,
      elementLoadingText: "正在加载...",
      roleList: [],
      areaList: [],
    }
  },
  created() {
    // this.fetchData();
    //  this.getren();
  },
  methods: {
    // 切换页面显示个数操作
    handleSizeChange(val) {
      this.queryParams.pageIndex = 1;
      this.queryParams.pageSize = val;
      this.handleQuery("page");
    },
    // 切换页面页码
    pageCurrentChange(val) {
      this.queryParams.pageIndex = val;
      this.handleQuery("page");
    },
    selectAreaList() {
      selectAreaList({type: 1}).then(res => {
        this.areaList = res.data
      })
    },
    selectRoleList() {
      selectRoleList().then(res => {
        this.roleList = res.data
      })
    },
    handleSearch() {
      this.queryParams.pageIndex = 1;
      this.handleQuery()
    },
    handleQuery() {
      this.listLoading = true
      selectPageList(this.queryParams).then((res) => {
        this.list = res.data.records;
        this.total = res.data.total;
        this.listLoading = false
      }).catch(err => {
        this.listLoading = false
      });
    },
    resetQuery() {
      this.queryParams = {
        pageIndex: 1,
        pageSize: 20,
        user_name: "",
        area_id: "",
        role_id: "",
        phone: "",
        remarks: "",
      }
      this.handleSearch()
    },
    handleAdd(row) {
      if (row) {
        this.$refs["edit"].showEdit(row);
      } else {
        this.$refs["edit"].showEdit();
      }
    },
    handleDel(id) {
      this.$confirm("您是否确认删除该项", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteByUserId({id}).then(res => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.handleSearch()
          })
        })
    },
    handleOperate(row) {
      const states = row.states == 1 ? 0 : 1
      const info = row.states == 1 ? '启用' : '停用'
      this.$confirm(`您是否确认${info}该项？`, "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        updStates({id: row.id, states}).then(res => {
          this.$message({
            type: "success",
            message: `${info}成功!`
          });
          this.handleQuery()
        })
      })
    },
    resetPassword(id) {
      this.$confirm("您是否确认重置密码", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        resetPass({id}).then(res => {
          this.$confirm("您重置后的密码：fxry@2024", "提示", {
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            type: "warning"
          })
        })
      })

    },
  },
}
</script>
