package com.jsdc.rfid.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.common.minio.service.MinioTemplate;
import com.jsdc.rfid.SpringUtils;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.AssetsType;
import com.jsdc.rfid.model.PrintConfig;
import com.jsdc.rfid.model.RFIDConfig;
import com.jsdc.rfid.service.AssetsManageService;
import com.jsdc.rfid.utils.CommonDataTools;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("rfidconfig")
@Controller
public class SysRfidConfigController {

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    @Autowired
    private PrintConfigMapper printConfigMapper;

    @Autowired
    private MinioMapper minioMapper;

    @Autowired
    private MinioTemplate minioTemplate;

    @RequestMapping("/toIndex.do")
    public String toIndex(Model model){
        model.addAttribute("minio", minioMapper.selectById(1));
        return "system/rfidconfig/index";
    }

    @GetMapping("/getConfig")
    @ResponseBody
    public ResultInfo getConfig(){
        List<RFIDConfig> configs = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery());
        RFIDConfig config = null;
        if(CollectionUtils.isEmpty(configs)){
            config = RFIDConfig.builder().name("RFID配置").isab(1).create_time(new Date()).create_user(1).build();
        }else {
            config = configs.get(0);
        }
        return ResultInfo.success(config);
    }


    @RequestMapping("updateConfig.do")
    @ResponseBody
    public ResultInfo updateConfig(RFIDConfig config){
        return ResultInfo.success(rfidConfigMapper.updateById(config));
    }

    @RequestMapping("/toPrint.do")
    public String toPrint(Model model){
        return "system/rfidconfig/print";
    }

    @RequestMapping("getPrintList.do")
    @ResponseBody
    public ResultInfo getPrintList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "limit", defaultValue = "10") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<PrintConfig> printConfigs = printConfigMapper.selectList(Wrappers.<PrintConfig>query()
                .eq("is_del", "0").orderByAsc("sort")
        );
        return ResultInfo.success(new PageInfo<>(printConfigs));
    }

    @RequestMapping("updatePrintConfig.do")
    @ResponseBody
    public ResultInfo updatePrintConfig(PrintConfig config){
        return ResultInfo.success(printConfigMapper.updateById(config));
    }

    /** 头像文件大小的上限值(10MB) */
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<>();

    /** 初始化允许上传的头像的文件类型 */
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }

    @PostMapping("/loginImg/upload")
    @ResponseBody
    public ResultInfo changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) throws FileNotFoundException {
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            return ResultInfo.error("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            return ResultInfo.error("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
        if (!AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            return ResultInfo.error("不支持使用该类型的文件作为头像，允许的文件类型：" + AVATAR_TYPES);
        }

        // 保存的头像文件的文件名
        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = 0;
        if (originalFilename != null) {
            beginIndex = originalFilename.lastIndexOf(".");
        }
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        try {
            minioTemplate.putObject("images", filename, file.getInputStream());
        } catch (Exception e) {
            return ResultInfo.error("上传文件时读写错误，请稍后重新尝试");
        }

        // 头像路径
        String avatar = filename;

        rfidConfigMapper.update(null, Wrappers.<RFIDConfig>lambdaUpdate().set(RFIDConfig::getLoginimage, avatar));

        // 返回成功头像路径
        return ResultInfo.success(avatar);
    }

    @PostMapping("/loginLightImg/upload")
    @ResponseBody
    public ResultInfo loginLightImg(@RequestParam("file") MultipartFile file, HttpSession session) throws FileNotFoundException {
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            return ResultInfo.error("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            return ResultInfo.error("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
        if (!AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            return ResultInfo.error("不支持使用该类型的文件作为头像，允许的文件类型：" + AVATAR_TYPES);
        }

        // 保存的头像文件的文件名
        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        try {
            minioTemplate.putObject("images", filename, file.getInputStream());
        } catch (Exception e) {
            return ResultInfo.error("上传文件时读写错误，请稍后重新尝试");
        }

        // 头像路径
//        String avatar = "/images/" + filename;

        rfidConfigMapper.update(null, Wrappers.<RFIDConfig>lambdaUpdate().set(RFIDConfig::getLogolightimage, filename));

        // 返回成功头像路径
        return ResultInfo.success(filename);
    }

    /**
     * 一键调整所有资产编号
     */
    @GetMapping("/adjustAssetNumber")
    @ResponseBody
    public ResultInfo adjustAssetNumber() {
        // 1.查询所有资产
        try {
            List<AssetsManage> lists = SpringUtils.getBean(AssetsManageMapper.class).selectList(Wrappers.<AssetsManage>lambdaQuery()
                    .eq(AssetsManage::getIs_del, G.ISDEL_NO)
            );
            for (int i = 0; i < lists.size(); i++) {
                AssetsManage bean = lists.get(i);
                AssetsType type = SpringUtils.getBean(AssetsTypeMapper.class).selectById(bean.getAsset_type_id());
                if (null == type){
                    continue;
                }

                String no = String.format("%05d", i);
                String number = StrUtil.format("{}{}{}", type.getAssets_type_code(), CommonDataTools.getTodayMonth(), no);
                bean.setAsset_code(number);
                bean.setRfid(AssetsManageService.toHexString(bean.getAsset_code()));
                SpringUtils.getBean(AssetsManageMapper.class).updateById(bean);
            }
        } catch (BeansException e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

//    public ResultInfo changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) throws FileNotFoundException {
//        // 判断上传的文件是否为空
//        if (file.isEmpty()) {
//            // 是：抛出异常
//            return ResultInfo.error("上传的头像文件不允许为空");
//        }
//
//        // 判断上传的文件大小是否超出限制值
//        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
//            // 是：抛出异常
//            return ResultInfo.error("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
//        }
//
//        // 判断上传的文件类型是否超出限制
//        String contentType = file.getContentType();
//        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
//        if (!AVATAR_TYPES.contains(contentType)) {
//            // 是：抛出异常
//            return ResultInfo.error("不支持使用该类型的文件作为头像，允许的文件类型：" + AVATAR_TYPES);
//        }
//        File path = new File(ResourceUtils.getURL("classpath:").getPath());
//        if(!path.exists()) {
//            path = new File("");
//        }
//        File upload = new File(path.getAbsolutePath(),"static/images/");
//        if(!upload.exists()) {
//            upload.mkdirs();
//        }
//        String parent= upload.getPath();
////        String parent = this.getClass().getClassLoader().getResource("static").getFile()+"/images";
//
//
//        // 获取当前项目的绝对磁盘路径
////        String parent = session.getServletContext().getRealPath("upload");
//
//
//        System.out.println(parent);
//
//        // 保存头像文件的文件夹
//        File dir = new File(parent);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        // 保存的头像文件的文件名
//        String suffix = "";
//        String originalFilename = file.getOriginalFilename();
//        int beginIndex = originalFilename.lastIndexOf(".");
//        if (beginIndex > 0) {
//            suffix = originalFilename.substring(beginIndex);
//        }
//        String filename = UUID.randomUUID().toString() + suffix;
//
//        // 创建文件对象，表示保存的头像文件
//        File dest = new File(dir, filename);
//        // 执行保存头像文件
//        try {
//            file.transferTo(dest);
//        } catch (IllegalStateException e) {
//            // 抛出异常
//            return ResultInfo.error("上传文件时出现非法状态，可能文件已被移动或删除");
//        } catch (IOException e) {
//            // 抛出异常
//            return ResultInfo.error("上传文件时读写错误，请稍后重新尝试");
//        }
//
//        // 头像路径
//        String avatar = "/images/" + filename;
//        // 从Session中获取uid和username
////        Integer uid = getUidFromSession(session);
////        String username = getUsernameFromSession(session);
//        //   Integer uid = 6;
//        //  String username=String.valueOf(1900300717);
//        // 将头像写入到数据库中
////        userService.changeAvatar(uid, username, avatar);
//
//        rfidConfigMapper.update(null, Wrappers.<RFIDConfig>lambdaUpdate().set(RFIDConfig::getLoginimage, avatar));
//
//        // 返回成功头像路径
//        return ResultInfo.success(avatar);
//    }

}