package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.utils.PostUtils;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private BorrowAssetsService borrowAssetsService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysPostService sysPostService;


    /**
     * 跳转借用列表页
     *
     * @return
     */
    @RequestMapping("toBorrowIndex.do")
    public String toIndex() {
        return "change/borrow/index";
    }

    /**
     * 跳转借用新增页面
     *
     * @return
     */
    @RequestMapping("toAddBorrow.do")
    public String toAddBorrow(Model model) {
        SysUser sysUser = sysUserService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);


        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUsers = sysUserService.selectList(userqueryWrapper);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.ne("asset_state","6");
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);
        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("sysUsers", sysUsers);
        return "change/borrow/add";
    }


    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model) {
        Borrow borrow = borrowService.getOneInfo(id);
        List<BorrowAssets> borrowAsset = borrowService.getOneById(id);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.ne("asset_state","6");
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);
        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUser = sysUserService.selectList(userqueryWrapper);
        model.addAttribute("borrow", borrow);
        model.addAttribute("borrowAsset", borrowAsset);
        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        return "change/borrow/edit";
    }


    /**
     * 跳转借用管理列表页
     *
     * @return
     */
    @RequestMapping("toBorrowManageIndex.do")
    public String toBorrowManageIndex() {
        return "change/borrowManage/index";
    }

    /**
     * 跳转领用页
     *
     * @return
     */
    @RequestMapping("toConfirm.do")
    public String toConfirm(Integer id, Model model) {
        model.addAttribute("id", id);
        return "change/borrowManage/apply";
    }


    /**
     * 跳转展示详情页
     *
     * @return
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type, Model model) {
        List<BorrowAssets> borrowAssets = borrowService.getOneById(id);
        model.addAttribute("borrowAssets", borrowAssets);
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        return "change/borrow/view";
    }


    /**
     * 跳转逾期详情页
     *
     * @return
     */
    @RequestMapping("toOverdueIndex.do")
    public String toOverdueIndex(Model model) {
        List<BorrowAssets> borrowAssets = borrowService.beOverdue();
        model.addAttribute("borrowAssets", borrowAssets);
        return "change/borrowManage/overdue";
    }

    /**
     * 跳转打印详情页
     *
     * @return
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        Borrow borrow = borrowService.getOneInfo(id);
        List<BorrowAssets> borrowAsset = borrowService.getOneById(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        model.addAttribute("borrow", borrow);
        model.addAttribute("borrowAsset", borrowAsset);
        model.addAttribute("time",time);
        return "change/borrow/print";
    }




    /**
     * 借用申请首页分页展示数据
     *
     * @param borrow
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getPage(Borrow borrow, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        int userId = sysUserService.getUser().getId();
        borrow.setCreate_user(userId);
        PageInfo<Borrow> pageInfo = borrowService.getPageInfo(borrow, page, limit);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 借用单新增
     *
     * @param borrow
     * @return
     */
    @RequestMapping(value = "addBorrow.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addBorrow(@NonNull String borrow_data, String borrow) {
        JSONArray array = JSON.parseArray(borrow_data);
        List<BorrowAssets> list = array.toJavaList(BorrowAssets.class);
        Borrow borrow1 = JSON.parseObject(borrow, Borrow.class);
        return borrowService.addBorrow(borrow1, list);
    }

    /**
     * 修改借用单
     * 作者：xuaolong
     *
     * @param borrow
     * @return
     */
    @RequestMapping(value = "updateBorrow.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateBorrow(@NonNull String borrow_data, String borrow) {
        JSONArray array = JSON.parseArray(borrow_data);
        List<Integer> list = array.toJavaList(Integer.class);
        Borrow borrow1 = JSON.parseObject(borrow, Borrow.class);
        return borrowService.updateBorrow(borrow1, list);
    }

    /**
     * 删除借用单
     * 作者：xuaolong
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteBorrow.do")
    @ResponseBody
    public ResultInfo deleteBorrow(Integer id) {
        return borrowService.deleteBorrow(id);
    }

    /**
     * 借用单送审
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "submitBorrow.do")
    @ResponseBody
    public ResultInfo submitBorrow(Integer id) {
        return borrowService.submitBorrow(id);
    }


    /**
     * 打印借用单单据
     *
     * @param borrow
     * @return
     */
    @RequestMapping(value = "printBillBorrow.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo printBillBorrow(Borrow borrow) {
        return borrowService.printBillBorrow(borrow);
    }


    /**
     * 分页展示借用管理
     *
     * @param borrow
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "collectionBorrowByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo collectionBorrowByPage(Borrow borrow, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        return borrowService.collectionBorrowByPage(borrow, page, limit);
    }


    /**
     * 根据Id查询借用单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getOneById.do")
    @ResponseBody
    public ResultInfo getOneById(Integer id) {
        List<BorrowAssets> list = borrowService.getOneById(id);
        return ResultInfo.success(list);
    }

    /**
     * 借用单确认
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "confirmBorrowAssets.do")
    @ResponseBody
    public ResultInfo confirmBorrowAssets(Integer id) {
        return borrowService.confirmBorrowAssets(id);
    }


    /**
     * 借用归还
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "returnBorrow.do")
    @ResponseBody
    public ResultInfo returnBorrow(Integer id) {
        return borrowService.returnBorrow(id);
    }

    /**
     * 获取当前登陆用户逾期未归还的资产
     *
     * @return
     */
    @RequestMapping(value = "beOverdue.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo beOverdue() {
        List<BorrowAssets> list = borrowService.beOverdue();
        return ResultInfo.success(list);
    }


    /**
     * 跳转待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        return "shenpimana/jieyongsp/index";
    }


    /**
     * 待审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Borrow bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserService.selectById(userId).getPost();
        if (null == postId) {
            return ResultInfo.error("没有权限查看");
        }
        SysPost sysPost = sysPostService.selectById(postId);
        if (null == sysPost) {
            return ResultInfo.error("没有权限查看");
        }
        Integer data_permission = sysPost.getData_permission();
        if (G.DATAPERMISSION_PERSONAL == data_permission) {//仅查看个人通过审核数据
            bean.setUse_id(userId);
        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
            int department = sysUser.getDepartment();
            bean.setDepartment_id(department);
        }
        PageInfo<Borrow> pageInfo = borrowService.getPageInfo(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(Borrow bean,String logs) {
        Integer count = borrowService.updateById(bean);
        if (count > 0) {
            if (bean.getStatus().equals(PostUtils.approved)) {
                if (Base.empty(logs)) {
                    logs ="审批通过";
                } else {
                    logs = "审批通过，" + logs;
                }
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getBorrow_code())
                        .type(PostUtils.operation_type_jy).operate_id(bean.getId()).record(logs).build());
            } else if (bean.getStatus().equals(PostUtils.reject)) {
                if (Base.empty(logs)) {
                    logs ="驳回成功";
                } else {
                    logs = "驳回成功，" + logs;
                }
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getBorrow_code())
                        .type(PostUtils.operation_type_jy).operate_id(bean.getId()).record(logs).build());
            }
            //审批通过
            if (PostUtils.approved.equals(bean.getStatus())) {
                List<BorrowAssets> list = borrowAssetsService.selectList(new QueryWrapper<BorrowAssets>().eq("borrow_id", bean.getId()));
                list.forEach(a -> {
                    a.setBorrow_status("1");
                    borrowAssetsService.updateById(a);
                });
            }
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

}
