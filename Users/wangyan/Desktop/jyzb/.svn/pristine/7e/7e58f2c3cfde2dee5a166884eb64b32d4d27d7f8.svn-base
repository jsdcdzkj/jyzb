<template>
  <div class="box">
    <div class="left_box">
      <img :src="imgUrl||require('@/assets/icon/icon_title.png')">
      {{ title }}
    </div>
    <div>
        <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'moduleTitle',
  props: {
    showImg: {
      type: Boolean,
      default: true
    },
    imgUrl:{
      type:String,
      default:''
    },
    title:{
      type:String,
      default:''
    }
  },
  methods: {
    
  }
}
</script>

<style scoped lang="scss">
.box{
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: bold;
  img{
    width: 20px;
    height: auto;
    margin-right: 8px;
  }
  .left_box{
    display: flex;
    align-items: center;
  }
}
</style>
