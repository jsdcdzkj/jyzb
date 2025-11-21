package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.ConsWarehousingManagement;
import com.jsdc.rfid.service.ConsWarehousingManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Controller
@RequestMapping("/consWarehousingManagement")
public class ConsWarehousingManagementController extends BaseController {

    @Autowired
    private ConsWarehousingManagementService consWarehousingManagementService;


    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, ConsWarehousingManagement beanParam) {
        PageInfo<ConsWarehousingManagement> page = consWarehousingManagementService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }
     /**
     *  ID查询
     */
    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id){
        return consWarehousingManagementService.getById(id);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addConsWarehousingManagement(ConsWarehousingManagement consWarehousingManagement){

        return consWarehousingManagementService.addConsWarehousingManagement(consWarehousingManagement);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editConsWarehousingManagement(ConsWarehousingManagement consWarehousingManagement){

        return consWarehousingManagementService.editConsWarehousingManagement(consWarehousingManagement);
    }
}
