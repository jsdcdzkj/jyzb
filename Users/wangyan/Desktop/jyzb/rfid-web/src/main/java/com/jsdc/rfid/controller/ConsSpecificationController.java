package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.ConsCategory;
import com.jsdc.rfid.model.ConsSpecification;
import com.jsdc.rfid.service.ConsCategoryService;
import com.jsdc.rfid.service.ConsSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * ClassName: ConsSpecificationController
 * Description:
 * date: 2022/6/9 9:51
 *
 * @author bn
 */
@Controller
@RequestMapping("consSpecification")
@AllArgsConstructor
public class ConsSpecificationController extends BaseController {

    private ConsSpecificationService consSpecificationService;

    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(ConsSpecification consSpecification){

        return ResultInfo.success(consSpecificationService.getList(consSpecification));
    }
}
