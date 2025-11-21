package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.OperationRecord;
import com.jsdc.rfid.service.OperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * 资产管理 操作日志
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Controller
@RequestMapping("/operationRecord")
public class OperationRecordController extends BaseController {

    @Autowired
    private OperationRecordService operationRecordService;


    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, OperationRecord beanParam) {
        PageInfo<OperationRecord> page = operationRecordService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }
     /**
     *  ID查询
     */
    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id){
        return operationRecordService.getById(id);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addOperationRecord(OperationRecord operationRecord){

        return operationRecordService.addOperationRecord(operationRecord);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editOperationRecord(OperationRecord operationRecord){

        return operationRecordService.editOperationRecord(operationRecord);
    }
}
