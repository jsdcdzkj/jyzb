package com.jsdc.rfid.controller.app.assets;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.rfid.service.AssetsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AppAssetsManageController {

    @Autowired
    private AssetsManageService assetsManageService;

    /**
     * 得到资产详情信息
     * @param asset_code
     * @return
     */
    public String getAssetsManage(String asset_code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bean", assetsManageService.getAssetsManageByCode(asset_code));
        return jsonObject.toJSONString();
    }
}