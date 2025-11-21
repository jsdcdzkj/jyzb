<template>
  <div class="app-container">
    <total :list="totalData"></total>
    <div class="container-main mt16">
      <div class="container-left">
        <div class="box-card">
          <moduleTitle title="应急库物资数量趋势" :imgUrl="require('@/assets/layout/icon_qushi.png')"></moduleTitle>
          <div class="line-charts" style="margin-top: -34px;">
            <taskLine :valdata="taskLineData"></taskLine>
          </div>
        </div>
        <div class="box-card mt16">
          <moduleTitle title="应急库物资类型分析" :imgUrl="require('@/assets/layout/icon_leixing.png')"></moduleTitle>
          <el-row :gutter="20" style="padding: 0 16px;">
            <el-col :span="12">
              <div class="pie-boxs">
                <div class="pie-charts">
                  <gradePieMore :valdata="useData" />
                </div>
                <div class="pie-header pb16">
                  <div class="line"></div>
                  <div class="diamond"></div>
                  <div class="pie-title">使用装备类型占比</div>
                  <div class="diamond"></div>
                  <div class="line"></div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="pie-boxs">
                <div class="pie-charts">
                  <gradePieMore :valdata="stockData" />
                </div>
                <div class="pie-header pb16">
                  <div class="line"></div>
                  <div class="diamond"></div>
                  <div class="pie-title">库存装备类型占比</div>
                  <div class="diamond"></div>
                  <div class="line"></div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="box-card mt16">
          <moduleTitle title="分局应急库物资数据" :imgUrl="require('@/assets/layout/icon_fenju.png')"></moduleTitle>
          <div class="bar-charts">
            <div class="bar-charts-title">
              <div class="sub-title">库存装备情况</div>
              <div class="main-title">分局</div>
              <div class="sub-title align-left">使用装备情况</div>
            </div>
            <taskBar :valdata="barData"></taskBar>
          </div>
        </div>
      </div>
      <div class="container-right">
        <div class="box-card">
          <moduleTitle title="应急库物资类型占比" :imgUrl="require('@/assets/layout/icon_all.png')"></moduleTitle>
          <gradePieRose :valdata="gradeData" />
        </div>
        <div class="box-card mt16">
          <moduleTitle title="系统通知" :imgUrl="require('@/assets/layout/icon_notice.png')"></moduleTitle>
          <notice></notice>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import total from '@/components/dashboard/total'
import notice from '@/components/dashboard/notice'
import {taskLine,gradePieMore,gradePieRose,taskBar} from '@/components/Charts'
import {getEquipNum,getEquipTrend,getEquipType,getEquipDept} from '@/api/dashbord'
export default {
  components: {
    total,notice,taskLine,gradePieMore,gradePieRose,taskBar
  },
  data() {
    return {
      totalData:[{
        name:'应急库物资总数',
        val:0,
        img:require('@/assets/layout/static-a.png')
      },{
        name:'应急库物资库存总数',
        val:0,
        img:require('@/assets/layout/static-b.png')
      },{
        name:'应急库物资领用总数',
        val:0,
        img:require('@/assets/layout/static-c.png')
      },{
        name:'应急库物资处置总数',
        val:0,
        img:require('@/assets/layout/static-d.png')
      }],
      taskLineData:{
        legendData: ['使用装备', '库存装备', '处置装备'],
        xLabel: [
          '1月',
          '2月',
          '3月',
          '4月',
          '5月',
          '6月',
          '7月',
          '8月',
          '9月',
          '10月',
          '11月',
          '12月'
        ],
        dataNumA: [12,12,12,12,12,12,12,12,12,12,12,12],
        dataNumB: [23,23,43,43,55,66,77,77,44,66,44,2],
        dataNumC: [11,33,4,4,55,66,77,889,9,99,99,3],
      },
      gradeData:{
        equipTypeList:[],
        total:0,
        title:'应急库物资总数'
      },
      stockData:{
        equipTypeList:[],
        total:0,
        title:'库存装备总数'
      },
      useData:{
        equipTypeList:[],
        total:0,
        title:'使用装备总数'
      },
      barData:{
        Xdata: [],
        legendData:[],
        leftValue:[],
        rightValue:[],
      }
    }
  },
  created(){
    getEquipNum({type:3}).then(res=>{
      this.totalData[0].val = res.data.totalNum;
      this.totalData[1].val = res.data.stockNum;
      this.totalData[2].val = res.data.useNum;
      this.totalData[3].val = res.data.disposeNum;
    })
  },
  mounted() {
    getEquipTrend({type:3}).then(res=>{
      const data = res.data||[];
      let xLabel = [];
      let dataNumA = [];
      let dataNumB = [];
      let dataNumC = [];
      data.map(el=>{
        const attr = Object.keys(el)[0];
        xLabel.push(attr);
        dataNumA.push(el[attr][1].num);
        dataNumB.push(el[attr][0].num);
        dataNumC.push(el[attr][2].num);
      })
      this.taskLineData = Object.assign({},this.taskLineData,{xLabel,dataNumA,dataNumB,dataNumC});
      this.$forceUpdate();
    })
    getEquipType({type:3}).then(res=>{
      this.gradeData = Object.assign({},this.gradeData,res.data);
    })
    getEquipType({equip_status:0,type:3}).then(res=>{
      this.stockData = Object.assign({},this.stockData,res.data);
    })
    getEquipType({equip_status:1,type:3}).then(res=>{
      this.useData = Object.assign({},this.useData,res.data);
    })
    getEquipDept({type:3}).then(({data})=>{
      let dataInfo = {
        Xdata: [],
        legendData:[],
        leftValue:[],
        rightValue:[]
      };
      let leftArray = [];
      let rightArray = [];
      data.stock.map((el,index)=>{
        dataInfo.Xdata.push(el.name);
        el.deptEquipList.map((item,idx)=>{
          if(index===0){
            dataInfo.legendData.push(item.equipType);
            leftArray.push([]);
          }
          leftArray[idx].push(item.equipNum);
        })
      })
      data.use.map((el,index)=>{
        el.deptEquipList.map((item,idx)=>{
          if(index===0){
            rightArray.push([]);
          }
          rightArray[idx].push(item.equipNum);
        })
      })
      dataInfo.leftValue = leftArray;
      dataInfo.rightValue = rightArray;
      this.barData = dataInfo;
    })
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
  .app-container{
    overflow-y: auto;
  }
  .mt16{
    margin-top: 16px;
  }
  .container-main{
    width: 100%;
    display: flex;
    box-sizing: border-box;
    .container-left{
      flex:1;
      margin-right: 16px;
      .pie-boxs{
        .pie-header{
          display: flex;
          align-items: center;
          .diamond{
            width: 9px;
            height: 9px;
            background: #91A3CA;
          }
          .line{
            margin: 0 16px;
            height: 1px;
            flex: 1;
            background: #91A3CA;
            opacity: 0.32;
          }
          .pie-title{
            padding: 0 14px;
            height: 30px;
            line-height: 30px;
            border-radius: 64px 64px 64px 64px;
            font-size: 14px;
            color: #000;
          }
        }
      }
      .bar-charts{
        .bar-charts-title{
          margin: 10px auto 0;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 90%;
          height: 32px;
          background: #F8F8F8;
          border-radius: 8px 8px 8px 8px;
          font-size: 16px;
          color: #666;
          .main-title{
            width: 130px;
            text-align: center;
          }
          .sub-title{
            flex: 1;
            text-align: right;
          }
          .align-left{
            text-align: left;
          }
        }
      }
    }
    .container-right{
      width: 500px;
      flex-shrink: 0;
    }
  }
</style>
