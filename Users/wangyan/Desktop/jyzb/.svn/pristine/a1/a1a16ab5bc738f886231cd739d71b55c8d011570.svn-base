package com.jsdc.rfid.common.aop.logaop;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.rfid.model.SysLog;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.SysLogService;
import com.jsdc.rfid.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vo.ResultInfo;

import java.util.Date;

/**
 * @ClassName LogAspect
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/4 11:48
 * @Version 1.0
 */

@Aspect
@Component
public class LogAspect {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysLogService logService;

    @Pointcut(value = "@annotation(com.jsdc.rfid.common.aop.logaop.LogInfo)")
    public void logPointcut(){

    }

    @AfterReturning(value = "logPointcut()",returning = "resultInfo")
    public void saveLoginfo(JoinPoint joinPoint, ResultInfo resultInfo) throws Exception {
        if(resultInfo.getCode() == 0 && StringUtils.isNotEmpty(resultInfo.getLogMsg())){
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            LogInfo annotation = signature.getMethod().getAnnotation(LogInfo.class);
            SysLog sysLog = new SysLog();
            SysUser sysUser = sysUserService.getUser();
            sysLog.setContent(resultInfo.getLogMsg());
            sysLog.setOperate_time(new Date());
            sysLog.setOperate_type(annotation.value().getDesc());
            if(StringUtils.equals(annotation.value().getValue(), "1")){
                if(((JSONObject) resultInfo.getData()).getString("success").equals("true")){
                    sysLog.setUser_id(sysUser.getId());
                }
            }
            if(null != sysUser){
                sysLog.setUser_name(sysUser.getUser_name());
            }
            logService.insert(sysLog);
        }

    }
}
