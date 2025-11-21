<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="dept_name" size="small" placeholder="名称" :maxlength="50" clearable @keyup.enter.native="fetchData"/>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
          </el-form-item>
          <el-form-item style="float: right;" v-if="list[0].level!=3">
            <el-button size="small" type="success" icon="el-icon-plus" @click="handleAdd(list[0])">新增</el-button>
            <el-button size="small" type="primary" icon="el-icon-s-unfold" @click="mergeOrg">合并</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="content-box">
      <moduleTitle title="组织架构列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
 highlight-current-row :tree-props="{children: 'children', hasChildren: 'spread'}" row-key="id" default-expand-all>
          <el-table-column label="名称" prop="title"></el-table-column>
          <el-table-column label="编码" prop="code" align="center"></el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="{row}">
              <el-button type="success" size="mini" @click="handleAdd(row)" v-if="row.level!=3">
                新增
              </el-button>
              <template v-if="row.level!=1&&list[0].level!=3">
                <el-button type="primary" size="mini" @click="handleEdit(row)">
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
    <edit ref="edit" @fetch-data="fetchData" :selectList="selectList" :select="nowCate"></edit>
    <merge ref="merge" @refreshTree="fetchData"></merge>
  </div>
</template>

<script>
import Edit from './components/edit'
import Merge from './components/merge'
import {getDeptTree,delDept} from '@/api/system'
export default {
  components: {
    Edit,
    Merge
  },
  data() {
    return {
      dept_name:'',
      nowCate:'',
      list: [],
      listLoading: false,
      selectList:[]
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    refresh() {
      this.dept_name = '';
      this.fetchData();
    },
    handleAdd(row){
      this.nowCate = row.id;
      if(row.level==2&&this.list[0].level===1){
        this.selectList = this.list.find(el=>el.id==row.parent_id).children;
      }else{
        this.selectList = this.list;
      }
      this.$nextTick(()=>{
        this.$refs.edit.showEdit({level:row.level});
      })
    },
    handleEdit(row){
      if(row.level=='2'){
        this.selectList = this.list;
      }else{
        let list = [];
        this.list.some(el=>{
          if(el.children.some(item=>item.id==row.parent_id)){
            list = el.children;
            return true
          }else{
            return false;
          }
        })
        this.selectList = list;
      }
      this.$nextTick(()=>{
        this.$refs.edit.showEdit(row);
      })
    },
    fetchData() {
      this.listLoading = true;
      getDeptTree({dept_name:this.dept_name}).then((response) => {
        this.list = response.data;
        this.listLoading = false;
      })
    },
    del(id) {
      this.$confirm('您是否确认删除该项？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDept({ id }).then(() => {
          this.$message({
              message: '删除成功',
              type: 'success'
            })
            this.fetchData();
        })
      })
    },
    mergeOrg(){
      this.$refs.merge.showEdit();
    }
  }
}
</script>

<style lang="scss" scoped>
  
</style>
