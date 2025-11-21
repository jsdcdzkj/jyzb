package com.jsdc.rfid.controller.app.repair;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.service.PictureImgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/app/repair")
public class AppController extends BaseController {

    @Value("${jsdc.filepath}")
    private String filePath;
    @Autowired
    private PictureImgService pictureService;

    //上传图片方法
    public String baoxiuFileUpd(String result) {
        JSONObject res = new JSONObject();
        res.put("result", result);
        if (empty(result)) {
            res.put("status", false);
        } else {
            String[] picBase64s = result.split(",");
            List<String> paths = pictureService.uploadBase64Files(picBase64s);
            String tempPath = StringUtils.join(paths,",");
            res.put("result", tempPath);
            res.put("status", true);
        }

        System.out.println(res.toJSONString());
        return res.toJSONString();
    }

    /**
     * 读取图片
     */
    public String baoxiuFileDown(String path) {
        JSONObject res = new JSONObject();
        res.put("result", "");
        if (empty(path)) {
            res.put("status", false);
        } else {
            String picBase64 = pictureService.baoxiuFileDownBase64(path);
            res.put("result", picBase64);
            res.put("status", true);
        }
        System.out.println(res.toJSONString());
        return res.toJSONString();
    }

}
