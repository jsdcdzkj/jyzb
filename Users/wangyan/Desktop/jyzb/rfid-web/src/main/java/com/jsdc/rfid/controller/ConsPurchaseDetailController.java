package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.ConsPurchaseApply;
import com.jsdc.rfid.model.ConsPurchaseDetail;
import com.jsdc.rfid.model.OperationRecord;
import com.jsdc.rfid.model.PurchaseDetail;
import com.jsdc.rfid.service.ConsPurchaseDetailService;
import com.jsdc.rfid.service.OperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ConsPurchaseApplyVo;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ClassName: PurchaseDetailController
 * Description:  采购明细
 * date: 2022/4/24 16:56
 *
 * @author bn
 */
@Controller
@RequestMapping("consPurchaseDetail")
public class ConsPurchaseDetailController extends BaseController {

    @Autowired
    private ConsPurchaseDetailService purchaseDetailService;


    /***
     *  根据条件获取编号全部数据
     * @param purchaseDetail
     * @return
     */
    @RequestMapping(value = "toAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAll(ConsPurchaseDetail purchaseDetail){

        return ResultInfo.success(purchaseDetailService.toAll(purchaseDetail));
    }

    /**
     * 根据资产品类获得该品类下资产民称
     * @param purchaseDetail
     * @return
     */
    @RequestMapping(value = "toAssetsName.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetsName(ConsPurchaseDetail purchaseDetail){

        return ResultInfo.success(purchaseDetailService.toAssetsName(purchaseDetail));
    }

}
