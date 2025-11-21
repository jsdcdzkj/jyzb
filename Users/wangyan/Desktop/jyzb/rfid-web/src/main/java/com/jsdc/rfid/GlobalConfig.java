package com.jsdc.rfid;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.RFIDConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalConfig {

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    @ModelAttribute
    public void globalModel(ModelMap model){

        List<RFIDConfig> configs = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery());
        RFIDConfig config = null;
        if(org.apache.commons.collections.CollectionUtils.isEmpty(configs)){
            config = RFIDConfig.builder().name("RFID配置").isab(1).create_time(new Date()).create_user(1).build();
        }else {
            config = configs.get(0);
        }
        model.addAttribute("config", config);
    }
}