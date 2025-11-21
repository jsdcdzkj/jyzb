package com.jsdc.rfid.controller.app.repair;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.utils.PostUtils;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/app/police")
public class PoliceController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private PictureImgService pictureImgService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPostPermissionService postPermissionService;

    /**
     * 保存方法
     * 20230129做变更
     * 编写人：zln
     *
     * @param files
     * @return
     */
    public String saveOrUpdateData(String dept_id, String dept_name, Integer sqr_id, String sqr_name, Integer degree,
                                   String good_name, String assetType_name, String assetManage_id, String assetType_Id,
                                   String phone, String yytime, Integer pd_type, String represent, String files) {
        JSONObject object = new JSONObject();
        String code = commonDataTools.getNo(DataType.APPLYSINGLE_BX.getType(), null);
        ApplySingle applySingle = new ApplySingle();
        applySingle.setDept_id(dept_id);
        applySingle.setDept_name(dept_name);
        applySingle.setSqr_id(sqr_id);
        applySingle.setSqr_name(sqr_name);
        applySingle.setDegree(degree);
        applySingle.setGood_name(good_name);
        applySingle.setAssettype_name(assetType_name);
        applySingle.setAssetmanage_id(assetManage_id);
        applySingle.setAssettype_id(assetType_Id);
        applySingle.setPhone(phone);
        if (notEmpty(yytime)) {
            try {
                applySingle.setYytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(yytime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        applySingle.setPd_type(pd_type);
        applySingle.setRepresent(represent);
        applySingle.setOrder_code(code);
        Integer count = applySingleService.save(applySingle, files, null);
        if (count == 0) {
            object.put("code", 0);
            object.put("msg", "操作成功！");
        } else if (count == -1) {
            object.put("code", -1);
            object.put("msg", "暂无维修人员");
        } else if (count == 1) {
            object.put("code", -1);
            object.put("msg", "操作失败！");
        }
        return object.toJSONString();
    }

    /**
     * 报修管理
     * 20230129做变更
     * 编写人：zln
     *
     * @param user_id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public String pageList(Integer user_id, Integer pageIndex, Integer pageSize) {
        ApplySingleVo vo = new ApplySingleVo();
        //根据当前登录的用户查询岗位
        SysUser sysUser = sysUserService.selectById(user_id);
        SysPost sysPost = sysPostService.selectById(sysUser.getPost());
        if (0 == sysPost.getData_permission()) {//个人
            if(Base.notEmpty(user_id)){
                vo.setSqr_id(user_id);
            }else{
                vo.setSqr_id(sysUserService.getUser().getId());
            }
        }
        JSONObject object = new JSONObject();
        object.put("page", applySingleService.selectPageList(pageIndex, pageSize, vo));
        return object.toJSONString();
    }


    /**
     * 我的维修
     * 20230129做变更
     * 编写人：zln
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public String historyData(Integer pageIndex, Integer pageSize, Integer repair_id) {
        ApplySingleVo vo = new ApplySingleVo();
        vo.setRepair_id(repair_id);
        PageInfo pageInfo = applySingleService.selectPageList(pageIndex, pageSize, vo);
        JSONObject object = new JSONObject();
        object.put("page", pageInfo);
        return object.toJSONString();
    }

    /**
     * 获取资产类型、资产名称数据
     *
     * @param type          1.资产类型 2.资产数据
     * @param dept_id       部门id
     * @param asset_type_id 资产类型id
     * @return
     */
    public String getJsonList(Integer type, Integer dept_id, Integer user_id, Integer asset_type_id) {
        JSONObject jsonObject = feedbackService.getJsonList(type, dept_id, user_id, asset_type_id);
        return JSONObject.toJSONString(jsonObject);
    }


    /**
     * 流程中的单据、历史单据
     *
     * @param pageIndex
     * @param pageSize
     * @param sqr_id
     * @return
     */

    public String pageData(Integer sqr_id, Integer pageIndex, Integer pageSize, String menu_type) {
        ApplySingleVo vo = new ApplySingleVo();
        if (notEmpty(menu_type) && "4".equals(menu_type)) {
            if ("4".equals(menu_type)) {
                vo.setRepair_id(sqr_id);
            } else {
                vo.setSqr_id(sqr_id);
            }
        }
        vo.setMenu_type(menu_type);
        JSONObject object = new JSONObject();
        object.put("page", applySingleService.selectPageList(pageIndex, pageSize, vo));
        return object.toJSONString();
    }


    /**
     * 撤单功能
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "cancelOrder.json", method = RequestMethod.POST)
    @ResponseBody
    public String cancelOrderData(Integer id) {
        ApplySingle applySingle = applySingleService.selectById(id);
        JSONObject object = new JSONObject();
        Integer count = 0;
        if (notEmpty(applySingle)) {
            applySingle.setState(-1);//已撤单
            count = applySingleService.updateById(applySingle);
            //设置为资产故障
            applySingleService.assetManageUpdate(applySingle.getAssetmanage_id(), 2);
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(applySingle.getOrder_code())
                    .type(PostUtils.operation_type_bx).record("撤回一条单据").build());
        }
        object.put("count", count);
        return object.toJSONString();
    }


    /**
     * 详情数据
     *
     * @param id
     * @return
     */
    public String detailsData(Integer id) {
        JSONObject object = new JSONObject();
        ApplySingleVo applySingle = applySingleService.findApplySingleDetils(id);
        //维修人员反馈
        Feedback feedback = feedbackService.selectOne(new QueryWrapper<Feedback>().eq("applysingle_Id", id));
        if (notEmpty(feedback)) {
            List<PictureImg> feedList = pictureImgService.getLists(feedback.getId(), 2);
            if (feedList.size() > 0 && null != feedList) {
                object.put("feedpic", feedList);
            }
        }
        List<PictureImg> lists = pictureImgService.getLists(id, 1);
        if (lists.size() > 0 && null != lists) {
            object.put("lists", lists);
        }
        object.put("applySingle", applySingle);
        object.put("feedback", feedback);
        return object.toJSONString();
    }


    /**
     * 反馈功能
     * 20230129做变更
     * 编写人：zln
     *
     * @return
     */
    public String fankuiSaveData(String applysingle_Id, String files, Integer state, String startTime, String endTime, String describes, Integer repair_id, Integer repair_status) {
        Feedback feedback = new Feedback();
        feedback.setApplysingle_id(applysingle_Id);
        feedback.setFiles(files);
        feedback.setRepair_status(repair_status);
        feedback.setState(state);
        if (notEmpty(startTime)) {
            try {
                feedback.setStarttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
                feedback.setEndtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        feedback.setDescribes(describes);
        feedback.setUserId(repair_id);
        return feedbackService.onSave(feedback, feedback.getState(), feedback.getFiles());
    }


    /**
     * 签收、维修
     *
     * @param
     * @return
     */
    public String dispatchData(ApplySingleVo applySingle) {
        if (notEmpty(applySingle.getState())) {
            if (applySingle.getState() == 2) {
                applySingle.setLogs("派单......");
            } else if (applySingle.getState() == 4) {
                applySingle.setLogs("确认签收");
            } else if (applySingle.getState() == 6) {
                applySingle.setLogs("可以维修");
                applySingle.setExtrnaltime(new Date());
            } else if (applySingle.getState() == 7) {
                applySingle.setLogs("完成反馈");
            } else if (applySingle.getState() == 8) {
                applySingle.setLogs("维修完成");
            } else if (applySingle.getState() == 3 || applySingle.getState() == 5) {
                //拒绝原因，无法维修原因
                applySingle.setJjyy(applySingle.getLogs());
            }
        }
        pictureImgService.savePicture(applySingle.getId(), applySingle.getFiles(), 3);//评论图片上传
        applySingleService.updateById(applySingle);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("message", "操作成功！");
        return jsonObject.toJSONString();
    }


    /**
     * 根据岗位获取菜单权限
     * 20230128做变更
     * 编写人：zln
     *
     * @param post_id
     * @return
     */
    @RequestMapping(value = "selectByPostPermissionCode.do", method = RequestMethod.POST)
    @ResponseBody
    public String selectByPostPermissionCode(String post_id) {
        JSONObject object = new JSONObject();
        List<String> postPermission = postPermissionService.selectByPostPermissionCode(post_id);
        object.put("menuPermission",postPermission);
        return object.toJSONString();
    }

}
