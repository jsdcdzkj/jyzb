package com.jsdc.rfid.controller.app.change;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.rfid.mapper.ManagementMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppManagementController {

    @Autowired
    private ManagementMapper managementMapper;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private ManagementAssetsService managementAssetsService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AssetsTypeService assetsTypeService;

    /**
     * app处置列表
     */
    public String  getAllManagement(Integer pageSize, Integer pageIndex,Integer userId){
        Management management = new Management();
        management.setCreate_user(userId);
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageIndex, pageSize);
        List<Management> list = managementMapper.getPageByUserId(management,userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Management temp : list) {
            if (null != temp.getApply_date()){
                temp.setReal_apply_date(simpleDateFormat.format(temp.getApply_date()));
            }
            if (!StringUtils.isEmpty(temp.getStatus())) {
                if ("1".equals(temp.getStatus())){
                    temp.setStatus_name("未送审");
                }else if ("2".equals(temp.getStatus())) {
                    temp.setStatus_name("未审批");
                }else if ("3".equals(temp.getStatus())) {
                    temp.setStatus_name("审批中");
                }else if ("4".equals(temp.getStatus())) {
                    temp.setStatus_name("审批通过");
                }else {
                    temp.setStatus_name("审批驳回");
                }
            }
        }
        PageInfo<Management> page = new PageInfo<>(list);
        jsonObject.put("page", page);
        return jsonObject.toJSONString();
    }

    /**
     * 送审
     * @param id
     * @param userId
     * @return
     */
    public String submitOneManage(Integer id,Integer userId){
        JSONObject jsonObject = new JSONObject();
        managementService.appSubmitManage(id,userId);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

    public String  getOneManagement(Integer id){
        JSONObject jsonObject = new JSONObject();
        Management management = managementService.getOneInfoById(id);
        if (!StringUtils.isEmpty(management.getStatus())){
            if ("1".equals(management.getStatus())){
                management.setStatus_name("未送审");
            }else if ("2".equals(management.getStatus())) {
                management.setStatus_name("未审批");
            }else if ("3".equals(management.getStatus())) {
                management.setStatus_name("审批中");
            }else if ("4".equals(management.getStatus())) {
                management.setStatus_name("审批通过");
            }else {
                management.setStatus_name("审批驳回");
            }
        }
        QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("management_id",id).eq("is_del",0);
        List<ManagementAssets> list = managementAssetsService.selectList(queryWrapper);
        for(ManagementAssets temp: list){
            if( null != temp.getAssets_id()){
                AssetsManage assetsManage = assetsManageService.selectById(temp.getAssets_id());
                if(null != assetsManage){
                    temp.setAsset_name(assetsManage.getAsset_name());
                    temp.setAsset_code(assetsManage.getAsset_code());
                }
            }
            if(!StringUtils.isEmpty(temp.getProposal())){
                if ("2".equals(temp.getProposal())){
                    temp.setProposal_name("报废");
                }else if ("3".equals(temp.getProposal())) {
                    temp.setProposal_name("遗失");
                }else if ("4".equals(temp.getProposal())) {
                    temp.setProposal_name("无偿调拨");
                }else if ("5".equals(temp.getProposal())) {
                    temp.setProposal_name("对外捐赠");
                }else if ("6".equals(temp.getProposal())) {
                    temp.setProposal_name("出售");
                }else if ("7".equals(temp.getProposal())) {
                    temp.setProposal_name("出让");
                }else if ("8".equals(temp.getProposal())) {
                    temp.setProposal_name("转让");
                }else if ("9".equals(temp.getProposal())) {
                    temp.setProposal_name("置换");
                }else {
                    temp.setProposal_name("暂无数据");
                }
            }else {
                temp.setProposal_name("暂无数据");
            }
            if (!StringUtils.isEmpty(temp.getReason())){
                if ("2".equals(temp.getReason())){
                    temp.setReason_name("报废");
                }else if ("3".equals(temp.getReason())) {
                    temp.setReason_name("遗失");
                }else if ("4".equals(temp.getReason())) {
                    temp.setReason_name("无偿调拨");
                }else if ("5".equals(temp.getReason())) {
                    temp.setReason_name("对外捐赠");
                }else if ("6".equals(temp.getReason())) {
                    temp.setReason_name("出售");
                }else if ("7".equals(temp.getReason())) {
                    temp.setReason_name("出让");
                }else if ("8".equals(temp.getReason())) {
                    temp.setReason_name("转让");
                }else if ("9".equals(temp.getReason())) {
                    temp.setReason_name("置换");
                }else {
                    temp.setReason_name("暂无数据");
                }
            }else {
                temp.setReason_name("暂未处置");
            }
            if (!StringUtils.isEmpty(temp.getStatus())) {
                if ("1".equals(temp.getStatus())){
                    temp.setStatusName("已处置");
                }else {
                    temp.setStatusName("未处置");
                }
            }
            if (null != temp.getManagement_user()) {
                SysUser sysUser = sysUserService.selectById(temp.getManagement_user());
                if (null != sysUser){
                    temp.setManagement_name(sysUser.getUser_name());
                }
            }else {
                temp.setManagement_name("暂无数据");
            }
        }
        jsonObject.put("list", list);
        jsonObject.put("management", management);

        String jjyy = "";
        OperationRecord operationRecord = managementService.getOperationRecordData(id);
        if (Base.notEmpty(operationRecord)) {
            if (Base.notEmpty(operationRecord.getRecord())) {
                //驳回原因
                if (operationRecord.getRecord().contains("：")) {
                    jjyy = operationRecord.getRecord().substring(operationRecord.getRecord().indexOf("：") + 1);
                }
            }
        }
        jsonObject.put("jjyy", jjyy);
        return jsonObject.toJSONString();
    }



    public String getAllAssetsList(Integer pageIndex, Integer pageSize, String ids){
        return getAllAssetsList(pageIndex, pageSize,ids,null);
    }

    public String getAllAssetsList(Integer pageIndex, Integer pageSize){
        return getAllAssetsList(pageIndex, pageSize,null,null);
    }

    public String getAllAssetsListByCode(Integer pageIndex, Integer pageSize, String asset_code){
        return getAllAssetsList(pageIndex, pageSize,null,asset_code);
    }

    /**
     * 获取所有可以处置的资产
     * @param ids
     * @return
     */
    public String getAllAssetsList(Integer pageIndex, Integer pageSize,String ids,String asset_code){
        JSONObject jsonObject = new JSONObject();
        String [] idList = null;
        if (ids != null && !ids.equals("")){
            idList =ids.split("-");
        }
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("asset_state","6");
        queryWrapper.eq("is_del","0");

        if (!StringUtils.isEmpty(asset_code)) {
            queryWrapper.like("asset_code",asset_code);
        }

        //排除掉已经选择的耗材
        if (null != idList) {
            List<Integer> idsList = new ArrayList<>();
            for (String id : idList) {
                idsList.add(Integer.parseInt(id));
            }
            queryWrapper.notIn("id",idsList);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<AssetsManage> list = assetsManageService.selectList(queryWrapper);

        for (AssetsManage temp : list){
            if (null != temp.getAsset_type_id()){
                AssetsType assetsType = assetsTypeService.selectById(temp.getAsset_type_id());
                if (null != assetsType){
                    temp.setAsset_type_name(assetsType.getAssets_type_name());
                }
            }
            if (null != temp.getAdmin_user()){
                SysUser sysUser = sysUserService.selectById(temp.getAdmin_user());
                if (null != sysUser){
                    temp.setAdmin_user_name(sysUser.getUser_name());
                }
            }
        }
        PageInfo<AssetsManage> page = new PageInfo<>(list);
        jsonObject.put("result", page);
        return jsonObject.toJSONString();
    }



    /**
     * 新增处置单
     * @param ids
     * @param remark
     * @param userId
     * @param proposals
     * @return
     */
    public String addManage(String ids, String remark,Integer userId,String proposals) {
        JSONObject jsonObject = new JSONObject();
        managementService.addAPPManage(ids, remark,userId,proposals,1);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }
    /**
     * 新增处置单并送审
     * @param ids
     * @param remark
     * @param userId
     * @param proposals
     * @return
     */
    public String addManageSub(String ids, String remark,Integer userId,String proposals) {
        JSONObject jsonObject = new JSONObject();
        managementService.addAPPManage(ids, remark,userId,proposals,2);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }
}
