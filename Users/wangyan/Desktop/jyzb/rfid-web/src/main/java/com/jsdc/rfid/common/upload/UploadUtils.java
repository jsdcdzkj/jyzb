package com.jsdc.rfid.common.upload;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadUtils {

	/**
	 * 日期格式化对象
	 */
	public static final DateFormat MONTH_FORMAT = new SimpleDateFormat("/yyyyMM/ddHHmmss");
	
	/**
	 * 创建路径
	 * @param dir
	 */
	public static void checkDirAndCreate(File dir) {
		if (!dir.exists())
			dir.mkdirs();
	}
	
	/**
	 * 生成文件名
	 * @param path
	 * @param ext
	 * @return
	 */
	public static String generateFilename(String path, String ext) {
		return path + MONTH_FORMAT.format(new Date()) + RandomStringUtils.random(4, Num62.N36_CHARS) + "." + ext;
	}
}
