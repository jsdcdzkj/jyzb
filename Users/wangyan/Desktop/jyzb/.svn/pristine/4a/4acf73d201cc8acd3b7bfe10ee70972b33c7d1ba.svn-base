import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import qs from "qs";
import router from '@/router';

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API?process.env.VUE_APP_BASE_API:window.location.origin+'/jyzb_api',
  timeout: 30000,
  headers: {
    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
  }
})

// request interceptor
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['accessToken'] = store.getters.token;
    }
    if (config.data && config.headers["Content-Type"] === "application/x-www-form-urlencoded;charset=UTF-8"){
      config.data = qs.stringify(config.data);
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    if (response.config.responseType === "blob") {
      return response;
    }
    const res = response.data;
    if(res.code==43000){
      store.dispatch('user/resetToken');
      router.replace('/login');
      return
    }else if (res.code !== 0) {
      Message({
        message: res.msg || 'Error',
        type: 'error'
      })
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    Message({
      message: error.msg||'请检查网络设置',
      type: 'error'
    })
    return Promise.reject(error)
  }
)

export default service
