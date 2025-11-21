<template>
    <div class="notification-box">
        <div class="notification-item" v-for="(item,index) in noticeList" :key="index">
            <div class="notification-des">{{ item.message }}</div>
            <span>{{ item.create_time }}</span>
        </div>
    </div>
  </template>
  
  <script>
import {getNotice} from '@/api/dashbord'

  export default {
    name: 'notice',
    data(){
        return {
            noticeList:[]
        }
    },
    created(){
        getNotice({page:1,limit:12}).then(res=>{
            this.noticeList = res.data;
        })
    }
  }
  </script>
  
  <style scoped lang="scss">
    .total_box{
      background: #fff;
      height: 120px;
      padding: 0 32px;
      border-radius: 4px;
      display: flex;
      flex-shrink: 0;
    }
    .statistics-item{
      flex:1;
      display: flex;
      align-items: center;
      box-sizing: border-box;
      color: rgba(0, 0, 0, 0.85);
      .static-icon{
        margin-right: 16px;
        width: 56px;
        height: 56px;
      }
      .val{
        font-size: 20px;
        line-height: 1.5;
        font-weight: bold;
      }
      .name{
        font-size: 14px;
      }
    }
  </style>
  