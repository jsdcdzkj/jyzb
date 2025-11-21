package com.jsdc.rfid.controller.app.change;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppBorrowController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private AssetsTypeService assetsTypeService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ConsReceiveService consReceiveService;
    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;
    @Autowired
    private ConsCategoryService consCategoryService;
    @Autowired
    private ConsAssettypeService consAssettypeService;
    @Autowired
    private ConsSpecificationService consSpecificationService;


    public String  getAllConsReceive(Integer pageSize, Integer pageIndex,Integer userId){
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageIndex, pageSize);
        List<ConsReceive> list = consReceiveService.getAllConsReceiveByUserId(userId, StringUtils.EMPTY);
        PageInfo<ConsReceive> page = new PageInfo<>(list);
        System.out.println(page);
        jsonObject.put("page", page);
        return jsonObject.toJSONString();
    }

    public String  getOneConsReceiveInfo(Integer id){
        JSONObject jsonObject = new JSONObject();
        ConsReceive consReceive = consReceiveService.getOneInfo(id);
        List<ConsReceiveAssets> list = consReceiveService.getOneInfoById(id);
        jsonObject.put("list", list);
        jsonObject.put("borrow", consReceive);

        String jjyy = "";
        OperationRecord operationRecord = consReceiveService.getOperationRecordData(id);
        if (Base.notEmpty(operationRecord)) {
            if (Base.notEmpty(operationRecord.getRecord())) {
                //驳回原因
                if (operationRecord.getRecord().contains("，")) {
                    jjyy = operationRecord.getRecord().substring(operationRecord.getRecord().indexOf("，") + 1);
                }
            }
        }
        jsonObject.put("jjyy", jjyy);

        return jsonObject.toJSONString();
    }



    public String getAllConsManList(Integer pageIndex, Integer pageSize, String ids){
        return getAllConsManList(pageIndex, pageSize,ids,null);
    }

    public String getAllConsManList(Integer pageIndex, Integer pageSize){
        return getAllConsManList(pageIndex, pageSize,null,null);
    }

    public String getAllConsManListByCode(Integer pageIndex, Integer pageSize, String asset_code){
        return getAllConsManList(pageIndex, pageSize,null,asset_code);
    }


    public String getAllConsManList(Integer pageIndex, Integer pageSize,String ids,String asset_code){
        JSONObject jsonObject = new JSONObject();
        String [] idList = null;
        if (ids != null && !ids.equals("")){
            idList =ids.split("-");
        }
        QueryWrapper<ConsInventoryManagement> queryWrapper = new QueryWrapper<>();
        //如果搜索条件不为空，根据耗材名字搜索
        if (StringUtils.isNotBlank(asset_code)){
            QueryWrapper<ConsAssettype> consAssettypeQueryWrapper = new QueryWrapper<>();
            consAssettypeQueryWrapper.like("name",asset_code);
            consAssettypeQueryWrapper.eq("is_del","0");
            consAssettypeQueryWrapper.orderByDesc("create_time");
            List<ConsAssettype> consAssettypeTemp =  consAssettypeService.selectList(consAssettypeQueryWrapper);
            if (!CollectionUtils.isEmpty(consAssettypeTemp)){
                List<Integer> assetNameIdList = new ArrayList<>();
                for (int i = 0; i < consAssettypeTemp.size(); i++) {
                    assetNameIdList.add(consAssettypeTemp.get(i).getId());
                }
                queryWrapper.in("asset_name_id",assetNameIdList);
            }else {
                queryWrapper.eq("asset_name_id",-100);
            }
        }

        queryWrapper.eq("is_del","0");
        queryWrapper.gt("inventory_num",0);

        //排除掉已经选择的耗材
        if (null != idList) {
            List<Integer> idsList = new ArrayList<>();
            for (String id : idList) {
                idsList.add(Integer.parseInt(id));
            }
            queryWrapper.notIn("id",idsList);
        }
        queryWrapper.orderByDesc("create_time");

        PageHelper.startPage(pageIndex, pageSize);

        List<ConsInventoryManagement> list = consInventoryManagementService.selectList(queryWrapper);
        //获取各个Id的名字
        for (ConsInventoryManagement temp : list){
            if (null != temp.getAsset_type_id()){
                ConsCategory consCategory = consCategoryService.selectById(temp.getAsset_type_id());
                if (null != consCategory){
                    temp.setAssetTypeName(consCategory.getName());
                }
            }
            if (null != temp.getAsset_name_id()){
                ConsAssettype consAssettype = consAssettypeService.selectById(temp.getAsset_name_id());
                if (null != consAssettype){
                    temp.setAsset_name(consAssettype.getName());
                }
            }
            if (null  != temp.getSpecifications()){
                ConsSpecification consSpecification = consSpecificationService.selectById(temp.getSpecifications());
                if (null != consSpecification){
                    temp.setSpecifications_name(consSpecification.getTypename());
                }
            }
        }

        PageInfo<ConsInventoryManagement> page = new PageInfo<>(list);
        jsonObject.put("result", page);
        return jsonObject.toJSONString();
    }




    public String addBorrow( String assetNameIds, String assetTypeIds,String specificationss ,String remark,Integer userId,String nums,String ids){

        JSONObject jsonObject = new JSONObject();
        consReceiveService.appAddConsReceive(assetNameIds, assetTypeIds, specificationss, remark, userId, nums,1,ids);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

    public String addConsReceiveAndSub( String assetNameIds, String assetTypeIds,String specificationss ,String remark,Integer userId,String nums,String ids){
        JSONObject jsonObject = new JSONObject();
        consReceiveService.appAddConsReceive(assetNameIds, assetTypeIds, specificationss, remark, userId, nums,2,ids);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }


    public String submitOneBorrow(Integer id,Integer userId){
        JSONObject jsonObject = new JSONObject();
        consReceiveService.subConReceiveApp(id,userId);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

    public String sureConsReceive(Integer id,Integer userId){
        JSONObject jsonObject = new JSONObject();
        consReceiveService.sureConsReceive(id,userId);
        jsonObject.put("success", "成功");
        return jsonObject.toJSONString();
    }

}
