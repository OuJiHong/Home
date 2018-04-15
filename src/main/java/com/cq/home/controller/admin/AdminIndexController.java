package com.cq.home.controller.admin;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Captcha;
import com.cq.home.bean.core.Captcha.CaptchaType;
import com.cq.home.controller.BaseController;
import com.cq.home.exception.InvalidCaptchaException;
import com.cq.home.service.CaptchaService;

/**
 *
 * 后端首页
 * @author Administrator
 * 2018年4月5日 下午3:25:07
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController extends BaseController{
	

	@Autowired
	private CaptchaService captchaService;
	
	
	@RequestMapping(value= {"/index",""})
	public String index(Model model) {
		User user = currentUser();
		model.addAttribute("user", user);
		return "index";
	}
	
	
	/**
	 * 欢迎页面
	 * @return
	 */
	@RequestMapping(value="welcome")
	public String welcome() {
		return "welcome";
	}
	

	/**
	 * 获取验证码
	 * @return
	 */
	@RequestMapping(value="getCaptcha", produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public BufferedImage getCaptcha() {
		Captcha captcha = captchaService.createCaptcha(CaptchaType.adminLoginType);
		return captcha.getData();
	}
	
	
	/**
	 * 进入登录页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String login(Model model, HttpServletRequest request) {
		Exception exception = (AuthenticationException)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if(exception != null) {
			
			String errorMsg = "登录失败";
			if(exception instanceof InvalidCaptchaException) {
				errorMsg = "验证码不正确";
			}else if(exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
				errorMsg = "用户名或密码不正确";
			}
			model.addAttribute("errorMsg", errorMsg);
			
		}
		
		return "login";
	}
	
	
	/**
	 * 注销
	 * @return
	 */
	@RequestMapping("logout")
	public String logout(Model model, HttpServletRequest request){
		
		String logout = request.getParameter("logout");
		//是否存在参数
		if(logout != null) {
			model.addAttribute("logout", true);
		}
		
		return "logout";
	}
	
	
}
