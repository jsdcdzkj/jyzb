import request from '@/utils/request'

// 库存接口
export function getStockList(data) {
  return request({
    url: '/stock/toListAll.do',
    method: 'post',
    data,
  })
}

//借用申请
export function shareApply(data) {
  return request({
    url: '/stock/apply.do',
    method: 'post',
    data,
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    }
  })
}

//借用申请列表
export function applyList(data) {
  return request({
    url: '/stock/applyPageQuery.do',
    method: 'post',
    data
  })
}
//借出列表
export function applyLendList(data) {
  return request({
    url: '/stock/applyLendPageQuery.do',
    method: 'post',
    data
  })
}
//借用申请删除
export function applyDel(data) {
  return request({
    url: '/stock/applyDel.do',
    method: 'post',
    data
  })
}
//借用页面申请人列表
export function applyPerson(data) {
  return request({
    url: '/stock/applyPerson.do',
    method: 'post',
    data
  })
}
//借出页面申请人列表
export function applyLendPerson(data) {
  return request({
    url: '/stock/applyLendPerson.do',
    method: 'post',
    data
  })
}
//借出详情仓库数据
export function equipStockWarehouse(data) {
  return request({
    url: '/stock/equipStockWarehouse.do',
    method: 'post',
    data
  })
}
//借用申请同意
export function applyAgree(data) {
  return request({
    url: '/stock/applyAgree.do',
    method: 'post',
    data,
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    }
  })
}
//借用申请拒绝
export function applyReject(data) {
  return request({
    url: '/stock/applyReject.do',
    method: 'post',
    data
  })
}
//借用归还提交
export function applyReturn(data) {
  return request({
    url: '/stock/applyReturn.do',
    method: 'post',
    data
  })
}
//借用确认归还
export function applyConfirmReturn(data) {
  return request({
    url: '/stock/applyConfirmReturn.do',
    method: 'post',
    data
  })
}
//借用详情接口
export function applyDetail(data) {
  return request({
    url: '/stock/applyLendDetail.do',
    method: 'post',
    data
  })
}