package com.jsdc.rfid.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.ConsAssettype;
import com.jsdc.rfid.model.ConsCategory;
import com.jsdc.rfid.service.ConsAssettypeService;
import com.jsdc.rfid.service.ConsCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * ClassName: ConsCategoryController
 * Description:
 * date: 2022/6/9 9:48
 *
 * @author bn
 */
@Controller
@RequestMapping("consCategory")
@AllArgsConstructor
public class ConsCategoryController extends BaseController {


    private ConsCategoryService categoryService;

    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(ConsCategory consCategory){

        return ResultInfo.success(categoryService.getList(consCategory));
    }
}
