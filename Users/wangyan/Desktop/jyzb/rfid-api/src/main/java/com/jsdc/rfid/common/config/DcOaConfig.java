//package com.jsdc.rfid.common.config;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.common.utils.*;
//import com.jsdc.rfid.model.SysDepartment;
//import com.jsdc.rfid.model.SysPost;
//import com.jsdc.rfid.model.SysUser;
//import com.jsdc.rfid.service.SysDepartmentService;
//import com.jsdc.rfid.service.SysPostService;
//import com.jsdc.rfid.service.SysUserService;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.*;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.PostConstruct;
//import javax.transaction.Transactional;
//import java.net.URLDecoder;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class DcOaConfig {
//
//    //调用接口
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private SysDepartmentService sysDepartmentService;
//    @Autowired
//    private SysPostService sysPostService;
//    @Autowired
//    private SysUserService sysUserService;
//
//
//    @Value("${oa.ip}")
//    public String ip;
//
//    @Value("${oa.port}")
//    public String port;
//
//    @Value("${jsdc.isCompany}")
//    public String isCompany;
//
//
//    /**
//     *  获取token
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void getToken(){
//        if (StringUtils.isBlank(isCompany) || !StringUtils.equals(isCompany, "1")){
//            return;
//        }
//
//        String url="http://"+ip+":"+port+"/seeyon/rest/token";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        Map<String, Object> requestParam = new HashMap<>();
//        requestParam.put("userName", "dcrest");
//        requestParam.put("password", "b4957d1e-b2b0-44b7-b62d-5d2aadc18c79");
//        HttpEntity entity = new HttpEntity(requestParam, headers);
//        JSONObject result = restTemplate.postForObject(url, entity, JSONObject.class);
//        DcOaData.Instance().setTokenValue(result.getString("id"));
//    }
//
//
//    /**
//     *  同步OA所有获取部门
//     */
//    @Scheduled(cron = "0 10 0 * * ?")
//    @Transactional
//    public void getOrgDepartments(){
//        if (StringUtils.isBlank(isCompany) || !StringUtils.equals(isCompany, "1")){
//            return;
//        }
//        String url="http://"+ip+":"+port+"/seeyon/rest/orgDepartments/670869647114347";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("token", DcOaData.Instance().getTokenValue());
//
//
//
//        ResponseEntity<JSONArray> entity = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers), JSONArray.class);
//
//        entity.getBody().forEach(x->{
//            DCDeptVo dcSysDept= JSONObject.parseObject(JSON.toJSONString(x),DCDeptVo.class);
//            QueryWrapper<SysDepartment> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("is_del","0").eq("oa_dept_id",dcSysDept.getId());
//            SysDepartment sysDepartment2 = sysDepartmentService.selectOne(queryWrapper1);
//            if (null != sysDepartment2){
//                sysDepartment2.setDept_name(dcSysDept.getName());
//                QueryWrapper<SysDepartment> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("is_del","0").eq("oa_dept_id",dcSysDept.getSuperior());
//                SysDepartment sysDepartment1 = sysDepartmentService.selectOne(queryWrapper);
//                if (null!= sysDepartment1) {
//                    sysDepartment2.setParent_dept(sysDepartment1.getId());
//                }else {
//                    sysDepartment2.setParent_dept(0);
//                }
//                sysDepartment2.setIs_enable(G.ISENABLE_YES);
//                sysDepartment2.setUpdate_time(new Date());
//                sysDepartmentService.updateById(sysDepartment2);
//
//            }else {
//                SysDepartment sysDepartment = new SysDepartment();
//                sysDepartment.setDept_name(dcSysDept.getName());
//                QueryWrapper<SysDepartment> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("is_del","0").eq("oa_dept_id",dcSysDept.getSuperior());
//                SysDepartment sysDepartment1 = sysDepartmentService.selectOne(queryWrapper);
//                if (null!= sysDepartment1) {
//                    sysDepartment.setParent_dept(sysDepartment1.getId());
//                }else {
//                    sysDepartment.setParent_dept(0);
//                }
//                sysDepartment.setDept_code(PinYinUtil.getFirstSpell(dcSysDept.getName()));
//                sysDepartment.setIs_del("0");
//                sysDepartment.setOa_dept_id(dcSysDept.getId());
//                sysDepartment.setIs_enable(G.ISENABLE_YES);
//                sysDepartment.setCreate_time(new Date());
//                sysDepartmentService.insert(sysDepartment);
//            }
//        });
//    }
//
//    /**
//     *  同步OA所有岗位
//     */
//    @Transactional
//    @Scheduled(cron = "0 5 0 * * ?")
//    public void getOrgPosts(){
//        if (StringUtils.isBlank(isCompany) || !StringUtils.equals(isCompany, "1")){
//            return;
//        }
//        String url="http://"+ip+":"+port+"/seeyon/rest/orgPosts/670869647114347";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("token",DcOaData.Instance().getTokenValue());
//        ResponseEntity<JSONArray> entity = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers), JSONArray.class);
//
//        entity.getBody().forEach(x->{
//            DCPostVo dcSysPost=JSONObject.parseObject(JSON.toJSONString(x),DCPostVo.class);
//
//            QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("is_del","0").eq("oa_post_id",dcSysPost.getId());
//            SysPost sysPost = sysPostService.selectOne(queryWrapper);
//            if (null != sysPost){
//                sysPost.setPost_name(dcSysPost.getName());
//                sysPost.setUpdate_time(new Date());
//                sysPost.setIs_enable(G.ISENABLE_YES);
//                sysPostService.updateById(sysPost);
//            }else {
//                SysPost sysPost1 = new SysPost();
//                sysPost1.setOa_post_id(dcSysPost.getId());
//                sysPost1.setPost_name(dcSysPost.getName());
//                sysPost1.setCreate_time(new Date());
//                sysPost1.setIs_del("0");
//                sysPost1.setIs_enable(G.ISENABLE_YES);
//                sysPost1.setData_permission(0);
//                sysPost1.setPost_code(PinYinUtil.getFirstSpell(dcSysPost.getName()));
//                sysPostService.insert(sysPost1);
//            }
//        });
//    }
//
//
//    /**
//     *  根据单位id获取单位所有人员
//     */
//    @Scheduled(cron = "0 15 0 * * ?")
//    @Transactional
//    public void getOrgMembers(){
//        if (StringUtils.isBlank(isCompany) || !StringUtils.equals(isCompany, "1")){
//            return;
//        }
//        String url="http://"+ip+":"+port+"/seeyon/rest/orgMembers/670869647114347";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("token",DcOaData.Instance().getTokenValue());
//        ResponseEntity<JSONArray> entity = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers), JSONArray.class);
//
//
//        entity.getBody().forEach(x->{
//
//            DCUserVo dcSysUser=JSONObject.parseObject(JSON.toJSONString(x),DCUserVo.class);
//
//            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("is_del","0").eq("oa_user_id",dcSysUser.getId());
//            SysUser sysUser = sysUserService.selectOne(queryWrapper);
//            if (null != sysUser){
//                //修改时间
//                sysUser.setUpdate_time(new Date());
//                //真实姓名
//                sysUser.setUser_name(dcSysUser.getName());
//                //手机号码
//                sysUser.setTelephone(dcSysUser.getTelNumber());
//                //性别
//                if( -1 != dcSysUser.getGender()){
//                    if (1 == dcSysUser.getGender()){
//                        sysUser.setSex(1);
//                    }else {
//                        sysUser.setSex(0);
//                    }
//                }
//                //部门
//                QueryWrapper<SysDepartment> departmentQueryWrapper = new QueryWrapper<>();
//                departmentQueryWrapper.eq("is_del","0").eq("oa_dept_id",dcSysUser.getOrgDepartmentId());
//                SysDepartment sysDepartment = sysDepartmentService.selectOne(departmentQueryWrapper);
//                if (null != sysDepartment){
//                    sysUser.setDepartment(sysDepartment.getId());
//                }
//                //岗位
//                QueryWrapper<SysPost> postQueryWrapper = new QueryWrapper<>();
//                postQueryWrapper.eq("is_del","0").eq("oa_post_id",dcSysUser.getOrgPostId());
//                SysPost sysPost = sysPostService.selectOne(postQueryWrapper);
//                if (null != sysPost){
//                    sysUser.setPost(sysPost.getId());
//                }
//                sysUserService.updateById(sysUser);
//
//            }else {
//                SysUser sysUser1 = new SysUser();
//                //删除标识
//                sysUser1.setIs_del("0");
//                //创建时间
//                sysUser1.setCreate_time(new Date());
//                //oa id
//                sysUser1.setOa_user_id(dcSysUser.getId());
//                //登录名
//                sysUser1.setLogin_name(PinYinUtil.getPingYin(dcSysUser.getName()));
//                //密码
//                sysUser1.setPassword("e10adc3949ba59abbe56e057f20f883e");
//                //真实姓名
//                sysUser1.setUser_name(dcSysUser.getName());
//                //手机号码
//                sysUser1.setTelephone(dcSysUser.getTelNumber());
//                //性别
//                if( -1 != dcSysUser.getGender()){
//                    if (1 == dcSysUser.getGender()){
//                        sysUser1.setSex(1);
//                    }else {
//                        sysUser1.setSex(0);
//                    }
//                }
//                //部门
//                QueryWrapper<SysDepartment> departmentQueryWrapper = new QueryWrapper<>();
//                departmentQueryWrapper.eq("is_del","0").eq("oa_dept_id",dcSysUser.getOrgDepartmentId());
//                SysDepartment sysDepartment = sysDepartmentService.selectOne(departmentQueryWrapper);
//                if (null != sysDepartment){
//                    sysUser1.setDepartment(sysDepartment.getId());
//                }
//                //岗位
//                QueryWrapper<SysPost> postQueryWrapper = new QueryWrapper<>();
//                postQueryWrapper.eq("is_del","0").eq("oa_post_id",dcSysUser.getOrgPostId());
//                SysPost sysPost = sysPostService.selectOne(postQueryWrapper);
//                if (null != sysPost){
//                    sysUser1.setPost(sysPost.getId());
//                }
//                //是否启用
//                sysUser1.setIs_enable(G.ISENABLE_YES);
//                sysUserService.insert(sysUser1);
//            }
//
//        });
//    }
//
//
//    /**
//     *  oa单点登录
//     * @param ticket
//     * @return
//     */
//    public String oaLogin(String ticket){
//        String url="http://"+ip+":"+port+"/seeyon/thirdpartyController.do?ticket="+ticket;
//        String response=restTemplate.getForObject(url,String.class);
//        try {
//            return URLDecoder.decode(response,"utf-8").replaceAll("\r\n","");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//
//
//}
