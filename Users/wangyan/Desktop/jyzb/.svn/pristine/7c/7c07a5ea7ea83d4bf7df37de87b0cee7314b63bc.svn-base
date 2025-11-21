<template>
  <div class="app-container">
    <div class="filter_container">
      <moduleTitle title="条件筛选"></moduleTitle>
      <div class="filter_box">
        <el-form :inline="true" class="demo-form-inline topsearch" @submit.native.prevent>
          <el-form-item>
            <el-input v-model="listQuery.name" size="small" placeholder="申请编号/装备类型/装备名称" :maxlength="50" clearable
              @keyup.enter.native="search" />
          </el-form-item>
          <el-form-item>
            <el-cascader v-model="listQuery.use_dept" :options="deptList" :show-all-levels="false"
              :props="{ checkStrictly: true, value: 'id', label: 'title', emitPath: false }" clearable
              placeholder="选择机构"></el-cascader>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="listQuery.status" placeholder="借用状态">
              <el-option label="待审批" :value="1"></el-option>
              <el-option label="借用中" :value="2"></el-option>
              <el-option label="待归还" :value="3"></el-option>
              <el-option label="已归还" :value="4"></el-option>
              <el-option label="已驳回" :value="5"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="listQuery.create_user" placeholder="申请人">
              <el-option v-for="el in userList" :key="el.id" :label="el.user_name" :value="el.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select clearable v-model="listQuery.equip_type" placeholder="装备类型" @change="changeType">
              <el-option v-for="el, index in assetsType" :key="index" :label="el.assets_type_name" :value="el.id"
                :disabled="el.is_disable == '1'"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-show="listQuery.equip_type">
            <el-select clearable v-model="listQuery.equip_name" placeholder="装备名称">
              <el-option v-for="el, index in nameList" :key="index" :label="el.assets_type_name"
                :value="el.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" icon="el-icon-search" @click="search">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="content-box">
      <moduleTitle title="库存列表"></moduleTitle>
      <div class="el-table-box">
        <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" stripe border fit height="100%"
          highlight-current-row>
          <el-table-column label="序号" width="60" type="index" align="center" fixed="left">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="申请编号" prop="delivery_no" fixed="left" align="center" width="120"></el-table-column>
          <el-table-column label="借出单位" prop="use_dept_name" fixed="left" align="center"
            min-width="150"></el-table-column>
          <el-table-column label="申请人" prop="create_user_name" align="center" width="80"></el-table-column>
          <el-table-column label="申请数量" prop="delivery_num" align="center" width="80"></el-table-column>
          <el-table-column align="center" prop="equip_type_name" label="装备类型" min-width="120"></el-table-column>
          <el-table-column align="center" prop="equip_name_desc" label="装备名称" min-width="120"></el-table-column>
          <el-table-column align="center" prop="assets_type_code" label="装备编码" min-width="120"></el-table-column>
          <el-table-column align="center" prop="equip_model" label="装备型号" min-width="120"></el-table-column>
          <el-table-column label="申请时间" prop="create_time" align="center" width="150"></el-table-column>
          <el-table-column label="借用时段" align="center" width="200">
            <template #default="{ row }">
              {{ row.startTime }} 至 {{ row.endTime }}
            </template>
          </el-table-column>
          <el-table-column label="审批时间" prop="applyTime" align="center" width="150"></el-table-column>
          <el-table-column label="归还时间" prop="returnTime" align="center" width="150"></el-table-column>
          <el-table-column label="状态" align="center" width="80" fixed="right">
            <template #default="{ row }">
              <el-tag type="warning" v-if="row.status == 1">待审批</el-tag>
              <el-tag type="primary" v-if="row.status == 2">借用中</el-tag>
              <el-tag type="success" v-if="row.status == 3">待归还</el-tag>
              <el-tag type="success" v-if="row.status == 4">已归还</el-tag>
              <el-tag type="danger" v-if="row.status == 5">已驳回</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="mini" v-if="row.status == 2" @click="applyReturn(row.id)">归还</el-button>
              <el-button type="danger" size="mini" v-if="row.status == 1" @click="delOrder(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="setQuery" />
  </div>
</template>

<script>
import { applyList, applyDel, applyReturn, applyPerson } from '@/api/share'
import { getEquipmentList, getDeptTree } from '@/api/system'
export default {
  data() {
    return {
      total: 0,
      list: [],
      dateRange: [],
      listLoading: false,
      assetsType: [],
      nameList: [],
      deptList: [],
      userList: [],
      button_loading: false,
      listQuery: {
        page: 1,
        limit: 20,
        status: '',
        create_user: '',
        name: '',
        use_dept: '',
        equip_type: '',
        equip_name: ''
      }
    }
  },
  computed: {
    name() {
      return this.$store.getters.name
    },
    dept_name() {
      return this.$store.getters.dept_name
    }
  },
  created() {
    this.fetchData();
    getEquipmentList().then(res => {
      this.assetsType = res.data || [];
    })
    getDeptTree({ is_permission: 0 }).then(res => {
      this.deptList = this.handleTreeList(res.data) || [];
    })
    applyPerson().then(res => {
      this.userList = res.data;
    })
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
    changeType(val) {
      this.listQuery.equip_name = '';
      this.nameList = [];
      if (val) {
        getEquipmentList({ parent_id: val }).then(res => {
          this.nameList = res.data || [];
        })
      }
    },
    applyReturn(id) {
      this.$confirm('是否确认归还?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        applyReturn({ id }).then(res => {
          this.$message.success('归还提交成功，请等待确认');
          this.fetchData();
        })
      })
    },
    setQuery({ page, limit }) {
      this.listQuery.page = page;
      this.listQuery.limit = limit;
      this.fetchData();
    },
    fetchData() {
      this.listLoading = true;
      applyList(this.listQuery).then((response) => {
        this.list = response.data;
        this.total = response.count;
        this.listLoading = false;
      })
    },
    delOrder(id) {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        applyDel({ id }).then(res => {
          this.$message.success('删除成功');
          if (this.list.length) {
            this.listQuery.page = Math.max(1, this.listQuery.page - 1);
          }
          this.fetchData();
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped></style>