<template>
  <div>
    <el-drawer :visible.sync="dialogFormVisible" :size="variables.drawWidth" @close="close" modal-append-to-body>
      <span slot="title" class="drawer-title"><b>月度转结</b></span>
      <el-form inline style="padding: 16px 16px 0;">
        <el-form-item>
          <el-date-picker v-model="yearMonth" type="month" :clearable="false" value-format="yyyy-MM" placeholder="选择月">
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="monthInfo.status === 0">
          <el-button plain @click="close">暂不结转</el-button>
          <el-button type="primary" @click="confirmOver">确认结转</el-button>
        </el-form-item>
      </el-form>
      <el-descriptions :column="2" border style="padding: 0 16px;">
        <el-descriptions-item label="统计时间范围">{{ timeObj.start }} ~ {{ timeObj.end }}</el-descriptions-item>
        <el-descriptions-item label="当前装备数量">{{ monthInfo.equipNum }}</el-descriptions-item>
        <el-descriptions-item label="入库数量">{{ monthInfo.equipInNum }}</el-descriptions-item>
        <el-descriptions-item label="出库数量">{{ monthInfo.equipOutNum }}</el-descriptions-item>
      </el-descriptions>
      <div style="padding: 16px;">
        <el-radio-group v-model="tabs" @change="getData">
          <el-radio-button label="入库记录"></el-radio-button>
          <el-radio-button label="出库记录"></el-radio-button>
        </el-radio-group>
      </div>
      <div style="padding: 16px;height: calc(100vh - 300px)">
        <el-table :data="tableList" stripe border fit height="100%" highlight-current-row v-if="tabs == '入库记录'"
          key="in">
          <el-table-column label="序号" width="60" type="index" align="center">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="入库单号" prop="enter_no" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="入库数量" prop="equip_num" align="center"></el-table-column>
          <el-table-column label="供应商名称" prop="supplier_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="入库时间" prop="enter_time" align="center"></el-table-column>
          <el-table-column label="记录时间" prop="create_time" align="center"></el-table-column>
          <el-table-column label="操作人" prop="create_user_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="{ row }">
              <el-button type="primary" size="mini" @click="handleInView(row.id)">
                明细详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-table :data="tableList" stripe border fit height="100%" highlight-current-row v-if="tabs == '出库记录'"
          key="out">
          <el-table-column label="序号" width="60" type="index" align="center">
            <template slot-scope="scope">
              {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="出库单号" prop="delivery_no" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="出库时间" prop="delivery_time" align="center"></el-table-column>
          <el-table-column label="记录时间" prop="create_time" align="center"></el-table-column>
          <el-table-column label="操作人" prop="create_user_name" align="center" show-overflow-tooltip></el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="{ row }">
              <el-button type="primary" size="mini" @click="handleOutView(row.id)">
                明细详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
        @pagination="setQuery" />
    </el-drawer>
    <inDetail ref="inDetail"></inDetail>
    <outDetail ref="outDetail"></outDetail>
  </div>

</template>

<script>
import variables from '@/styles/variables.scss'
import { getOverCount, saveOverCount, getInOrderList, getOutOrderList } from '@/api/inventory'
function getLastDayOfMonth(year, month) {
  // 设置月份为下个月的第一天
  var nextMonth = new Date(year, month + 1, 1);
  // 获取当前月的最后一天，即下个月的前一天
  var lastDay = new Date(nextMonth.getTime() - 1);
  // 返回最后一天的日期
  return lastDay.getDate();
}
import inDetail from '../../Warehousing/components/detail'
import outDetail from '../../Outbound/components/detail'
export default {
  name: 'count',
  components: {
    inDetail, outDetail
  },
  data() {
    return {
      dialogFormVisible: false,
      yearMonth: '',
      tableList: [],
      total: 0,
      listQuery: {
        page: 1,
        limit: 20
      },
      tabs: '入库记录',
      monthInfo: {

      },
    }
  },
  computed: {
    variables() {
      return variables
    },
    timeObj() {
      if (!this.yearMonth) {
        return {}
      } else {
        const day = getLastDayOfMonth(this.yearMonth.substr(0, 4), Number(this.yearMonth.substr(5, 2)) - 1);
        return {
          start: this.yearMonth + '-01',
          end: this.yearMonth + '-' + day
        }
      }
    }
  },
  watch: {
    yearMonth(val) {
      getOverCount({ yearMonth: val }).then(res => {
        this.monthInfo = res.data;
        this.dialogFormVisible = true;
      })
      this.listQuery = { page: 1, limit: 20 };
      this.tabs = '入库记录';
      this.getData();
    }
  },
  methods: {
    showView() {
      const month = Number(new Date().getMonth()) + 1;
      const yearMonth = new Date().getFullYear() + '-' + (month > 9 ? month : '0' + month);
      if (this.yearMonth == yearMonth) {
        if (this.monthInfo.equipNum && this.monthInfo.equipNum !== '') {
          this.dialogFormVisible = true;
        }
      } else {
        this.yearMonth = yearMonth;
      }
    },
    setQuery({ page, limit }) {
      this.listQuery.page = page;
      this.listQuery.limit = limit;
      this.getData();
    },
    getData() {
      if (this.tabs == '入库记录') {
        getInOrderList(Object.assign({}, this.listQuery, { enter_start_time: this.timeObj.start, enter_end_time: this.timeObj.end })).then(res => {
          this.tableList = res.data;
          this.total = res.count;
        })
      } else {
        getOutOrderList(Object.assign({}, this.listQuery, { delivery_start_time: this.timeObj.start, delivery_end_time: this.timeObj.end })).then(res => {
          this.tableList = res.data;
          this.total = res.count;
        })
      }
    },
    confirmOver() {
      this.$confirm('确定进行结转操作吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        saveOverCount({ yearMonth: this.yearMonth }).then(() => {
          this.$message.success('操作成功');
          this.close();
        })
      })
    },
    handleInView(id) {
      this.$refs.inDetail.showView(id);
    },
    handleOutView(id) {
      this.$refs.outDetail.showView(id);
    },
    close() {
      this.dialogFormVisible = false;
    }
  },
}
</script>
<style scoped lang="scss">
::v-deep {
  .el-drawer__header {
    padding: 10px !important;
    margin-bottom: 0px;
    border-bottom: 1px solid #d1d9e1;
  }
}
</style>
