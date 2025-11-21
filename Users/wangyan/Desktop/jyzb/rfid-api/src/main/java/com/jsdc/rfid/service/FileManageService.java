package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.FileManageDao;
import com.jsdc.rfid.model.FileManage;
import com.jsdc.rfid.model.SysUser;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class FileManageService extends BaseService<FileManageDao, FileManage> {
    @Value("${file.upload-path}")
    private String uploadPath;

    @Autowired
    private SysUserService userService;

    /**
     * @param file
     * @return com.jsdc.itss.vo.ResultInfo
     * @description 文件上传
     * @author wp
     * @date 2022/3/21
     */
    public Integer fileUpload(MultipartFile file) {

        //如果父文件夹不存在 则新建
        File parentFile = new File(uploadPath);
        if (!parentFile.exists() && !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String suffix = null;
        if (fileName != null) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        String path = uploadPath + File.separator + newFileName;
        File targetFile = new File(path);
        try {
            @Cleanup FileOutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(file.getBytes());
        } catch (Exception e) {

        }
        long fileSize = file.getSize();
        SysUser sysUser = userService.getUser();
        FileManage fileManage = new FileManage();
        fileManage.setFile_name(newFileName);
        fileManage.setStore_name(fileName);
        fileManage.setFile_size(fileSize == 0 ? "0" : String.valueOf(fileSize));
        fileManage.setFile_type(suffix);
        fileManage.setCreate_user(sysUser.getId());
        fileManage.setCreate_time(new Date());
        fileManage.setUpdate_user(sysUser.getId());
        fileManage.setUpdate_time(new Date());
        fileManage.setIs_del("0");
        if (insert(fileManage) > 0) {
            return fileManage.getId();
        } else {
            return null;
        }
    }

    /**
     * @param file
     * @return com.jsdc.itss.vo.ResultInfo
     * @description 文件上传
     * @author wp
     * @date 2022/3/21
     */
    public Integer appFileUpload(MultipartFile file,Integer userId) {

        //如果父文件夹不存在 则新建
        File parentFile = new File(uploadPath);
        if (!parentFile.exists() && !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String suffix = null;
        if (fileName != null) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        String path = uploadPath + File.separator + newFileName;
        File targetFile = new File(path);
        try {
            @Cleanup FileOutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(file.getBytes());
        } catch (Exception e) {

        }
        long fileSize = file.getSize();
        SysUser sysUser = userService.selectById(userId);
        FileManage fileManage = new FileManage();
        fileManage.setFile_name(newFileName);
        fileManage.setStore_name(fileName);
        fileManage.setFile_size(fileSize == 0 ? "0" : String.valueOf(fileSize));
        fileManage.setFile_type(suffix);
        fileManage.setCreate_user(sysUser == null ? 1 : sysUser.getId());
        fileManage.setCreate_time(new Date());
        fileManage.setUpdate_user(sysUser == null ? 1 : sysUser.getId());
        fileManage.setUpdate_time(new Date());
        fileManage.setIs_del("0");
        if (insert(fileManage) > 0) {
            return fileManage.getId();
        } else {
            return null;
        }
    }

    /**
     * @param files
     * @return com.jsdc.itss.vo.ResultInfo
     * @description 多文件上传
     * @author wp
     * @date 2022/3/21
     */
    public ResultInfo filesUpload(MultipartFile[] files) {
        for (MultipartFile file : files) {
            fileUpload(file);
        }
        return ResultInfo.success();
    }

    public void fileDownload(Integer fileId, HttpServletResponse response) {


        FileManage fileManage = selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();
        File file = new File(path);
        try {
            @Cleanup FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            byte[] data = os.toByteArray();
            response.resetBuffer();
            response.resetBuffer();
            response.setHeader("Content-Disposition", "attachment");
            //response.addHeader("file-name", URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadOldFile(Integer fileId, HttpServletResponse response) {
        FileManage fileManage = selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();
        File file = new File(path);
        try {
            @Cleanup FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            byte[] data = os.toByteArray();
            response.resetBuffer();
            response.resetBuffer();
            response.setHeader("Content-Disposition", "attachment");
//            response.addHeader("file-name", URLEncoder.encode(fileManage.getStore_name(), "UTF-8"));

            //下载文件
            String contentDisposition = String.format("attachment; filename=%s", UriUtils.encode(fileManage.getStore_name(), "UTF-8"));
            //浏览器内嵌显示
            contentDisposition = String.format("inline; filename=%s", UriUtils.encode(fileManage.getStore_name(), "UTF-8"));
            response.setHeader("Content-disposition", contentDisposition);

//            response.setHeader("Content-Disposition", "attachment;filename=" + fileManage.getStore_name());
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
