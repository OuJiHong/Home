package com.cq.home.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

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
//		webDataBinder.setValidator(new MsgValidator());
	}
	
	
}
