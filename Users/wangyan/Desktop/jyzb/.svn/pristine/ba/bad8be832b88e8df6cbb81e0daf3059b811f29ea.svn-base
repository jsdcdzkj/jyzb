package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.PurchaseLog;
import com.jsdc.rfid.service.PurchaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * ClassName: PurchaseLogController
 * Description: 采购日志
 * date: 2022/4/24 16:57
 *
 * @author bn
 */
@Controller
@RequestMapping("purchaseLog")
public class PurchaseLogController extends BaseController {

    @Autowired
    private PurchaseLogService purchaseLogService;

    @RequestMapping(value = "toAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAll(PurchaseLog purchaseLog){
        return ResultInfo.success(purchaseLogService.toAll(purchaseLog));
    }

}
