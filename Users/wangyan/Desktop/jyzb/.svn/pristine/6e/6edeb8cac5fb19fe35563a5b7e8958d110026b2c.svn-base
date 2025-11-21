package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.InventoryDetail;
import com.jsdc.rfid.service.InventoryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;
import java.util.List;

/**
 * ClassName: InventoryDetailController
 * Description: 盘点详情
 * date: 2022/4/24 17:20
 *
 * @author bn
 */
@Controller
@RequestMapping("inventoryDetail")
public class InventoryDetailController extends BaseController {

    @Autowired
    private InventoryDetailService inventoryDetailService;


    /**
     *  手动盘点
     * @return
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(InventoryDetail inventoryDetail){
        inventoryDetailService.edit(inventoryDetail);
        return ResultInfo.success();
    }

    /**
     *  批量盘点
     * @return
     */
    @RequestMapping(value = "toBatchEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toBatchEdit(@RequestBody List<InventoryDetail> inventoryDetail){
        inventoryDetailService.toBatchEdit(inventoryDetail);
        return ResultInfo.success();
    }
}
