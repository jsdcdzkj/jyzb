package com.jsdc.rfid.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.common.minio.service.MinioTemplate;
import com.jsdc.common.minio.vo.MinioEntity;
import com.jsdc.rfid.mapper.MinioMapper;
import lombok.Cleanup;
import net.hasor.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * minio 文件服务
 * @author zdq
 */
@RestController
@RequestMapping("/minio")
public class MinioFileController {

    private final MinioTemplate minioTemplate;

    @Autowired
    private MinioMapper minioMapper;

    public MinioFileController(MinioTemplate minioTemplate) {
        this.minioTemplate = minioTemplate;
    }

    /**
     * 上传附件
     * @param spaceName minio的空间名称
     * @param file 文件
     */
    @PostMapping("/{spaceName}/importFile")
    public ResultInfo importBugs(@PathVariable String spaceName, MultipartFile file) {
        try {
            String ymd = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + (int) ((Math.random() * 9 + 1) * 1000);

            String suffixName = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            String filename = ymd + suffixName;
            minioTemplate.putObject(spaceName, filename, file.getInputStream());

            Map<String, String> map = new HashMap<>();
            map.put("filename", filename);
            map.put("originalFilename", file.getOriginalFilename());
            return ResultInfo.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("上传文件错误!");
        }
    }

    /**
     * 下载附件
     */
    @GetMapping("/{bucketName}/minio")
    public void getMinioObject(@PathVariable String bucketName, @RequestParam String fileName,
                               final HttpServletResponse response) {
        try {
            @Cleanup InputStream inputStream = minioTemplate.getObject(bucketName, fileName);
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
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除附件
     */
    @GetMapping("/{bucketName}/delete")
    public ResultInfo deleteMinioObject(@PathVariable String bucketName, @RequestParam String fileName) {
        try {
            minioTemplate.removeObject(bucketName, fileName);
            return ResultInfo.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("删除文件错误!");
        }
    }

    /**
     * 预览地址
     */
    @GetMapping("/preview")
    public String preview(@RequestParam(name = "bucketName") String bucketName, @RequestParam(name = "fileName") String fileName) {
        System.out.println("bucketName:"+bucketName);
        try {
            return minioTemplate.getPreviewFileUrl(bucketName, fileName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 更新配置信息
     */
    @PostMapping("/update")
    public ResultInfo update(MinioEntity minioEntity) {
        if (null == minioEntity){
            return ResultInfo.error("更新配置信息错误!");
        }
        minioMapper.update(null, Wrappers.<MinioEntity>lambdaUpdate().eq(MinioEntity::getId, 1)
                .set(StringUtils.isNotBlank(minioEntity.getUrl()), MinioEntity::getUrl, minioEntity.getUrl())
                .set(StringUtils.isNotBlank(minioEntity.getUsername()), MinioEntity::getUsername, minioEntity.getUsername())
                .set(StringUtils.isNotBlank(minioEntity.getPassword()), MinioEntity::getPassword, minioEntity.getPassword())
        );
        return ResultInfo.success();
    }



}
