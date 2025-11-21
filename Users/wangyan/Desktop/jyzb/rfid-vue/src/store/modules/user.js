import { login, logout, getInfo,loginNumber } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

function getRouter(data){
  let routers = data.map(el=>{
    if(el.children.length){
      el.children = getRouter(el.children);
    }
    return {
      path: el.route_url,
      component: el.vue_path,
      name: el.route_name,
      meta: { title: el.permission_name,icon:el.icon },
      children:el.children||[]
    }
  })
  return routers;
}

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    roles: [],
    department:'',
    departmentName:''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_DEPARTMENT: (state, {department,dept_name}) => {
    state.department = department;
    state.departmentName = dept_name;
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { account, password} = userInfo
    return new Promise((resolve, reject) => {
      login({ consumer: account.trim(), cypher: password}).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.accessToken)
        commit('SET_NAME', data.user.user_name)
        commit('SET_DEPARTMENT', {department:data.user.department,dept_name:data.user.dept_name})
        setToken(data.accessToken)
        let routers = getRouter(data.roles);
        routers[0].path = '/';
        resolve(routers)
      }).catch(error => {
        reject(error)
      })
    })
  },

  loginNumber({ commit }, consumer) {
    return new Promise((resolve, reject) => {
      loginNumber({ consumer}).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.accessToken)
        commit('SET_NAME', data.user.user_name)
        commit('SET_DEPARTMENT', {department:data.user.department,dept_name:data.user.dept_name})
        setToken(data.accessToken)
        let routers = getRouter(data.roles);
        routers[0].path = '/';
        resolve(routers)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        if (!data) {
          removeToken() // must remove  token  first
          resetRouter()
          commit('RESET_STATE')
          router.replace('/login');
          return
        }
        commit('SET_NAME', data.user.user_name)
        commit('SET_DEPARTMENT', {department:data.user.department,dept_name:data.user.dept_name})
        let routers = getRouter(data.roles);
        routers[0].path = '/';
        resolve(routers)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout().then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      resetRouter()
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

