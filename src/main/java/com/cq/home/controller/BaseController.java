package com.cq.home.controller;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.cq.home.config.convert.CustomDateEditor;
import com.cq.home.config.convert.CustomStringEditor;

/**
 * 基础控制器
 * @author OJH
 *
 */
public abstract class BaseController {
	
	/**
	 *通用日志记录器 
	 */
	protected Log logger = LogFactory.getLog(getClass());
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		//指定字符串编辑和日期编辑
		webDataBinder.registerCustomEditor(String.class, new CustomStringEditor());
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor());
	}
	
	
}
