<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="permission_name" size="small" placeholder="名称" :maxlength="50" clearable @keyup.enter.native="fetchData"/>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
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
      <moduleTitle title="菜单列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
 highlight-current-row :tree-props="{children: 'children', hasChildren: 'spread'}" row-key="id" default-expand-all>
          <el-table-column label="菜单名称" prop="permission_name"></el-table-column>
          <el-table-column label="路由名称" prop="route_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="路由链接" prop="route_url" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="vue文件路径" prop="vue_path" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="重定向类型" prop="redirect_type" align="center"></el-table-column>
          <el-table-column label="图标" prop="icon" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="是否显示" align="center" width="80">
            <template #default="{row}">
              <el-switch v-model="row.is_show" :active-value="1" :inactive-value="0" @change="setShow($event,row.id)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="排序" prop="sort" align="center" width="80"></el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{row}">
              <template>
                <el-button type="primary" size="mini" @click="handleAdd(row)">
                  编辑
                </el-button>
                <el-button type="danger" size="mini" @click="del(row.id)">
                  删除
                </el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <edit ref="edit" @fetch-data="fetchData" :treeData="list"></edit>
  </div>
</template>

<script>
import Edit from './components/edit'
import {getMenuTree,delMenu,editMenu} from '@/api/system'
export default {
  components: {
    Edit
  },
  data() {
    return {
      permission_name:'',
      list: [],
      listLoading: false,
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    refresh() {
      this.permission_name = '';
      this.fetchData();
    },
    handleAdd(row){
      this.$refs.edit.showEdit(row);
    },
    fetchData() {
      this.listLoading = true;
      getMenuTree({permission_name:this.permission_name}).then((response) => {
        this.list = response.data;
        this.listLoading = false;
      })
    },
    setShow(val,id){
      editMenu({is_show:val,id}).then(()=>{
        this.$message.success('操作成功');
      })
    },
    del(id) {
      this.$confirm('您是否确认删除该项？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delMenu({ id }).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success'
          })
          this.fetchData();
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  
</style>
