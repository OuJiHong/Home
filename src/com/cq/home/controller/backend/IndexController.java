package com.cq.home.controller.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cq.home.controller.BaseController;

/**
 *
 * 后端首页
 * @author Administrator
 * 2018年4月5日 下午3:25:07
 *
 */
@Controller
@RequestMapping("/backend")
public class IndexController extends BaseController{
	
	
	@RequestMapping(value= {"/index",""})
	public String index() {
		return "index";
	}
	
	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	
}
