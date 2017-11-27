package com.cq.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用错误跳转
 * @author OJH
 *
 */
@Controller
@RequestMapping("/")
public class ErrorController extends BaseController{
	
	
	/**
	 * 页面未找到
	 * @return
	 */
	@RequestMapping("/404")
	public String pageNotFound(){
		return "/404";
	}
	
	/**
	 * 内部服务错误
	 * @return
	 */
	@RequestMapping("500")
	public String internalServerError(){
		return "/500";
	}
	
}
