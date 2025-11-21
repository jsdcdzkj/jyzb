<template>
  <div class="top-menu-bar">
    <el-menu :default-active="activeMenu" :text-color="variables.menuText"
      :active-text-color="variables.menuActiveText" mode="horizontal">
      <template v-for="route in permission_routes">
        <template v-if="!route.hidden && getFirstShowingChild(route.children, route)">
          <el-menu-item v-if="route.meta" :key="route.path" :index="resolvePath(route)" @click="jumpTo(route)">
            <div class="link-img" v-if="route.meta.icon">
              <img :src="require(`@/assets/menu/${route.meta.icon}.png`)" />
            </div>
            <item :title="route.meta.title" />
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import store from '@/store'
import { mapGetters } from 'vuex'
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import variables from '@/styles/variables.scss'

export default {
  components: { Item },
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, matched } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      store.dispatch('permission/changeSecondRoutes', matched[0])
      return matched[0].path || '/'
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    getFirstShowingChild(children = [], parent) {
      let showing = true
      if (parent.hidden || (typeof parent.path === 'undefined' && typeof parent.redirect === 'undefined')) {
        showing = false
      }
      if (!children) {
        return showing
      }
      const showingChildren = children.find(item => {
        return !item.hidden && item.path
      })
      if (typeof showingChildren !== 'undefined') {
        if (showingChildren.path && parent.alwaysShow && !parent.redirect) {
          parent.redirect = showingChildren.path
        }
        if (typeof parent.meta === 'undefined' && typeof showingChildren.meta !== 'undefined') {
          parent.meta = showingChildren.meta
        }
        showing = true
      }
      return showing
    },
    resolvePath(route) {
      if (isExternal(route.path)) {
        return route.path
      }
      if (isExternal(route.redirect)) {
        return route.redirect
      }
      return route.path
    },
    jumpTo(route) {
      // console.log(route)
      if (isExternal(route.path)) {
        window.open(route.path, '_blank')
        return
      }
      if (isExternal(route.redirect)) {
        window.open(route.redirect, '_blank')
        return
      }
      this.$router.push(path.resolve(route.path, route.redirect || ''))
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep {
  .el-menu {
    background: transparent !important;
  }
}

.top-menu-bar {
  .el-menu--horizontal.el-menu {
    display: flex;
    justify-content: center;
    overflow: hidden;
    border-bottom: 0 !important;
  }

  .el-menu--horizontal>.el-menu-item {
    width:120px;
    height: 48px !important;
    line-height: 48px !important;
    color: #fff !important;
    font-size: 16px !important;
    display: flex;
    margin-right: 16px;
    justify-content: center;
    align-items: center;
    position: relative;
    padding: 0 !important;
    border-radius: 2px;
    border: 1px solid rgba(61, 118, 241, 1)!important;
    box-sizing: border-box;
    &:nth-last-child(1){
      margin-right: 0;
    }
    .link-img {
      margin-left: 20px;
      margin-right: 10px;
      display: flex;
      justify-content: center;
      align-items: center;

      img {
        width: 18px;
        height: 18px;
        opacity: .8
      }
    }

    ::v-deep {
      span {
        margin-right: 20px;
        display: block;
      }
    }
  }

  .el-menu--horizontal>.el-menu-item:hover {
    background: linear-gradient(270deg, #3D76F1 0%, #345FB9 100%) !important;
    border: none!important;
    .link-img {
      img {
        opacity: 1
      }
    }
  }

  .el-menu--horizontal>.el-menu-item.is-active {
    background: linear-gradient(270deg, #3D76F1 0%, #345FB9 100%) !important;
    border: none!important;
    .link-img {
      display: flex;
      justify-content: center;
      align-items: center;

      img {
        opacity: 1
      }
    }
  }

  .svg-icon {
    margin-right: 5px;
  }

  .el-scrollbar__bar.is-horizontal {
    display: none;
  }

  ::v-deep .el-scrollbar__thumb {
    background-color: rgba(255, 255, 255, 0.5);

    &:hover {
      background-color: rgba(64, 158, 255, 1);
    }
  }
}

.mobile {
  .top-menu-bar {
    ::v-deep .el-menu--horizontal>.el-menu-item {
      padding: 0 10px;
      display: inline-block;

      .sub-el-icon {
        display: none;
      }

      .svg-icon {
        display: none;
      }
    }

    ::v-deep .el-scrollbar__thumb {
      display: none;

      &:hover {
        display: none;
      }
    }
  }
}
</style>
