<template>
  <div class="breadcrumb-Box">
    <div class="breadcrumb-content">
      <i class="el-icon-location"></i>
      <el-breadcrumb class="app-breadcrumb" separator-class="el-icon-arrow-right">
        <transition-group name="breadcrumb">
          <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
            <span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
            <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
          </el-breadcrumb-item>
        </transition-group>
      </el-breadcrumb>
    </div>
  </div>

</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null,
      title:null
    }
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false);
      this.title = this.levelList[this.levelList.length-1].meta.title
    },
    pathCompile(path) {
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item;
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
.breadcrumb-Box{display:flex;justify-content: space-between;width:100%;box-sizing: border-box;padding:0 20px 0 10px;align-items: center;
.breadcrumb-title{font-size:14px;font-weight:700;}
.breadcrumb-content{align-items: center;display:flex;i{margin-right:0px;font-size:16px;color:#999;}}
}
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 12px;
  line-height: 35px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
