package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.AssetsTrajectory;
import com.jsdc.rfid.service.AssetsTrajectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.List;

/**
 * 资产轨迹查询
 */
@Controller
@RequestMapping("/assetsTrajectory")
public class AssetsTrajectoryController extends BaseController {

    @Autowired
    private AssetsTrajectoryService assetsTrajectoryService;

    /**
     * 资产轨迹查询
     */
    @RequestMapping(value = "pageList.do")
    public String assetTrajectory(Model model) {
        return "baobiao/zichangj/index";
    }


    //分页数据
    @RequestMapping(value = "pageList.json")
    @ResponseBody
    public ResultInfo pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, AssetsTrajectory bean) {
        PageInfo<AssetsTrajectory> pageInfo = assetsTrajectoryService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    //列表数据
    @RequestMapping(value = "getList.do")
    @ResponseBody
    public ResultInfo getList(AssetsTrajectory bean) {
        List<AssetsTrajectory> list = assetsTrajectoryService.toList(bean);
        return ResultInfo.success(list);
    }

    /**
     * 详情页面
     */
    @RequestMapping(value = "selectByDetails.do")
    public String selectByDetails(Integer id, Model model) {
        //资产id
        model.addAttribute("id", id);
        return "baobiao/zichangj/view-log";
    }

}
