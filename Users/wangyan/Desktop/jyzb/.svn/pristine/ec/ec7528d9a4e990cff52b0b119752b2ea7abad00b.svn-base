package com.jsdc.rfid.controller.app.change;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AppReceiveController {

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private AssetsTypeService assetsTypeService;
    @Autowired
    private OperationRecordService operationRecordService;


    public String  getAllReceive(Integer pageSize, Integer pageIndex,Integer userId){
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageIndex, pageSize);
        List<Receive> list = receiveService.getAllReceive(userId);
        PageInfo<Receive> page = new PageInfo<>(list);
        jsonObject.put("page", page);
        System.out.println(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }


    public String  getOneReceiveInfo(Integer id){
        JSONObject jsonObject = new JSONObject();
        Receive receive = receiveService.getOneByReceiveId(id);
        List<ReceiveAssets> list = receiveService.getOneById(id);
        jsonObject.put("list", list);
        jsonObject.put("receive", receive);

        String jjyy = "";
        OperationRecord operationRecord = receiveService.getOperationRecordData(id);
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

    public String getAllSysInfo(Integer id){
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<SysDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<SysDepartment> list = sysDepartmentService.selectList(queryWrapper);
        SysUser sysUser = sysUserService.selectById(id);
        Integer dept = sysUser.getDepartment();
        if (null != dept){
            SysDepartment sysDepartment = sysDepartmentService.selectById(dept);
            if (null != sysDepartment){
                sysUser.setDept_name(sysDepartment.getDept_name());
            }
        }
        QueryWrapper<SysUser> queryWrapper1 =  new QueryWrapper<>();
        queryWrapper1.eq("is_del","0");
        List<SysUser> sysUserList = sysUserService.selectList(queryWrapper1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String realDate =  simpleDateFormat.format(new Date());
        jsonObject.put("list", list);
        jsonObject.put("sysUser", sysUser);
        jsonObject.put("sysUserList", sysUserList);
        jsonObject.put("realDate", realDate);
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


    public String getAllAssetsList(Integer pageIndex, Integer pageSize , String ids , String asset_code){

        JSONObject jsonObject = new JSONObject();

        String [] idList = null;
        if (ids != null && !ids.equals("")){
            idList =ids.split("-");
        }

        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("asset_state","0");
        queryWrapper.eq("is_del","0");
        if (null != idList){
            List<Integer> idsList = new ArrayList<>();
            for (int i = 0 ; i< idList.length ; i++){
                idsList.add(Integer.parseInt(idList[i]));
            }
            queryWrapper.notIn("id",idsList);
        }

        if (StringUtils.isNotBlank(asset_code)){
            queryWrapper.like("asset_code",asset_code);
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




    public String addReceive(String ids, String remark,Integer userId) {
        JSONObject jsonObject = new JSONObject();
        receiveService.addAPPReceive(ids, remark,userId,1);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

    public String addReceiveAndSub(String ids, String remark,Integer userId) {
        JSONObject jsonObject = new JSONObject();
        receiveService.addAPPReceive(ids, remark,userId,2);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

    public String submitOneReceive(Integer id,Integer userId){
        JSONObject jsonObject = new JSONObject();
        receiveService.submitAppReceive(id,userId);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

}
