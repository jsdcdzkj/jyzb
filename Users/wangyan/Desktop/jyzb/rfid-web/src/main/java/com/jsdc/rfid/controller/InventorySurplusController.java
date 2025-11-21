package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.service.InventorySurplusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: InventorySurplusController
 * Description: 盘点盘盈
 * date: 2022/4/24 17:22
 *
 * @author bn
 */
@Controller
@RequestMapping("inventorySurplus")
public class InventorySurplusController extends BaseController {

    @Autowired
    private InventorySurplusService inventorySurplusService;

}
