package com.jsdc.rfid.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.RFIDConfig;
import com.jsdc.rfid.model.SysDepartment;
import com.jsdc.rfid.model.SysPost;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysPostService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.utils.CascadeSelectTool;
import lombok.NonNull;
import net.hasor.utils.StringUtils;
import net.hasor.utils.io.output.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@RequestMapping("user")
@Controller
public class SysUserController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysPostService postService;
    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    /**
     * 跳转用户列表页
     * @return
     */
    @RequestMapping("toIndex.do")
    public String toIndex(Model model){
        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("deptList", departmentList);
        List<RFIDConfig> a = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery().like(RFIDConfig::getPrintconfigadd, "192.168.0.").eq(RFIDConfig::getIs_del, 0).last(" limit 1"));
        int isOa = 0;
        if (!CollectionUtils.isEmpty(a)){
            isOa = 1;
        }
        model.addAttribute("isOa", isOa);
        List<SysPost> posts = postService.selectList(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getIs_del, G.ISDEL_NO));
        model.addAttribute("posts", posts);
        return "system/user/index";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        SysUser user = userService.selectById(id);
        List<SysDepartment> departmentList = departmentService.getList(null);

        SysPost post = new SysPost();
        post.setDept_id(user.getDepartment());
        List<SysPost> posts = postService.getList(post);
        model.addAttribute("user", user);
        model.addAttribute("depts", departmentList);
        model.addAttribute("posts", posts);
        return "system/user/edit";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        List<SysDepartment> departmentList = departmentService.getList(null);
        List<SysPost> posts = postService.getList(null);
        model.addAttribute("depts", departmentList);
        model.addAttribute("posts", posts);
        return "system/user/add";
    }

    @RequestMapping("toEditPwd.do")
    public String toEditPwd(){
        return "system/user/editpwd";
    }

    /**
     * 分页查询
     * @param user
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysUser user,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(userService.getPage(user, page, limit));
    }

    @RequestMapping("getPageByIds.do")
    @ResponseBody
    public ResultInfo getPageByIds(SysUser user,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(userService.getPage(user, page, limit));
    }

    @GetMapping("getList.do")
    @ResponseBody
    public ResultInfo getList(SysUser user){
        return ResultInfo.success(userService.selectList(Wrappers.<SysUser>lambdaQuery().eq(
                SysUser::getIs_del, String.valueOf(0))));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysUser user){
        SysUser user1 = userService.add(user);
        if(null != user1){
            return ResultInfo.success(user1);
        }else{
            return ResultInfo.error("新增失败");
        }

    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysUser user){
        SysUser user1 = null;
        try {
            user1 = userService.edit(user);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        if(null != user1){
            return ResultInfo.success(user1);
        }else{
            return ResultInfo.error("编辑失败");
        }
    }

    @RequestMapping("updateUser.do")
    @ResponseBody
    public ResultInfo updateUser(@NonNull SysUser user){
        if(user.getId() == null){
            return ResultInfo.error("id不能为空");
        }
        if (StringUtils.isNotBlank(user.getIsloginty())){
            try {
                userService.syncUser(user);
            } catch (RuntimeException e) {
                return ResultInfo.error(e.getMessage());
            }
        }
        return ResultInfo.success(userService.updateById(user));
    }


    @RequestMapping("unifiedPortalSync.do")
    @ResponseBody
    public ResultInfo unifiedPortalSync(){
        List<SysUser> users = new ArrayList<>();
        try {
            users = userService.syncUsers();
        } catch (RuntimeException e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success(users);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id){
        try {
            userService.delete(id);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("editPassword.do")
    @ResponseBody
    @NonNull
    public ResultInfo editPassword(String originalPassword, String newPassword){
        return ResultInfo.success(userService.editPassword(originalPassword, newPassword));
    }

    /**
     * 重置密码
     */
    @RequestMapping("resetPassword.do")
    @ResponseBody
    @NonNull
    public ResultInfo resetPassword(String originalPassword, String newPassword){
        return ResultInfo.success(userService.resetPassword(originalPassword, newPassword));
    }

    /**
     * 根据id得到用户
     */
    @RequestMapping("getById.do")
    @ResponseBody
    public ResultInfo getById(Integer id){
        return ResultInfo.success(userService.selectById(id));
    }

    /**
     * 导入
     */
    @RequestMapping(value = "/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetImport(Integer fileId,String is_save){
        return userService.toImport(fileId,is_save);
    }

    /**
     * 模板下载
     * @param response
     */
    @RequestMapping(value = "/toExportTemplate.do", method = RequestMethod.GET)
    @ResponseBody
    public void excelTemplate(HttpServletResponse response) throws IOException {
        String fileName = "人员管理模板.xlsx";
        // 将工作簿写入输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = excelTemplate();
        workbook.write(outputStream);
        // 设置响应头，告诉浏览器返回的是一个Excel文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 将Excel文件写入响应的输出流
        try (OutputStream outStream = response.getOutputStream()) {
            outputStream.writeTo(outStream);
            outStream.flush();
        }
    }

    public XSSFWorkbook excelTemplate() {
        XSSFWorkbook book = new XSSFWorkbook();
        List<String> heads = Arrays.asList("本人所在机构（一级）", "本人所在机构（二级）", "本人所在机构（三级）", "身份证号码");
        CascadeSelectTool cascadeSelectTool = new CascadeSelectTool(book)
                .createSheet("人员管理")
                .createHead(heads)
                .createEmptyList(20, heads.size())
                .createFirstRow(1);


        return cascadeSelectTool.getWorkbook();
    }
}
