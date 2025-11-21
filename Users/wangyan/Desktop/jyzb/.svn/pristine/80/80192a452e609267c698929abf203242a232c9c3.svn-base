package com.jsdc.rfid.global;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vo.ResultInfo;

import java.sql.SQLException;

/**
 * 全局异常处理 
 * 
 * <p> @ControllerAdvice 可指定包前缀，例如：(basePackages = "com.pj.controller.admin")
 * @author kong
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** 全局异常拦截  */
	@ExceptionHandler
	public ResultInfo handlerException(Exception e) {

		// 打印堆栈，以供调试
		e.printStackTrace(); 


    	// 如果是未登录异常
		if(e instanceof NotLoginException){
			return ResultInfo.errorToken("未登录，请登录后再次访问");
		} 
		// 如果是权限异常
		else if(e instanceof NotPermissionException) {	
			NotPermissionException ee = (NotPermissionException) e;
			return ResultInfo.errorToken("无此权限");
		} 

		// 普通异常输出： 异常信息
		else {
			return ResultInfo.error(e.getMessage());
		}

    }
	
}
