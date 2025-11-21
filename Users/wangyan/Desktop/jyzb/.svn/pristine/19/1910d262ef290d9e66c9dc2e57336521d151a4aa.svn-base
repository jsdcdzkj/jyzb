package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/changeInfo")
public class ChangeInfoController {

    @Autowired
    private ChangeInfoService changeInfoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private SysPositionService sysPositionService;
    @Autowired
    private SysPlaceService sysPlaceService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private ProcessMemberHistoryService historyService;

    @Autowired
    private SysDictService sysDictService;


    /**
     * 跳转资产展示页面
     *
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
        return "change/modify/select";
    }


    /**
     * 跳转变更列表页
     *
     * @return
     */
    @RequestMapping("toChangeIndex.do")
    public String toIndex() {
        return "change/modify/index";
    }

    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("view-log")
    public String toViewLog() {
        return "change/modify/view-log";
    }


    /**
     * 跳转变更新增页面
     *
     * @return
     */
    @RequestMapping("toAddChange.do")
    public String toAddChange(Model model) {
        SysUser sysUser = sysUserService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);


        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");

        List<SysUser> sysUsers = sysUserService.selectList(userqueryWrapper);

        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.in("asset_state",0,1);
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);

        QueryWrapper<SysDepartment> sysDepartmentQueryWrapper = new QueryWrapper<>();
        sysDepartmentQueryWrapper.eq("is_del", "0");
        List<SysDepartment> sysDepartment = sysDepartmentService.selectList(sysDepartmentQueryWrapper);

        QueryWrapper<SysPosition> sysPositionQueryWrapper = new QueryWrapper<>();
        sysPositionQueryWrapper.eq("is_del", "0");
        List<SysPosition> sysPosition = sysPositionService.selectList(sysPositionQueryWrapper);
//        for (SysPosition temp : sysPosition) {
//            if (null != temp.getPlace_id()) {
//                temp.setPlace_name(sysPlaceService.selectById(temp.getPlace_id()).getPlace_name());
//            }
//        }

        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("sysUsers", sysUsers);
        model.addAttribute("sysDepartment", sysDepartment);
        model.addAttribute("sysPosition", sysPosition);
        return "change/modify/add";
    }


    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model) {
        ChangeInfo changeInfo = changeInfoService.getOneInfoById(id);
        List<ChangeDetail> changeDetail = changeInfoService.getInfoByChangeId(id);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.in("asset_state",0,1);
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);
        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUser = sysUserService.selectList(userqueryWrapper);


        QueryWrapper<SysDepartment> sysDepartmentQueryWrapper = new QueryWrapper<>();
        sysDepartmentQueryWrapper.eq("is_del", "0");
        List<SysDepartment> sysDepartment = sysDepartmentService.selectList(sysDepartmentQueryWrapper);

        QueryWrapper<SysPosition> sysPositionQueryWrapper = new QueryWrapper<>();
        sysPositionQueryWrapper.eq("is_del", "0");
        List<SysPosition> sysPosition = sysPositionService.selectList(sysPositionQueryWrapper);
        for (SysPosition temp : sysPosition) {
            if (null != temp.getPlace_id()) {
                temp.setPlace_name(sysPlaceService.selectById(temp.getPlace_id()).getPlace_name());
            }
        }
        model.addAttribute("changeInfo", changeInfo);
        model.addAttribute("changeDetail", changeDetail);
        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("sysDepartment", sysDepartment);
        model.addAttribute("sysPosition", sysPosition);
        return "change/modify/edit";
    }


    /**
     * 跳转变更管理列表页
     *
     * @return
     */
    @RequestMapping("toChangeManageIndex.do")
    public String toChangeManageIndex() {
        return "change/modifyManage/index";
    }


    /**
     * 跳转变更新增页面
     *
     * @return
     */
    @RequestMapping("toAddChangeByHand.do")
    public String toAddChangeByHand(Model model) {
        SysUser sysUser = sysUserService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);


        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUsers = sysUserService.selectList(userqueryWrapper);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.in("asset_state",0,1);
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);

        QueryWrapper<SysDepartment> sysDepartmentQueryWrapper = new QueryWrapper<>();
        sysDepartmentQueryWrapper.eq("is_del", "0");
        List<SysDepartment> sysDepartment = sysDepartmentService.selectList(sysDepartmentQueryWrapper);

        QueryWrapper<SysPosition> sysPositionQueryWrapper = new QueryWrapper<>();
        sysPositionQueryWrapper.eq("is_del", "0");
        List<SysPosition> sysPosition = sysPositionService.selectList(sysPositionQueryWrapper);
        for (SysPosition temp : sysPosition) {
            if (null != temp.getPlace_id()) {
                temp.setPlace_name(sysPlaceService.selectById(temp.getPlace_id()).getPlace_name());
            }
        }

        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("sysUsers", sysUsers);
        model.addAttribute("sysDepartment", sysDepartment);
        model.addAttribute("sysPosition", sysPosition);
        return "change/modifyManage/add";
    }


    /**
     * 跳转展示详情页
     *
     * @return
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type, Model model) {
        List<ChangeDetail> changeDetails = changeInfoService.getInfoByChangeId(id);
        model.addAttribute("id", id);
        model.addAttribute("changeDetails", changeDetails);
        model.addAttribute("type", type);
        ChangeInfo changeInfo = changeInfoService.getOneInfoById(id);
        processMemberService.getProcessDataByBusId(changeInfo.getId(), G.PROCESS_ZCBG,sysUserService.getUser(),changeInfo);
        model.addAttribute("changeInfo", changeInfo);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCBG);
        model.addAttribute("key", dictionary.getValue());
        return "change/modify/view";
    }


    /**
     * 获取所有符合条件的资产
     * @return
     */
    @RequestMapping(value = "getLeaveUnused.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getLeaveUnused( @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit,
                                      AssetsManage assetsManage,
                                      @RequestParam(required = false)String arr ){
        List<ReceiveAssets> list = new ArrayList<>();
        if (!StringUtils.isEmpty(arr)){
            JSONArray array = JSON.parseArray(arr);
            list = array.toJavaList(ReceiveAssets.class);
        }

        System.out.println(list);
        // 增加权限控制
        // 得到当前用户
        SysUser sysUser = sysUserService.getUser();
        if (null == sysUser || null == sysUser.getPost()){
            return ResultInfo.error("当前用户无权限");
        }
        SysPost post = sysPostService.selectById(sysUser.getPost());
        if (null == post){
            return ResultInfo.error("当前用户无权限");
        }
        // 0,个人 1,本部门, 2,全部
        Integer data_permission = post.getData_permission();
        switch (data_permission){
            case 0:
                assetsManage.setUse_people(sysUser.getId());
                break;
            case 1:
                assetsManage.setDept_id(sysUser.getDepartment());
                break;
            case 2:
                break;
            default:
                return ResultInfo.error("当前用户无权限");
        }

        return changeInfoService.getLeaveUnused(page, limit,assetsManage, list);
    }



    /**
     * 描述：变更申请 分页展示当前登陆用户的变更单
     * 作者：xuaolong
     *
     * @param changeInfo
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getChangeListByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getChangeListByPage(ChangeInfo changeInfo, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        int userId = sysUserService.getUser().getId();
        changeInfo.setCreate_user(userId);
        PageInfo<ChangeInfo> pageInfo = changeInfoService.getChangeListByPage(changeInfo, page, limit);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 新增变更单(变更申请)
     *
     * @param changeInfo_data
     * @param changeInfo
     * @return
     */
    @RequestMapping(value = "addChangeInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addChangeInfo(@NonNull String changeInfo_data, String changeInfo) {
        JSONArray array = JSON.parseArray(changeInfo_data);
        List<ChangeDetail> list = array.toJavaList(ChangeDetail.class);
        ChangeInfo changeInfo1 = JSON.parseObject(changeInfo, ChangeInfo.class);
        return changeInfoService.addChangeInfo(changeInfo1, list);
    }


    /**
     * 新增变更单(变更申请)并送审
     *
     * @param changeInfo_data
     * @param changeInfo
     * @return
     */
    @RequestMapping(value = "addChangeInfoAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addChangeInfoAndSub(@NonNull String changeInfo_data, String changeInfo) {
        JSONArray array = JSON.parseArray(changeInfo_data);
        List<ChangeDetail> list = array.toJavaList(ChangeDetail.class);
        ChangeInfo changeInfo1 = JSON.parseObject(changeInfo, ChangeInfo.class);
        return changeInfoService.addChangeInfoAndSub(changeInfo1, list);
    }

    /**
     * 修改变更单
     *
     * @param changeInfo
     * @return
     */
    @RequestMapping(value = "updateChangeInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateChangeInfo(@NonNull String changeInfo_data, String changeInfo) {
        JSONArray array = JSON.parseArray(changeInfo_data);
        List<Integer> list = array.toJavaList(Integer.class);
        ChangeInfo changeInfo1 = JSON.parseObject(changeInfo, ChangeInfo.class);
        return changeInfoService.updateChangeInfo(changeInfo1, list);
    }


    /**
     * 修改变更单
     *
     * @param changeInfo
     * @return
     */
    @RequestMapping(value = "updateChangeInfoAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateChangeInfoAndSub(@NonNull String changeInfo_data, String changeInfo) {
        JSONArray array = JSON.parseArray(changeInfo_data);
        List<Integer> list = array.toJavaList(Integer.class);
        ChangeInfo changeInfo1 = JSON.parseObject(changeInfo, ChangeInfo.class);
        return changeInfoService.updateChangeInfoAndSub(changeInfo1, list);
    }


    /**
     * 删除变更单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteChange.do")
    @ResponseBody
    public ResultInfo deleteChange(Integer id) {
        return changeInfoService.deleteChange(id);
    }

    /**
     * 变更单送审
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "submitChangeInfo.do")
    @ResponseBody
    public ResultInfo submitChangeInfo(Integer id) {
        return changeInfoService.submitChangeInfo(id);
    }

    /**
     * 变更单撤回
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "backChangeInfo.do")
    @ResponseBody
    public ResultInfo backChangeInfo(Integer id) {
        return changeInfoService.backChangeInfo(id);
    }

    /**
     * （变更管理）根据登陆用户权限分页展示变更信息
     *
     * @param changeInfo
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "collectionChangeByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo collectionChangeByPage(ChangeInfo changeInfo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        return changeInfoService.collectionChangeByPage(changeInfo, page, limit);
    }

    /**
     * 新增变更单(手动添加)
     *
     * @param changeInfo
     * @return
     */
    @RequestMapping(value = "addChangeInfoByManual.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addChangeInfoByManual(@NonNull String changeInfo_data, String changeInfo) {
        JSONArray array = JSON.parseArray(changeInfo_data);
        List<ChangeDetail> list = array.toJavaList(ChangeDetail.class);
        ChangeInfo changeInfo1 = JSON.parseObject(changeInfo, ChangeInfo.class);
        return changeInfoService.addChangeInfoByManual(changeInfo1, list);
    }


    /**
     * 跳转待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        return "shenpimana/biangengsp/index";
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
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ChangeInfo bean) {
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
//            bean.setUse_id(userId);
//        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
//            int department = sysUser.getDepartment();
//            bean.setDepartment_id(department);
//        }
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCBG);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<ChangeInfo> pageInfo = changeInfoService.getChangeListByPage(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 已审批数据
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ChangeInfo bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_ZCBG);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<ChangeInfo> pageInfo = changeInfoService.finishAdopt(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }


    @RequestMapping(value = "getDepart.do")
    @ResponseBody
    public ResultInfo getDepart(Integer id){

        SysUser sysUser = sysUserService.selectById(id);
        Integer department = sysUser.getDepartment();
        if (null != department){
            SysDepartment sysDepartment =  sysDepartmentService.selectById(department);
            if (null != sysDepartment){
                sysUser.setDept_name(sysDepartment.getDept_name());
                Integer dept_position = sysDepartment.getDept_position();
                if (null != dept_position){
                    SysPosition sysPosition =sysPositionService.selectById(dept_position);
                    sysUser.setPosition_name(sysPosition.getPosition_name());
                }
            }
        }
        return ResultInfo.success(sysUser);

    }

    @RequestMapping(value = "getOperationRecordList.do")
    @ResponseBody
    public  ResultInfo getOperationRecordList (Integer id){
        List<OperationRecord> list = changeInfoService.getOperationRecordList(id);
        return ResultInfo.success(list);
    }

}
