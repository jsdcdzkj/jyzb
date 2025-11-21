import { constantRoutes } from '@/router'
// import { getroutes } from '@/api/permission'
import Layout from '@/layout'
import EmptyLayout from '@/layout/EmptyLayout'
/**
 * 循环生成异步路由
 * @Author   JSDINGCHI-LKS
 * @DateTime 2024-04-16
 * @param    {[type]}    routes [description]
 * @return   {[type]}           [description]
 */
function loopCreateRouter(routes) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    const tpl = Object.assign({}, tmp)
    if (typeof tmp['component'] !== 'undefined' && tmp['component']) {
      if (tmp['component'] === 'Layout') {
        tpl['component'] = Layout
      } else if (tmp['component'] === 'EmptyLayout') {
        tpl['component'] = EmptyLayout
      } else {
        tpl['component'] = resolve => require([`../../${tmp['component']}.vue`], resolve)
      }
    }
    if (tmp['children']) {
      tpl['children'] = loopCreateRouter(tmp['children'])
    }
    res.push(tpl)
  })
  return res
}

const state = {
  routes: [],
  second_routes: [],
  third_routes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  SET_SECOND_ROUTES: (state, routes) => {
    state.second_routes = routes
  },
  SET_THIRD_ROUTES: (state, routes) => {
    state.third_routes = routes
  }
}

const actions = {
  generateRoutes({ commit },newroutes) {
    return new Promise((resolve, reject) => {
      
      // const newroutes = [
      //   {
      //     path: '/',
      //     component: 'Layout',
      //     redirect: '/dashboard',
      //     name: 'Home',
      //     id: 1,
      //     meta: { title: '首页',icon:'icon_home' },
      //     children: [
      //       {
      //         path: '/',
      //         id: 2,
      //         name: 'dashboard',
      //         component: 'views/dashboard/index',
      //         meta: { title: '装备概览',icon:'form' }
      //       },
      //       {
      //         path: '/home/EquipmentStatistics',
      //         id: 3,
      //         name: 'EquipmentStatistics',
      //         component: 'views/home/EquipmentStatistics/index',
      //         meta: { title: '装备统计',icon:'flow-chart' }
      //       },
      //       {
      //         path: '/home/Warehousing',
      //         id: 3,
      //         name: 'Warehousing',
      //         component: 'views/home/Warehousing/index',
      //         meta: { title: '入库管理',icon:'flow-chart' }
      //       },
      //       {
      //         path: '/home/stock',
      //         id: 3,
      //         name: 'stock',
      //         component: 'views/home/stock/index',
      //         meta: { title: '库存管理',icon:'flow-chart' }
      //       },
      //       {
      //         path: '/home/Outbound',
      //         id: 3,
      //         name: 'Outbound',
      //         component: 'views/home/Outbound/index',
      //         meta: { title: '出库管理',icon:'flow-chart' }
      //       }
      //     ]
      //   },
      //   {
      //     path: '/GeneralEquipment',
      //     component: 'Layout',
      //     redirect: '/Generalindex',
      //     name: 'GeneralEquipment',
      //     id: 10,
      //     meta: { title: '通用装备',icon:'icon_tyzb'},
      //     children: [
      //       {
      //         path: '/Generalindex',
      //         id: 102,
      //         name: 'GeneralEquipmentOverview',
      //         component: 'views/dashboard/generalEquipment',
      //         meta: { title: '通用装备概览',icon:'server-line' }
      //       },
      //       {
      //         path: '/GeneralStatistics_1',
      //         id: 102,
      //         name: 'GeneralEquipmentStatistics',
      //         component: 'views/home/EquipmentStatistics/index',
      //         meta: { title: '通用装备统计',icon:'file-paper-line' }
      //       }
      //     ]
      //   },
      //   {
      //     path: '/SpecialEquipment',
      //     component: 'Layout',
      //     redirect: '/SpecialIndex',
      //     name: 'FileManagement',
      //     id: 10,
      //     meta: { title: '特种装备',icon:'icon_tzzb'},
      //     children: [
      //       {
      //         path: '/SpecialIndex',
      //         id: 102,
      //         name: 'SpecialIndex',
      //         component: 'views/dashboard/specialEquipment',
      //         meta: { title: '特种装备概览',icon:'server-line' }
      //       },
      //       {
      //         path: '/SpecialStatistics_2',
      //         id: 102,
      //         name: 'SpecialStatistics',
      //         component: 'views/home/EquipmentStatistics/index',
      //         meta: { title: '特种装备统计',icon:'file-paper-line' }
      //       }
      //     ]
      //   },
      //   {
      //     path: '/EmergencySupplies',
      //     component: 'Layout',
      //     redirect: '/EmergencyIndex',
      //     name: 'EmergencySupplies',
      //     id: 10,
      //     meta: { title: '应急库物资',icon:'icon_yjwz'},
      //     children: [
      //       {
      //         path: '/EmergencyIndex',
      //         id: 102,
      //         name: 'EmergencyIndex',
      //         component: 'views/dashboard/emergencySupplies',
      //         meta: { title: '应急库物资概览',icon:'server-line' }
      //       },
      //       {
      //         path: '/EmergencyStatistics_3',
      //         id: 102,
      //         name: 'EmergencyStatistics',
      //         component: 'views/home/EquipmentStatistics/index',
      //         meta: { title: '应急库物资统计',icon:'file-paper-line' }
      //       }
      //     ]
      //   },
      //   {
      //     path: '/CarManagement',
      //     component: 'Layout',
      //     redirect: '/CarIndex',
      //     name: 'CarManagement',
      //     id: 10,
      //     meta: { title: '车辆管理',icon:'icon_car'},
      //     children: [
      //       {
      //         path: '/CarIndex',
      //         id: 102,
      //         name: 'CarIndex',
      //         component: 'views/dashboard/carManagement',
      //         meta: { title: '车辆概览',icon:'server-line' }
      //       },
      //       {
      //         path: '/CarStatistics_4',
      //         id: 102,
      //         name: 'CarStatistics',
      //         component: 'views/home/EquipmentStatistics/index',
      //         meta: { title: '车辆统计',icon:'file-paper-line' }
      //       }
      //     ]
      //   },
      //   {
      //     path: '/setting',
      //     component: 'Layout',
      //     redirect: '/settingUser',
      //     name: 'Setting',
      //     id: 6,
      //     meta: { title: '系统管理',icon:'icon_xtgl' },
      //     children: [
      //       {
      //         path: '/settingUser',
      //         name: 'User',
      //         id: 61,
      //         component: 'views/systemSet/user/index',
      //         meta: { title: '用户管理',icon:'file-paper-line' }
      //       },
      //       {
      //         path: '/settingRole',
      //         name: 'Role',
      //         id: 62,
      //         component: 'views/systemSet/role/index',
      //         meta: { title: '角色管理',icon:'file-paper-line' }
      //       },
      //       {
      //         path: '/settingMenu',
      //         name: 'settingMenu',
      //         id: 63,
      //         component: 'views/systemSet/menu/index',
      //         meta: { title: '菜单管理',icon:'file-paper-line' }
      //       },{
      //         path: '/settingOrg',
      //         name: 'settingOrg',
      //         id: 63,
      //         component: 'views/systemSet/Org/index',
      //         meta: { title: '组织架构',icon:'file-paper-line' }
      //       },{
      //         path: '/settingEquip',
      //         name: 'Equip',
      //         id: 62,
      //         component: 'views/systemSet/Equip/index',
      //         meta: { title: '装备类型管理',icon:'file-paper-line' }
      //       },
      //       {
      //         path: '/settingSupplier',
      //         name: 'Supplier',
      //         id: 62,
      //         component: 'views/systemSet/Supplier/index',
      //         meta: { title: '供应商管理',icon:'file-paper-line' }
      //       },
      //       {
      //         path: '/settingWarehouse',
      //         name: 'Warehouse',
      //         id: 62,
      //         component: 'views/systemSet/Warehouse/index',
      //         meta: { title: '仓库管理',icon:'file-paper-line' }
      //       },
      //     ]
      //   }
      // ]

      const newarry = newroutes;
      for (let i = 0; i < newarry.length; i++) {
        newarry[i].redirect = newarry[i].children[0].path
      }
      const asyncRouterMap = newarry;
      const accessedRoutes = loopCreateRouter(asyncRouterMap)
      commit('SET_ROUTES', constantRoutes.concat(accessedRoutes))
      resolve(accessedRoutes)
    })
  },
  changeSecondRoutes({ commit, state }, data) {
    commit('SET_SECOND_ROUTES', [])
    const second_routes = state.routes.find(item => {
      return !item.hidden && (item.path === data.path || (data.path === '' && item.path === '/'))
    })
    if (typeof second_routes !== 'undefined' && second_routes.children) {
      const res = second_routes.children.filter(item => {
        return !item.hidden && item.path
      })
      if (res.length > 0) {
        commit('SET_SECOND_ROUTES', res)
      }
    }
  },
  changeThirdRoutes({ commit, state }, data) {
    commit('SET_THIRD_ROUTES', [])
    const third_routes = state.second_routes.find(item => {
      return !item.hidden && (item.path === data.path || (data.path === '' && item.path === '/'))
    })
    if (typeof third_routes !== 'undefined' && third_routes.children) {
      const res = third_routes.children.filter(item => {
        return !item.hidden && item.path
      })
      if (res.length > 1) {
        commit('SET_THIRD_ROUTES', res)
      }
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
