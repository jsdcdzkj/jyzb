package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.Date;
import java.util.List;

/**
 * 管理员派单后，维修人员接单功能
 *
 * @authon zln
 */
@Controller
@RequestMapping("feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private AssetsManageService manageService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDepartmentService dwService;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private PictureImgService pictureService;
    @Autowired
    private ExternalMaintenanceService externalMaintenanceService;

    //分页
    @RequestMapping(value = "page.do")
    public String page() {
        return "feddback/page";
    }

    //分页数据
    @RequestMapping(value = "page.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        bean.setSqr_id(userService.getUser().getId());
        bean.setTypeState("2");
        PageInfo pageInfo = applySingleService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }


    //详情
    @RequestMapping(value = "view.do")
    public String view(Model model, Feedback feedback, Integer applyId) {
        List<AssetsManage> manageList = manageService.selectList(new QueryWrapper<AssetsManage>().eq("is_del", "0"));
        model.addAttribute("manageList", manageList);
        //单位名称
        List<SysDepartment> dwList = dwService.getList(null);
        model.addAttribute("dwList", dwList);
        //维修人名称
        List<SysUser> userList = userService.selectList(new QueryWrapper<SysUser>().eq("is_del", "0"));
        model.addAttribute("userList", userList);
        ApplySingle applySingle = applySingleService.findApplySingleDetils(applyId);
        model.addAttribute("apply", applySingle);
        Feedback feed = feedbackService.findFeedbackDetils(feedback);
        model.addAttribute("feed", feed);
        List<PictureImg> lists = pictureService.getLists(applyId, 1);
        model.addAttribute("lists", lists);
        return "feddback/view";
    }

    //反馈单填写页面
    @RequestMapping(value = "add.do")
    public String add(Model model, Integer applyId, Integer state) {
//        Stdmode stdmode = new Stdmode();
//        stdmode.setType(1);
//        List<Stdmode> stdmodeList = stdmodeService.getStdmodeList(stdmode);
//        model.addAttribute("stdmodeList", stdmodeList);
        model.addAttribute("applyId", applyId);
        model.addAttribute("state", state);
        return "feddback/add";
    }

    //保存
    @RequestMapping(value = "saveOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdate(Feedback feedback, Integer applyId, Integer state, String files) {
        return feedbackService.saveOrUpdate(feedback, state, applyId, files);
    }


    //----------------------------------------------维修人员在pc端接单20210406-----------------------------------------------\\
    //待接单
    @RequestMapping(value = "djdList.do")
    public String djdList(Model model, String menu_type) {
        model.addAttribute("menu_type", menu_type);
        return "jdfeddback/djdList";
    }

    //待接单分页
    @RequestMapping(value = "djdList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo djdList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        bean.setPicType(1);
        bean.setRepair_id(userService.getUser().getId());
        PageInfo pageInfo = applySingleService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 1.type=待签收
     * 申请表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "details.do")
    public String details(Model model, Integer applyId, String menu_type) {
        ApplySingleVo applySingle = applySingleService.findApplySingleDetils(applyId);
        List<PictureImg> lists = pictureService.getLists(applyId, 1);
        //维修人员反馈
        Feedback feed = feedbackService.selectOne(new QueryWrapper<Feedback>().eq("applysingle_Id", applyId));
        if(notEmpty(feed)){
            List<PictureImg> feedList = pictureService.getLists(feed.getId(), 2);
            model.addAttribute("feedList", feedList);
            model.addAttribute("feedback", feed);
        }
        List<ExternalMaintenance> emList = externalMaintenanceService.selectAll();
        List<SysUser> sysUsers = userService.selectList(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getIs_del, G.ISDEL_NO));
        if (notEmpty(applySingle.getIsexternal())) {
            ExternalMaintenance externalMaintenance = externalMaintenanceService.selectById(applySingle.getUnitid());
            applySingle.setUnit(externalMaintenance.getUnitname());
            SysUser sysUser = userService.selectById(applySingle.getManagers());
            applySingle.setManager(sysUser.getUser_name());
        }
        model.addAttribute("emList", emList);
        model.addAttribute("userList", sysUsers);
        model.addAttribute("lists", lists);
        model.addAttribute("apply", applySingle);
        model.addAttribute("menu_type", menu_type);
        if ("1".equals(menu_type)) {
            return "jdfeddback/details";
        } else {
            return "jdfeddback/djddetails";
        }
    }

    /**
     * 列表 公共方法
     * <p>
     * picType 类型(1维修人员反馈,2.申请单表)
     */
    public void onPubList(Integer pageIndex, Integer pageSize, ApplySingleVo bean, Model model) {
        bean.setRepair_id(userService.getUser().getId());
        PageInfo pageInfo = applySingleService.selectPageList(pageIndex, pageSize, bean);
        model.addAttribute("bean", bean);
        model.addAttribute("page", pageInfo);
    }


    @RequestMapping(value = "dispatch.json", method = RequestMethod.POST)
    @ResponseBody
    public String dispatch(ApplySingleVo applySingle, String logs) {
        if (notEmpty(applySingle.getState())) {
            if (applySingle.getState() == 2) {
                logs = "派单......";
            } else if (applySingle.getState() == 4) {
                logs = "确认签收";
            } else if (applySingle.getState() == 6) {
                logs = "可以维修";
                applySingle.setEdittime(new Date());
            } else if (applySingle.getState() == 7) {
                logs = "完成反馈";
            } else if (applySingle.getState() == 8) {
                logs = "维修完成";
            } else if (applySingle.getState() == 3 || applySingle.getState() == 5) {
                //拒绝原因，无法维修原因
                applySingle.setJjyy(logs);
            }
        }
        applySingleService.updateById(applySingle);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("message", "操作成功！");
        return jsonObject.toJSONString();
    }

    //反馈单—维修方式
    @RequestMapping(value = "fankuiInfo.json", method = RequestMethod.POST)
    @ResponseBody
    public String fankuiInfo(Integer applyId, Integer state) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", state);
        jsonObject.put("bean", applySingleService.selectById(applyId));
        return jsonObject.toJSONString();
    }

    //保存
    @RequestMapping(value = "fankuiSave.json", method = RequestMethod.POST)
    @ResponseBody
    public String fankuiSave(Feedback bean, Integer state, String files) {
        return feedbackService.onSave(bean, state, files);
    }

    //我的维修详情
    @RequestMapping(value = "hdetails.do")
    public String hdetails(Feedback feedback, Integer applyId, Model model) {
        //申请表
        ApplySingle applySingle = applySingleService.findApplySingleDetils(applyId);
        if (feedback != null) {
            feedback.setApplysingle_id(applySingle.getId().toString());
        }
        //维修人员反馈
        Feedback feed = feedbackService.findFeedbackDetils(feedback);
        model.addAttribute("feedback", feed);
        //图片
        List<PictureImg> lists = pictureService.getLists(applyId, 1);
        model.addAttribute("lists", lists);
        if (notEmpty(feed) && notEmpty(feed.getId())) {
            List<PictureImg> feedpic = pictureService.getLists(feed.getId(), 2);
            model.addAttribute("feedpic", feedpic);
        }
        model.addAttribute("apply", applySingle);
        return "jdfeddback/historydetails";
    }

    /**
     * 提交外部申请
     * @param bean
     * @return
     */
    @RequestMapping(value = "externalApply.json", method = RequestMethod.POST)
    @ResponseBody
    public String externalApply(ApplySingle bean) {
        return feedbackService.externalApplication(bean);
    }
}
