package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 易耗品 耗材 - 手动入库
 * @Author zhangdequan
 */
@Controller
@RequestMapping("/consReceive/hand")
public class ConsHandReceiveController {

    @Autowired
    private ConsHandworkService consHandworkService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private ConsCategoryService consCategoryService;

    @Autowired
    private ConsAssettypeService consAssettypeService;

    @Autowired
    private ConsSpecificationService consSpecificationService;

    /**
     * 页面跳转
     * @return
     */
    @GetMapping(value = "/receiveManage/{pageName}")
    public String toConsIndex(@PathVariable String pageName) {
        return "/haocai/receiveManage/" + pageName;
    }

    /**
     * 跳转列表页
     */
    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "/haocai/receiveManage/index" ;
    }

    /**
     * 跳转新增页面
     */
    @RequestMapping("toAddReceive.do")
    public String toAddIndex(Model model){
        SysUser sysUser = userService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);

        QueryWrapper<ConsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<ConsCategory> consCategories = consCategoryService.selectList(queryWrapper);

        QueryWrapper<ConsAssettype> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("is_del","0");
        List<ConsAssettype> consAssettypes = consAssettypeService.selectList(queryWrapper1);

        QueryWrapper<ConsSpecification> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("is_del","0");
        List<ConsSpecification> consSpecifications = consSpecificationService.selectList(queryWrapper2);


        model.addAttribute("sysUser", sysUser);
        model.addAttribute("consCategories", consCategories);
        model.addAttribute("consAssettypes", consAssettypes);
        model.addAttribute("consSpecifications", consSpecifications);
        return "haocai/receiveManage/add";

    }



    /**
     * 跳转耗材申领修改页面
     */
    @RequestMapping("toUpdateReceive.do")
    public String toUpdateReceive(Integer id,Model model) {
        SysUser sysUser = userService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);

        QueryWrapper<ConsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<ConsCategory> consCategories = consCategoryService.selectList(queryWrapper);

        QueryWrapper<ConsAssettype> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("is_del","0");
        List<ConsAssettype> consAssettypes = consAssettypeService.selectList(queryWrapper1);

        QueryWrapper<ConsSpecification> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("is_del","0");
        List<ConsSpecification> consSpecifications = consSpecificationService.selectList(queryWrapper2);

        ConsHandwork consReceive = consHandworkService.getOneInfo(id);

        List<ConsReceiveAssets> consReceiveAssets = consHandworkService.getOneInfoById(id);

        model.addAttribute("sysUser", sysUser);
        model.addAttribute("consCategories", consCategories);
        model.addAttribute("consAssettypes", consAssettypes);
        model.addAttribute("consSpecifications", consSpecifications);
        model.addAttribute("consReceive", consReceive);
        model.addAttribute("consReceiveAssets", consReceiveAssets);
        model.addAttribute("id", id);
        return "haocai/receiveManage/edit";
    }

    /**
     * 跳转展示明细页面
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type, Model model) {
        ConsHandwork consReceive = consHandworkService.getOneInfo(id);
        List<ConsReceiveAssets> receiveAssets = consHandworkService.getOneInfoById(id);
        model.addAttribute("receive", consReceive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        return "haocai/receiveManage/view";
    }


    /**
     * 跳转打印页面
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        ConsHandwork consReceive = consHandworkService.getOneInfo(id);
        List<ConsReceiveAssets> receiveAssets = consHandworkService.getOneInfoById(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());

        model.addAttribute("receive", consReceive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        model.addAttribute("time", time);
        return "haocai/receiveManage/print";
    }

    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("viewLog.do")
    public String viewLog() {
        return "haocai/receiveManage/viewLog";
    }

    /**
     * 查询单个表单日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getOperationRecordList.do")
    @ResponseBody
    public ResultInfo getOperationRecordList(Integer id){
        List<OperationRecord> list = consHandworkService.getOperationRecordList(id);
        return ResultInfo.success(list);
    }


    /**
     * 库存列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             ConsHandwork beanParam) {
        return consHandworkService.getListByPage(beanParam ,pageIndex, pageSize );
    }


    /**
     * 新增手动出库单
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "addReceive.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addReceive(@NonNull String receive_data, String receive) {

        JSONArray array = JSON.parseArray(receive_data);
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsHandwork receive1 = JSON.parseObject(receive, ConsHandwork.class);
        return consHandworkService.addConsHand(list, receive1);

    }

    /**
     * 修改手动出库单
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "updateReceive.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceive(@NonNull String receive_data, String receive) {

        JSONArray array = JSON.parseArray(receive_data);
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsHandwork receive1 = JSON.parseObject(receive, ConsHandwork.class);
        return consHandworkService.updateConsHand(list, receive1);

    }


    /**
     * 删除领用单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteReceive.do")
    @ResponseBody
    public ResultInfo deleteReceive(@NonNull Integer id) {
        return ResultInfo.success(consHandworkService.delConsHand(id));
    }

    /**
     * 出库
     * @param id
     * @return
     */
    @RequestMapping(value = "retrievalOut.do")
    @ResponseBody
    public ResultInfo retrievalOut(Integer id){
        return consHandworkService.outWarehouse(id);
    }

    /**
     * 编辑数据初始化
     */
    @RequestMapping("getLydById.do")
    @ResponseBody
    public ResultInfo getLydById(Integer id) {
        return ResultInfo.success(consHandworkService.getOneInfoById(id));
    }

    /**
     * 编辑数据初始化
     */
    @RequestMapping("getLydByIdPage.do")
    @ResponseBody
    public ResultInfo getLydByIdPage(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit,
                                     Integer id) {
        PageHelper.startPage(page, limit);
        return ResultInfo.success(new PageInfo<>(consHandworkService.getOneInfoById(id)));
    }

}
