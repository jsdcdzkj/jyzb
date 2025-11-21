package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.common.upload.FileRepository;
import com.jsdc.rfid.dao.PictureImgDao;
import com.jsdc.rfid.model.PictureImg;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.jsdc.core.common.utils.BaseUtils.notEmpty;

/**
 * @author zln
 * @descript 报修申请功能
 * @date 2022-04-24
 */
@Service
@Transactional
public class PictureImgService extends BaseService<PictureImgDao, PictureImg> {

    //图片存放地址
    @Value("${jsdc.filepath}")
    private String filePath;

    @Autowired
    private FileRepository fileRepository;

    public String uploadFiles(MultipartFile files) {
        String origName1 = files.getOriginalFilename();
        String fileF = null;//获取文件后缀名称
        if (FilenameUtils.getExtension(origName1) != null) {
            fileF = FilenameUtils.getExtension(origName1).toLowerCase(Locale.ENGLISH);
        }
        String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileF;
        String file_path = "upload/" + file_name;
        try {
            fileRepository.storePhotoByExt(files, filePath + file_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file_path;
    }

    public List<String> uploadBase64Files(String[] picBase64s) {
        List<String> paths = new ArrayList<String>();
        for (int i = 0; i < picBase64s.length; i++) {
            String pic = picBase64s[i];
            String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + "png";
            String file_path = "upload/" + file_name;
            FileRepository.base64ToFile(pic, file_name, filePath + "upload/");
            paths.add(file_path);
        }

        return paths;
    }

    public String baoxiuFileDownBase64(String path) {
        System.out.println("path == " + path);
        InputStream in = null;
        byte[] data = null;
        try {
            in = Files.newInputStream(Paths.get(path));
            data =new byte[in.available()];
            in.read(data);
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return Base64.encodeBase64String(data);
    }

    //新增图片上传
    public void savePicture(Integer applyId, String files, Integer type) {
        if (files != null) {
            files =  files.replace(",","-----");
            String[] str = files.split("-----");
            if (str.length > 0) {
                for (String file : str) {
                    if (notEmpty(file)) {
                        PictureImg picture = new PictureImg();
                        picture.setType(type);//申请单
                        picture.setIsdelete(0);//
                        picture.setTypeid(applyId);
                        picture.setPicurl(file);
                        insert(picture);
                    }
                }
            }
        }
    }

    //列表
    public List<PictureImg> getLists(Integer typeId, Integer type) {
        LambdaUpdateWrapper<PictureImg> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PictureImg::getIsdelete, G.ISDEL_NO);
        wrapper.eq(PictureImg::getType,type);
        wrapper.eq(PictureImg::getTypeid,typeId);
        return selectList(wrapper);
    }

    //删除
    public Integer deleteById(Integer typeId, Integer type) {
        return delete(new QueryWrapper<PictureImg>().eq("typeId", typeId).eq("type", type));
    }

}
