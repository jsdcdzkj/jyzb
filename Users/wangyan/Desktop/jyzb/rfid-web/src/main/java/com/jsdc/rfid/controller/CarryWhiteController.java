package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.AssetsType;
import com.jsdc.rfid.model.CarryWhite;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.AssetsManageService;
import com.jsdc.rfid.service.AssetsTypeService;
import com.jsdc.rfid.service.CarryWhiteService;
import com.jsdc.rfid.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangdequan
 * @create 2024-02-28 10:42:26
 */
@Controller
@RequestMapping("/carryWhite")
public class CarryWhiteController extends BaseController {

    @Autowired
    private CarryWhiteService carryWhiteService;

    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private AssetsManageService assetsManageService;

    @Autowired
    private SysUserService sysUserService;

    // 跳转到外携白名单页面
    @RequestMapping(value = "whileIndex.do")
    public String whileIndex(Model model) {
        return "carrymanage/while/page";
    }

    // 跳转到外携白名单添加页面
    @RequestMapping("toSelect.do")
    public String toSelect(Model model) {
        //获取所有资产类型
        List<AssetsType> assetsTypes = assetsTypeService.selectList(Wrappers.<AssetsType>lambdaQuery().eq(AssetsType::getIs_del, G.ISDEL_NO));
        model.addAttribute("assetsTypes", assetsTypes);
        //得到人员信息
        List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getIs_del, G.ISDEL_NO));
        model.addAttribute("sysUsers", sysUsers);
        return "carrymanage/while/select";
    }

    /**
     * 白名单列表
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, CarryWhite beanParam) {
        PageInfo<CarryWhite> page = carryWhiteService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     * 得到资产列表
     */
    @RequestMapping(value = "assets/toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetsList(@RequestParam(defaultValue = "1", name = "page") Integer pageIndex,
                                   @RequestParam(defaultValue = "10", name = "limit") Integer pageSize,
                                   AssetsManage beanParam) {
        // 查询所有白名单id
        List<CarryWhite> ids = carryWhiteService.selectList(Wrappers.<CarryWhite>query()
                .select("asset_id")
                .eq("white_flag", G.ISDEL_YES)
                .eq("is_del", G.ISDEL_NO)
        );

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<AssetsManage> page = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery().eq(AssetsManage::getIs_del, G.ISDEL_NO)
                .notIn(!CollectionUtils.isEmpty(ids), AssetsManage::getId, ids.stream().map(CarryWhite::getAsset_id).collect(Collectors.toList()))
                .like(null != beanParam.getAsset_code(), AssetsManage::getAsset_code, beanParam.getAsset_code())
                .like(null != beanParam.getAsset_name(), AssetsManage::getAsset_name, beanParam.getAsset_name())
                .like(null != beanParam.getSpecification(), AssetsManage::getSpecification, beanParam.getSpecification())
                .eq(null != beanParam.getAsset_type_id(), AssetsManage::getAsset_type_id, beanParam.getAsset_type_id())
                .eq(null != beanParam.getUse_people(), AssetsManage::getUse_people, beanParam.getUse_people())
        );
        List<AssetsManage> newList = assetsManageService.afterList(page);
        return ResultInfo.success(new PageInfo<>(newList));
    }

    /**
     * 保存
     */
    @RequestMapping(value = "toSave.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toSave(@RequestBody List<Integer> assetIds) {
        try {
            if (CollectionUtils.isEmpty(assetIds)) {
                return ResultInfo.error("请选择资产");
            }
            carryWhiteService.toSave(assetIds);
        } catch (Exception e) {
            log.error("保存白名单失败", e);
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    /**
     * 更新状态
     */
    @RequestMapping(value = "toUpdate.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toUpdate(CarryWhite carryWhite) {
        try {
            if (null == carryWhite.getId()) {
                return ResultInfo.error("id不能为空");
            }
            carryWhiteService.updateById(carryWhite);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    /**
     * 更新状态
     */
    @RequestMapping(value = "toDelete.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toDelete(CarryWhite carryWhite) {
        try {
            if (null == carryWhite.getId()) {
                return ResultInfo.error("id不能为空");
            }
            carryWhite.setIs_del(G.ISDEL_YES);
            carryWhiteService.updateById(carryWhite);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

}
