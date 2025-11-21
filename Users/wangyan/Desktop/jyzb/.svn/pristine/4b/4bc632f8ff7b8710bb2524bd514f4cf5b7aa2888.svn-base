import request from '@/utils/request'

//供应商接口
export function getSupplierList(data) {
  return request({
    url: '/supplier/getPage.do',
    method: 'post',
    data,
  })
}
export function getAllSupplier(data) {
  return request({
    url: '/supplier/getList.do',
    method: 'post',
    data,
  })
}
export function addSupplier(data) {
  return request({
    url: '/supplier/add.do',
    method: 'post',
    data,
  })
}
export function editSupplier(data) {
  return request({
    url: '/supplier/edit.do',
    method: 'post',
    data,
  })
}
export function delSupplier(params) {
  return request({
    url: '/supplier/del.do',
    method: 'get',
    params,
  })
}

//仓库管理接口
export function getWarehouseList(data) {
  return request({
    url: '/warehouse/getPage.do',
    method: 'post',
    data,
  })
}
export function getAllWarehouse(params) {
  return request({
    url: '/warehouse/toList.do',
    method: 'get',
    params,
  })
}
export function addWarehouse(data) {
  return request({
    url: '/warehouse/add.do',
    method: 'post',
    data,
  })
}
export function editWarehouse(data) {
  return request({
    url: '/warehouse/edit.do',
    method: 'post',
    data,
  })
}
export function delWarehouse(params) {
  return request({
    url: '/warehouse/del.do',
    method: 'get',
    params,
  })
}

// 装备类型管理
export function downTemplate(params) {
  return request({
    url: '/assetsType/toExportTemplate.do',
    method: 'get',
    responseType: "blob",
    params,
  })
}
export function exportAllTreeData(params) {
  return request({
    url: '/assetsType/exportAllTreeData.do',
    method: 'get',
    responseType: "blob",
    params,
  })
}
export function importFile(data) {
  return request({
    url: '/assetsType/toImport.do',
    method: 'post',
    data
  })
}
export function getEquipmentTree(data) {
  return request({
    url: '/assetsType/getTree.do',
    method: 'post',
    data,
  })
}
export function getEquipmentList(data) {
  return request({
    url: '/assetsType/toList.do',
    method: 'post',
    data,
  })
}
export function addEquipment(data) {
  return request({
    url: '/assetsType/add.do',
    method: 'post',
    data,
  })
}
export function editEquipment(data) {
  return request({
    url: '/assetsType/edit.do',
    method: 'post',
    data,
  })
}
export function delEquipment(params) {
  return request({
    url: '/assetsType/del.do',
    method: 'get',
    params,
  })
}

// 组织架构
export function getDeptTree(data) {
  return request({
    url: '/dept/getTree.do',
    method: 'post',
    data,
  })
}
export function getDeptTreeThree(data) {
  return request({
    url: '/dept/getThree.do',
    method: 'post',
    data,
  })
}
export function getDeptTreeTwo(data) {
  return request({
    url: '/dept/getTreeTwo.do',
    method: 'post',
    data,
  })
}
export function deptMerge(data) {
  return request({
    url: '/dept/merge.do',
    method: 'post',
    data,
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    }
  })
}
export function findDept(data) {
  return request({
    url: '/dept/toList.do',
    method: 'post',
    data,
  })
}
export function addDept(data) {
  return request({
    url: '/dept/add.do',
    method: 'post',
    data,
  })
}
export function editDept(data) {
  return request({
    url: '/dept/edit.do',
    method: 'post',
    data,
  })
}
export function delDept(params) {
  return request({
    url: '/dept/del.do',
    method: 'get',
    params,
  })
}
// 菜单管理
export function getMenuTree(params) {
  return request({
    url: '/permission/getTree.do',
    method: 'get',
    params,
  })
}
export function delMenu(params) {
  return request({
    url: '/permission/del.do',
    method: 'get',
    params,
  })
}
export function addMenu(data) {
  return request({
    url: '/permission/add.do',
    method: 'post',
    data,
  })
}
export function editMenu(data) {
  return request({
    url: '/permission/edit.do',
    method: 'post',
    data,
  })
}
//角色管理
export function getRolePage(params) {
  return request({
    url: '/post/getPage.do',
    method: 'get',
    params,
  })
}
export function getRoleList(params) {
  return request({
    url: '/post/getList.do',
    method: 'get',
    params,
  })
}
export function delRole(params) {
  return request({
    url: '/post/del.do',
    method: 'get',
    params,
  })
}
export function addRole(data) {
  return request({
    url: '/post/add.do',
    method: 'post',
    data,
  })
}
export function editRole(data) {
  return request({
    url: '/post/edit.do',
    method: 'post',
    data,
  })
}
export function detailRole(params) {
  return request({
    url: '/post/detail.do',
    method: 'get',
    params,
  })
}
// 用户管理
export function getUserPage(params) {
  return request({
    url: '/user/getPage.do',
    method: 'get',
    params,
  })
}
// 用户管理导入
export function userImportFile(data) {
  return request({
    url: '/user/toImport.do',
    method: 'post',
    data
  })
}
export function delUser(params) {
  return request({
    url: '/user/del.do',
    method: 'get',
    params,
  })
}
export function addUser(data) {
  return request({
    url: '/user/add.do',
    method: 'post',
    data,
  })
}
export function editUser(data) {
  return request({
    url: '/user/edit.do',
    method: 'post',
    data,
  })
}
export function changePwd(params) {
  return request({
    url: '/user/resetPassword.do',
    method: 'get',
    params,
  })
}
// 下载文件
export function downLoadFile(fileId) {
  return request({
    url: '/file/'+fileId+'/downloadFile.do',
    method: 'get',
    responseType: "blob"
  })
}
export function toExportTemplate(params) {
  return request({
    url: '/user/toExportTemplate.do',
    method: 'get',
    responseType: "blob",
    params,
  })
}
