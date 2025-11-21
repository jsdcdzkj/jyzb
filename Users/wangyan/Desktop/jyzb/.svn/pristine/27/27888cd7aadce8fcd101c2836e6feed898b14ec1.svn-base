package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.PurchaseDetail;
import com.jsdc.rfid.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * ClassName: PurchaseDetailController
 * Description:  采购明细
 * date: 2022/4/24 16:56
 *
 * @author bn
 */
@Controller
@RequestMapping("purchaseDetail")
public class PurchaseDetailController extends BaseController {

    @Autowired
    private PurchaseDetailService purchaseDetailService;


    /***
     *  根据条件获取编号全部数据
     * @param purchaseDetail
     * @return
     */
    @RequestMapping(value = "toAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAll(PurchaseDetail purchaseDetail){

        return ResultInfo.success(purchaseDetailService.toAll(purchaseDetail));
    }

    /**
     * 根据资产品类获得该品类下资产民称
     * @param purchaseDetail
     * @return
     */
    @RequestMapping(value = "toAssetsName.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetsName(PurchaseDetail purchaseDetail){

        return ResultInfo.success(purchaseDetailService.toAssetsName(purchaseDetail));
    }
}
