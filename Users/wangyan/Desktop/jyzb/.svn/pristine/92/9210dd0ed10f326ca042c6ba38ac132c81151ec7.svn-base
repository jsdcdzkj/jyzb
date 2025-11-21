<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 v-else class="sidebar-title">{{ title }} </h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
       <img src="../../../assets/logo.png" style="height:40px;margin-right:10px;margin-left:10px;">
       <h1 class="sidebar-title">{{ title }} </h1>
        <!-- <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 class="sidebar-title">{{ title }} </h1> -->
      </router-link>
    </transition>
  </div>
</template>

<script>
import defaultSettings from '@/settings'
const title = defaultSettings.title
const logo=require('../../../assets/logo.png')
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title,
      logo
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 60px;
  line-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

    & .sidebar-logo {
      width: 80px;
      height: 80px;
      margin: 0 6px;
    }

    & .sidebar-title {
      flex: 1;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      display: inline-block;
      margin: 0;
      color:#fff;
      font-weight: 700;
      line-height: 60px;
      font-size: 28px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin: 0px;
    }
  }
}

.mobile {
  .sidebarLogoFade-enter-active {
    transition: none;
  }

  .sidebar-logo-container {
    .sidebar-logo {
      margin: 0 !important;
    }
    .sidebar-title {
      display: none;
    }
  }
}
</style>
