import request from '@/utils/request'

//装备数量接口
export function getEquipNum(params) {
  return request({
    url: '/statisticsWarehouse/equipNum.do',
    method: 'get',
    params,
  })
}
// 装备趋势
export function getEquipTrend(params) {
  return request({
    url: '/statisticsWarehouse/equipNumList.do',
    method: 'get',
    params,
  })
}
// 装备类型
export function getEquipType(params) {
  return request({
    url: '/statisticsWarehouse/equipType.do',
    method: 'get',
    params
  })
}
//分局装备统计
export function getEquipDept(params) {
  return request({
    url: '/statisticsWarehouse/equipDept.do',
    method: 'get',
    params
  })
}
export function addInOrder(data) {
  return request({
    url: '/enterWarehouse/add.do',
    method: 'post',
    data,
    headers:{
      "Content-Type": "application/json;charset=UTF-8"
    }
  })
}
//系统通知
export function getNotice(params) {
  return request({
    url: '/statisticsWarehouse/noticeList.do',
    method: 'get',
    params,
  })
}

