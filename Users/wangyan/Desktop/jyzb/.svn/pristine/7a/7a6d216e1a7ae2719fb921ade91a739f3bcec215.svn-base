package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.AssetsType;
import com.jsdc.rfid.model.ConsAssettype;
import com.jsdc.rfid.service.ConsAssettypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * ClassName: ConsAssetTypeController
 * Description:
 * date: 2022/6/8 16:34
 *
 * @author bn
 */
@Controller
@RequestMapping("consAssetType")
@AllArgsConstructor
public class ConsAssetTypeController extends BaseController {

    private ConsAssettypeService assettypeService;

    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(ConsAssettype assetsType){

        return ResultInfo.success(assettypeService.getList(assetsType));
    }

}
