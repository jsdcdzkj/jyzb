package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.mapper.FileManageMapper;
import com.jsdc.rfid.model.FileManage;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.FileManageService;
import com.jsdc.rfid.service.ManHourAssignService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.vo.ManHourAssignVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manHourAssign")
public class ManHourAssignController {
    @Autowired
    private ManHourAssignService service;

    @Autowired
    private FileManageService fileManageService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Autowired
    private SysUserService sysUserService;


    @GetMapping("toIndex.do")
    public String toIndex() {
        return "manhourassign/index";
    }

    @RequestMapping("getPageList")
    @ResponseBody
    public ResultInfo getList(ManHourAssignVo vo,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit) {
        PageInfo<ManHourAssignVo> manHourAssignVoList = this.service.getPageList(page, limit, vo);
        return ResultInfo.success(manHourAssignVoList);
    }

    @GetMapping("importPage")
    public String importPage() {
        return "manhourassign/import";
    }

    /**
     * 文件上传
     */
    @RequestMapping(value = "/uploadFiles",method = RequestMethod.POST, consumes = "multipart/*")
    @ResponseBody
    public ResultInfo upload(MultipartHttpServletRequest request) {
        MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();// 为了获取文件，这个类是必须的
        List<MultipartFile> list = map.get("file");// 获取到文件的列表
        List<FileManage> fileManages = new ArrayList<>();
        for (MultipartFile file : list) {
            Integer temp = fileManageService.fileUpload(file);
            if (temp == null) {
                return ResultInfo.error("文件上传失败");
            }
            fileManages.add(fileManageService.selectById(temp));
        }

        return ResultInfo.success(fileManages);
    }

    @PostMapping("uploadManHourAssignFile")
    @ResponseBody
    public ResultInfo uploadManHourAssignFile(Integer fileId) {
        SysUser user = this.sysUserService.getUser();
        return this.service.generateStatisticExcel(fileId, user);
    }

    @GetMapping("downLoadManHourAssignExcel")
    public void downLoadManHourAssignExcel(Integer fileId, HttpServletResponse response) throws UnsupportedEncodingException {
        FileManage fileManage  = this.fileManageService.selectById(fileId);
        String file_name = fileManage.getFile_name();
        File file = new File(uploadPath + File.separator + file_name);
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileManage.getStore_name(), "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", String.valueOf(file.length()));
        response.setContentType("application/octet-stream");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            bis = new BufferedInputStream(fi);
            int len = 0;
            byte[] buffer = new byte[4 * 1024];
            bos = new BufferedOutputStream(response.getOutputStream());
            while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, len);
            }

            bos.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
