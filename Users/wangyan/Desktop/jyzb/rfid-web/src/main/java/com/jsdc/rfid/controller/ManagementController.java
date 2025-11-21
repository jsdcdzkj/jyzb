package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.enums.ManagementStatusEnums;
import com.jsdc.rfid.mapper.ManagementAssetsFileMemberMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private ManagementService managementService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private ManagementAssetsService assetsService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private ManagementAssetsService managementAssetsService;

    @Autowired
    private ManagementAssetsFileMemberMapper managementAssetsFileMemberMapper;

    @Autowired
    private FileManageService fileManageService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 跳转资产展示页面
     *
     * @return
     */
    @RequestMapping("toSelect.do")
    public String toSelect(Model model){
        //获取所有资产类型
        QueryWrapper<AssetsType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<AssetsType> assetsTypes =  assetsTypeService.selectList(queryWrapper);
        model.addAttribute("assetsTypes", assetsTypes);
        return "change/dealWith/select";
    }

    /**
     * 跳转处置列表页
     * @return
     */
    @RequestMapping("toManageIndex.do")
    public String toManageIndex(Model model){
        // 得到所有部门
        List<SysDepartment> sysDepartments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, 0));
        model.addAttribute("departments", sysDepartments);
        return "change/dealWith/index";
    }


    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("view-log")
    public String toViewLog() {
        return "change/dealWith/view-log";
    }


    /**
     * 跳转处置新增页面
     * @return
     */
    @RequestMapping("toAddManage.do")
    public String toAddManage(Model model){
        SysUser sysUser = sysUserService.getUser();
        String dept_name = sysDepartmentService.selectById( sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);


        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del","0");
        List<SysUser> sysUsers = sysUserService.selectList(userqueryWrapper);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.ne("asset_state","6");
        List<AssetsManage> assetsManage=  assetsManageService.selectList(queryWrapper);
        model.addAttribute("assetsManage",assetsManage);
        model.addAttribute("sysUser",sysUser);
        model.addAttribute("sysUsers",sysUsers);
        return "change/dealWith/add";
    }

    /**
     * 跳转处置修改页面
     * @return
     */
    @RequestMapping("toUpdateManage.do")
    public String toUpdateManage(int id,Model model){
        Management management = managementService.getOneInfoById(id);
        SysUser sysUser = sysUserService.getUser();
        String dept_name = sysDepartmentService.selectById( sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);


        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del","0");
        List<SysUser> sysUsers = sysUserService.selectList(userqueryWrapper);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.ne("asset_state","6");
        List<AssetsManage> assetsManage=  assetsManageService.selectList(queryWrapper);
        List<ManagementAssets> managementAssets = managementService.getDetailInfo(id);
        model.addAttribute("managementAssets",managementAssets);

        List<Map<String, Object>> map = new ArrayList<>() ;
        for (ManagementStatusEnums enums : ManagementStatusEnums.values()){
            Map<String, Object> data = new HashMap<>();
            data.put("id", enums.getType());
            data.put("name", enums.getDesc());
            map.add(data);
        }
        model.addAttribute("statusTypes", map);
        model.addAttribute("management",management);
        model.addAttribute("assetsManage",assetsManage);
        model.addAttribute("sysUser",sysUser);
        model.addAttribute("sysUsers",sysUsers);
        return "change/dealWith/edit";
    }

    /**
     * 跳转展示详情页
     * @return
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id,Integer type,Model model){
        List<ManagementAssets> managementAssets = managementService.getDetailInfo(id);
        model.addAttribute("managementAssets",managementAssets);
        model.addAttribute("id",id);
        model.addAttribute("type",type);
        Management manage = managementService.getOneInfoById(id);
        try {
            processMemberService.getProcessDataByBusId(manage.getId(), G.PROCESS_ZCCZ,sysUserService.getUser(),manage);
        } catch (Exception e) {
            log.error("获取流程数据失败", e);
        }
        model.addAttribute("management", manage);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCCZ);
        model.addAttribute("key", dictionary.getValue());
        return "change/dealWith/view";
    }


    /**
     * 跳转管理列表页
     * @return
     */
    @RequestMapping("toDealWithManageIndex.do")
    public String toDealWithManageIndex(Model model){
        // 得到所有部门
        List<SysDepartment> sysDepartments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("departments", sysDepartments);
        return "change/dealWithManage/index";
    }


    /**
     * 跳转管理列表页
     * @return
     */
    @RequestMapping("toDealWithAssets.do")
    public String toDealWithAssets(int id,Model model){
        List<ManagementAssets> managementAssets = managementService.getDetailInfo(id);
        model.addAttribute("managementAssets",managementAssets);
        model.addAttribute("id",id);
        List<Map<String, Object>> map = new ArrayList<>() ;
        for (ManagementStatusEnums enums : ManagementStatusEnums.values()){
            Map<String, Object> data = new HashMap<>();
            data.put("id", enums.getType());
            data.put("name", enums.getDesc());
            map.add(data);
        }
        model.addAttribute("statusTypes", map);
        return "change/dealWithManage/apply";
    }

    /**
     * 跳转管理详情页
     */
    @RequestMapping("toDealWithView.do")
    public String toDealWithView(Integer id,Model model){

        ManagementAssets managementAsset = managementAssetsService.selectById(id);
        model.addAttribute("managementAsset", managementAsset);
        model.addAttribute("id",id);
        if (null != managementAsset && null != managementAsset.getAssets_id()) {
            AssetsManage assetsManage = assetsManageService.getById(managementAsset.getAssets_id());
            assetsManage.setAsset_status_name(AssetsStatusEnums.getValue(assetsManage.getAsset_state()));
            assetsManage.setScrap_time_str(assetsManage.getScrap_time() == null?"":DateUtils.formatDate(assetsManage.getScrap_time(), "yyyy-MM-dd HH:mm:ss"));
            assetsManage.setGuarantee_end_time_str(assetsManage.getGuarantee_end_time() == null?"":DateUtils.formatDate(assetsManage.getGuarantee_end_time(), "yyyy-MM-dd HH:mm:ss"));
            model.addAttribute("assetsManage", assetsManage);
        }
        List<Map<String, Object>> map = new ArrayList<>() ;
        for (ManagementStatusEnums enums : ManagementStatusEnums.values()){
            Map<String, Object> data = new HashMap<>();
            data.put("id", enums.getType());
            data.put("name", enums.getDesc());
            map.add(data);
        }
        model.addAttribute("statusTypes", map);

        model.addAttribute("files", null);
        List<ManagementAssetsFileMember> fileMembers = managementAssetsFileMemberMapper.selectList(Wrappers.<ManagementAssetsFileMember>lambdaQuery().eq(ManagementAssetsFileMember::getManagementasset_id,id));
        if(!CollectionUtils.isEmpty(fileMembers)) {
            List<FileManage> files =  fileManageService.selectList(Wrappers.<FileManage>lambdaQuery().eq(FileManage::getIs_del,"0")
                    .in(FileManage::getId,fileMembers.stream().map(ManagementAssetsFileMember::getFile_id).collect(Collectors.toList())));
            model.addAttribute("files", files);
        }

        return "change/dealWith/assetInfo";
    }


    /**
     * 获取所有符合条件的资产
     * @return
     */
    @RequestMapping(value = "getLeaveUnused.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getLeaveUnused( @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit,AssetsManage assetsManage, @RequestParam(required = false)String arr ){
        List<ReceiveAssets> list = new ArrayList<>();
        if (!StringUtils.isEmpty(arr)){
            JSONArray array = JSON.parseArray(arr);
            list = array.toJavaList(ReceiveAssets.class);
        }

        System.out.println(list);
        return managementService.getLeaveUnused(page, limit,assetsManage,list);
    }



    /**
     * 处置 展示当前登陆用户的所有处置单
     * @param management
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPageByUserId.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getPageByUserId(Management management, @RequestParam(defaultValue = "1") Integer page,  @RequestParam(defaultValue = "10") Integer limit){
        int userId = sysUserService.getUser().getId();
        management.setCreate_user(userId);
        PageInfo<Management> pageInfo = managementService.getPageByUserId(management, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 新增处置单
     * @param
     * @return
     */
    @RequestMapping(value = "addManagement.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addManagement (@NonNull String management_data, String management){
        JSONArray array = JSON.parseArray(management_data);
        List<ManagementAssets> list = array.toJavaList(ManagementAssets.class);
        Management management1 =JSON.parseObject(management,Management.class);
        management1.setDetail(list);
        return managementService.addManagement(management1);
    }


    /**
     * 新增处置单并送审
     * @param
     * @return
     */
    @RequestMapping(value = "addManagementAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addManagementAndSub (@NonNull String management_data, String management){
        JSONArray array = JSON.parseArray(management_data);
        List<ManagementAssets> list = array.toJavaList(ManagementAssets.class);
        Management management1 =JSON.parseObject(management,Management.class);
        management1.setDetail(list);
        return managementService.addManagementAndSub(management1);
    }

    /**
     * 修改处置单
     * @param management
     * @return
     */
    @RequestMapping(value = "updateManagement.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateManagement(@NonNull String management_data, String management){
        JSONArray array = JSON.parseArray(management_data);
        List<ManagementAssets> list = array.toJavaList(ManagementAssets.class);
        Management management1 =JSON.parseObject(management,Management.class);
        management1.setDetail(list);

        return managementService.updateManagement(management1);
    }

    /**
     * 修改处置单并送审
     * @param management
     * @return
     */
    @RequestMapping(value = "updateManagementAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateManagementAndSub(@NonNull String management_data, String management){
        JSONArray array = JSON.parseArray(management_data);
        List<ManagementAssets> list = array.toJavaList(ManagementAssets.class);
        Management management1 =JSON.parseObject(management,Management.class);
        management1.setDetail(list);

        return managementService.updateManagementAndSub(management1);
    }

    /**
     * 删除处置单
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteManagement.do")
    @ResponseBody
    public ResultInfo deleteManagement(Integer id){
        return managementService.deleteManagement(id);
    }

    /**
     * 处置单送审
     * @param id
     * @return
     */
    @RequestMapping(value = "submitManage.do")
    @ResponseBody
    public ResultInfo submitManage(Integer id){
        return managementService.submitManage(id);
    }

    /**
     * 打印单据
     * @param id
     * @return
     */
    @RequestMapping(value = "printManage.do")
    @ResponseBody
    public ResultInfo printManage(Integer id){
        return managementService.printManage(id);
    }


    /**
     * 撤回
     * @param id
     * @return
     */
    @RequestMapping(value = "backManage.do")
    @ResponseBody
    public ResultInfo backManage(Integer id){
        return managementService.backManage(id);
    }


    /**
     * 跳转资产选择页面
     *
     * @return
     */
    @RequestMapping(value = "assetSelect.do")
    public String toAssetSelect(Model model) {
        return "/change/dealWithManage/assets-select";
    }

    /**
     * 跳转待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model,Integer status) {
        model.addAttribute("status",status);
        // 部门
        List<SysDepartment> departments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery()
                .eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("departments", departments);
        return "shenpimana/chuzhisp/index";
    }



    /**
     * 跳转打印详情页
     *
     * @return
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        Management management = managementService.getOneInfoById(id);
        List<ManagementAssets> managementAssetsList = managementService.getDetailInfo(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        model.addAttribute("management", management);
        model.addAttribute("managementAssetsList", managementAssetsList);
        model.addAttribute("time", time);
        return "change/dealWith/print";
    }



    /**
     * 待审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Management bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
//        int userId = sysUser.getId();
//        Integer postId = sysUserService.selectById(userId).getPost();
//        if (null == postId) {
//            return ResultInfo.error("没有权限查看");
//        }
//        SysPost sysPost = sysPostService.selectById(postId);
//        if (null == sysPost) {
//            return ResultInfo.error("没有权限查看");
//        }
//        Integer data_permission = sysPost.getData_permission();
//        if (G.DATAPERMISSION_PERSONAL == data_permission) {//仅查看个人通过审核数据
//            bean.setApply_user(userId);
//        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
//            int department = sysUser.getDepartment();
//            bean.setDepartment_id(department);
//        }
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCCZ);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<Management> pageInfo = managementService.getPageByUserId(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }
    /**
     * 已审批数据
     */
    @RequestMapping(value = "finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Management bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_ZCCZ);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<Management> pageInfo = managementService.finishAdopt(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }



    /**
     * 分页展示处置管理列表页
     * @param management
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getManageAssetsByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getManageAssetsByPage(Management management, @RequestParam(defaultValue = "1") int page,  @RequestParam(defaultValue = "10") int limit){
        return managementService.getManageAssetsByPage(management, page, limit);
    }


    /**
     * 处置操作
     * @param managementAssets
     * @return
     */
    @RequestMapping(value = "dealWithAssets.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo dealWithAssets(ManagementAssets managementAssets){
        return managementService.dealWithAssets(managementAssets);
    }

    /**
     * 分页展示处置列表页
     * @param id
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "setManageAssetsByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo setManageAssetsByPage(Integer id, @RequestParam(defaultValue = "1") int page,  @RequestParam(defaultValue = "10") int limit){
        return managementService.setManageAssetsByPage(id, page, limit);
    }


    //查询单条记录日志信息
    @RequestMapping(value = "getOperationRecordList.do")
    @ResponseBody
    public  ResultInfo getOperationRecordList (Integer id){
        List<OperationRecord> list = managementService.getOperationRecordList(id);
        return ResultInfo.success(list);
    }
}
