package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.AssetsAccessRecord;
import com.jsdc.rfid.service.AssetsAccessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * 资产进出记录
 */
@Controller
@RequestMapping("/assetsAccessRecord")
public class AssetsAccessRecordController extends BaseController {

    @Autowired
    private AssetsAccessRecordService assetsAccessRecordService;

    /**
     * 资产进出记录
     */
    @RequestMapping(value = "pageList.do")
    public String assetAccessRecord(Model model) {
        return "baobiao/zichanjc/index";
    }

    //分页数据
    @RequestMapping(value = "pageList.json")
    @ResponseBody
    public ResultInfo pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, AssetsAccessRecord bean) {
        PageInfo<AssetsAccessRecord> pageInfo = assetsAccessRecordService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

}
