<template>
  <div class="total_box">
    <div class="statistics-item" v-for="el,index in list" :key="index">
      <img :src="el.img" class="static-icon"></img>
      <div class="static-des">
        <div class="val">{{ el.val }}</div>
        <div class="name">{{ el.name }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'dashboardTotal',
  props: {
    list: {
      type: Array,
      default: []
    }
  },
  methods: {
    
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
