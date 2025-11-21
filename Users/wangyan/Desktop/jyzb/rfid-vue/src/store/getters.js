const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  name: state => state.user.name,
  roles: state => state.user.roles,
  dept_name: state => state.user.departmentName,
  permission_routes: state => state.permission.routes,
  second_routes: state => state.permission.second_routes,
  third_routes: state => state.permission.third_routes
}
export default getters
