package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.utils.PostUtils;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * @author zln
 * @descript 报修管理
 * @date 2022-04-24
 */
@Controller
@RequestMapping("/applysingle")
public class ApplySingleController extends BaseController {

    @Value("${jsdc.filepath}")
    private String filePath;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private AssetsTypeService typeService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private PictureImgService imgService;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private PictureImgService pictureImgService;
    @Autowired
    private OperationRecordService operationRecordService;

    /**
     * 报修管理
     * 20230128做变更
     * 编写人：zln
     *
     * @return
     */
    @RequestMapping(value = "pageList.do")
    public String pageList(Model model) {
        //状态
        List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getIs_del, G.ISDEL_NO)
                .eq(SysDict::getType, "bx_state"));
        model.addAttribute("sysDicts", sysDicts);
        return "applysingle/page";
    }

    /**
     * 报修管理
     * 20230128做变更
     * 编写人：zln
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "pageList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        //根据当前登录的用户查询岗位
        SysPost sysPost = sysPostService.selectById(sysUserService.getUser().getPost());
        if (0 == sysPost.getData_permission()) {//个人
            bean.setSqr_id(sysUserService.getUser().getId());
        }
        return ResultInfo.success(applySingleService.selectPageList(page, limit, bean));
    }

    @Autowired
    private SysDictService sysDictService ;
    /**
     * 我的维修信息列表
     * 20230128做变更
     * 编写人：zln
     */
    @RequestMapping(value = "myRepair.do")
    public String history(Model model) {
        //状态
        List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getIs_del, G.ISDEL_NO)
                .eq(SysDict::getType, "bx_state"));
        model.addAttribute("sysDicts", sysDicts);
        return "jdfeddback/history";
    }

    /**
     * 我的维修信息列表
     * 20230128做变更
     * 编写人：zln
     *
     * @param applySingle
     * @return
     */
    @RequestMapping(value = "myRepair.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo history(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo applySingle) {
        applySingle.setRepair_id(sysUserService.getUser().getId());
        return ResultInfo.success(applySingleService.selectPageList(page, limit, applySingle));
    }


    //跳转分页页面
    @RequestMapping(value = "add.do")
    public String add(Model model, Integer id) {
        //资产品类
        List<AssetsType> list = typeService.selectList(new QueryWrapper<AssetsType>().eq("is_del", "0").eq("is_enable", "1"));
        model.addAttribute("list", list);
        SysUser user = sysUserService.getUser();
        model.addAttribute("user", user);
        //部门数据
        List<SysDepartment> dwList = departmentService.selectList(new QueryWrapper<SysDepartment>().eq("is_del", "0"));
        model.addAttribute("dwList", dwList);
        //维修人员
        List<SysUser> userList = sysUserService.selectList(new QueryWrapper<SysUser>().eq("is_del", "0").eq("post", PostUtils.wxry));
        model.addAttribute("userList", userList);
        String code = commonDataTools.getNo(DataType.APPLYSINGLE_BX.getType(), null);
        model.addAttribute("code", code);
        if (notEmpty(id)) {
            List<PictureImg> lists = pictureImgService.getLists(id, 1);//type=1代表申请单  type=2代表反馈单
            model.addAttribute("lists", lists);
            ApplySingle single = applySingleService.selectById(id);
            model.addAttribute("bean", single);
            return "applysingle/update";
        } else {
            return "applysingle/add";
        }
    }


    /**
     * 报修申请
     * 20230128做变更
     * 编写人：zln
     *
     * @param applySingle
     * @param files
     * @param ids
     * @return
     */
    @RequestMapping(value = "save.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(ApplySingle applySingle, String files, String ids) {
        Integer count = applySingleService.save(applySingle, files, ids);
        if (count == 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(applySingle.getOrder_code())
                    .type(PostUtils.operation_type_bx).record("新增一条数据").build());
            return ResultInfo.success(count);
        } else {
            return ResultInfo.error("失败");
        }
    }

    /**
     * menu_type 1、流程中单据 2、驳回单据 3、历史记录
     *
     * @param model
     * @param menu_type
     * @return
     */
    @RequestMapping(value = "page.do")
    public String page(Model model, String menu_type) {
        if ("1".equals(menu_type)) {
            model.addAttribute("menu_title", "流程中单据");
        } else if ("2".equals(menu_type)) {
            model.addAttribute("menu_title", "驳回单据");
        } else {
            model.addAttribute("menu_title", "历史记录");
        }
        model.addAttribute("menu_type", menu_type);
        return "applysingle/page";
    }

    /**
     * menu_type 1、流程中单据 2、驳回单据 3、历史记录
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "page.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        SysUser user = sysUserService.getUser();
        //普通人员
        if (PostUtils.plain.equals(user.getPost() + "")) {
            bean.setSqr_id(user.getId());
        }
        PageInfo<ApplySingleVo> pageInfo = applySingleService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 管理员操作
     *
     * @param model
     * @param menu_type
     * @return
     */
    @RequestMapping(value = "adminList.do")
    public String adminList(Model model, String menu_type) {
        if ("1".equals(menu_type)) {
            model.addAttribute("menu_title", "未派单列表");
        } else if ("2".equals(menu_type)) {
            model.addAttribute("menu_title", "已派单列表");
        } else {
            model.addAttribute("menu_title", "已完成列表");
        }
        model.addAttribute("menu_type", menu_type);
        return "applysingle/adminList";
    }

    /**
     * 1、未派单 2、已派单 3、已完成
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adminList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo yplist(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        SysUser user = sysUserService.getUser();
        //普通人员
        if (PostUtils.plain.equals(user.getPost() + "")) {
            bean.setSqr_id(user.getId());
        }
        PageInfo pageInfo = applySingleService.selectYPDPage(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }


    //保存
    @RequestMapping(value = "saveOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveOrUpdate(ApplySingle applySingle, String files, String ids) {
        Integer count = applySingleService.saveOrUpdate(applySingle, files, ids);
        if (count == 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(applySingle.getOrder_code())
                    .type(PostUtils.operation_type_bx).record("新增一条数据").build());
            return ResultInfo.success(count);
        } else {
            return ResultInfo.error("失败");
        }
    }

    /**
     * 撤单功能
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "cancelOrder.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo cancelOrder(Integer id) {
        ApplySingle applySingle = applySingleService.selectById(id);
        if (notEmpty(applySingle)) {
            applySingle.setState(-1);//已撤单
            applySingleService.updateById(applySingle);
            //设置为资产故障
            applySingleService.assetManageUpdate(applySingle.getAssetmanage_id(), 2);
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(applySingle.getOrder_code())
                    .type(PostUtils.operation_type_bx).record("撤回一条单据").build());
            return ResultInfo.success();
        }
        return ResultInfo.error("操作失败");
    }

    /**
     * 派单功能
     *
     * @param applySingle
     * @param logs        日志内容
     * @return
     */
    @RequestMapping(value = "dispatch.json", method = RequestMethod.POST)
    @ResponseBody
    public boolean dispatch(ApplySingle applySingle, String logs, String files) {
        if (notEmpty(applySingle.getState())) {
            if (applySingle.getState() == 2) {
                logs = "派单......";
            } else if (applySingle.getState() == 4) {
                logs = "确认签收";
            } else if (applySingle.getState() == 6) {
                logs = "可以维修";
            } else if (applySingle.getState() == 7) {
                logs = "完成反馈";
            } else if (applySingle.getState() == 8) {
                logs = "维修完成";
            }
        }
        pictureImgService.savePicture(applySingle.getId(), files, 3);//评论图片上传
        applySingleService.updateById(applySingle);
        return true;
    }

    /**
     * 上传图片
     */
    @RequestMapping(value = "baoxiuFileUpd.json")
    @ResponseBody
    public String baoxiuFileUpd(@RequestParam("file") MultipartFile[] files) throws Exception {
        String result = "";
        if (!(files.length == 0)) {
            for (MultipartFile file : files) {
                result = imgService.uploadFiles(file);
            }
        }
        JSONObject res = new JSONObject();
        res.put("result", result);
        if (empty(result)) {
            res.put("status", false);
        } else {
            res.put("status", true);
        }
        return res.toJSONString();
    }

    /**
     * 图片读取
     *
     * @param path
     * @param response
     */
    @RequestMapping(value = "readPic.do")
    public void getImageFilePath(String path, HttpServletResponse response) {
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            File file = new File(filePath + path);
            if (!file.exists()) {
                return;
            }
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (fis != null) {
                fis.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转资产选择页面
     */
    @RequestMapping(value = "toSelectAssetManage.page")
    public String assetManageList(Model model, Integer user_id,Integer bx_type) {
        model.addAttribute("user_id", user_id);
        model.addAttribute("bx_type", bx_type);
        return "applysingle/stock-select";
    }
}
