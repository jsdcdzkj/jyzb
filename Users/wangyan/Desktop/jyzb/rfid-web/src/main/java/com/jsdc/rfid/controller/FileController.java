package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.rfid.model.FileManage;
import com.jsdc.rfid.service.FileManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: FileController
 * Description: 文件服务控制器
 *
 * @author zhangdequan
 */
@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileManageService fileManageService;

    /**
     * 文件上传
     */
    @RequestMapping(value = "/uploadFiles",method = RequestMethod.POST, consumes = "multipart/*")
    @ResponseBody
    public ResultInfo upload(MultipartHttpServletRequest request) {
        MultiValueMap<String,MultipartFile> map = request.getMultiFileMap();// 为了获取文件，这个类是必须的
        List<MultipartFile> list = map.get("file");// 获取到文件的列表
        List<FileManage> fileManages = new ArrayList<>();
        for(MultipartFile file : list) {
            Integer temp = fileManageService.fileUpload(file);
            if(temp == null) {
                return ResultInfo.error("文件上传失败");
            }
            fileManages.add(fileManageService.selectById(temp));
        }

        return ResultInfo.success(fileManages);
    }

    /**
     * 批量上传附件
     */
    @RequestMapping(value = "batchUploadFile")
    @ResponseBody
    public ResultInfo BatchUploadFile(@RequestParam("file") MultipartFile[] files) throws Exception {
        List<FileManage> fileManages = new ArrayList<>();
        for(MultipartFile file : files) {
            Integer temp = fileManageService.fileUpload(file);
            if(temp == null) {
                return ResultInfo.error("文件上传失败");
            }
            fileManages.add(fileManageService.selectById(temp));
        }
        return ResultInfo.success(fileManages);
    }

    @GetMapping("/{fileId}/downloadFile.do")
    public void downloadFile(@PathVariable Integer fileId, HttpServletResponse response){
        fileManageService.fileDownload(fileId, response);
    }

    @GetMapping("/{fileId}/downloadOldFile.do")
    public void downloadOldFile(@PathVariable Integer fileId, HttpServletResponse response){
        fileManageService.downloadOldFile(fileId, response);
    }
}
