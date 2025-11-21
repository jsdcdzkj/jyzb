package com.jsdc.rfid;


import com.jsdc.rfid.utils.UnifiedPortalUtils;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.RFIDConfig;
import lombok.extern.slf4j.Slf4j;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    @Override
    public void run(String... args) throws Exception {
        // 在这里编写启动时需要执行的逻辑
        try {
            // 1.找到通用配置
            RFIDConfig rfid = rfidConfigMapper.selectById(1);
            if (rfid == null) {
                rfid = new RFIDConfig();
                rfid.setId(1);
                rfid.setName("RFID配置");
                rfid.setIsab(1);
                rfidConfigMapper.insert(rfid);
            }
            log.info("1.通用配置已经找到");
            // 2.找到统一门户配置
            if(StringUtils.isBlank(rfid.getToken()) || null == rfid.getTokendate()
                    || !UnifiedPortalUtils.isSameDay(rfid.getTokendate(), new Date())){
                if (StringUtils.isBlank(rfid.getPortalurl()) || StringUtils.isBlank(rfid.getAppid()) || StringUtils.isBlank(rfid.getPrivatekey())){
                    log.info("配置文件的统一门户没有配置,请先配置.");
                }else {
                    String url = rfid.getPortalurl();
                    String token = UnifiedPortalUtils.getToken(url, rfid.getAppid(), rfid.getPrivatekey());
                    if (StringUtils.equals("500",token)){
                        log.error("统一门户获取token错误,请检查参数接口等信息.");
                    }else {
                        rfid.setToken(token);
                        rfid.setTokendate(new Date());
                        rfidConfigMapper.updateById(rfid);
                        log.info("2.统一门户配置已经找到,并且token已经获取.");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("配置连接异常,token获取失败,请查看连接是否通畅!");
        }

    }



}
