package com.cq.home.controller.backend;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Message;
import com.cq.home.bean.core.Pageable;
import com.cq.home.controller.BaseController;
import com.cq.home.service.UserService;
import com.cq.home.validator.UserValidator;
import com.github.pagehelper.PageInfo;

/**
 * 用户控制器
 * @author OJH
 *
 */
@RestController
@RequestMapping("/backend/user")
public class UserController extends BaseController{
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * @return
	 */
	@RequestMapping("/list")
	public PageInfo<User> findPage(Pageable pageable){
		return userService.findPage(pageable);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Message add(@Valid User user) throws Exception{
		userService.add(user);
		return Message.success("添加成功");
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Message update(User user) throws Exception{
		userService.update(user);
		return Message.success("修改成功");
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	@RequestMapping("/count")
	public Long count(){
		return userService.count(null);
	}
	
	
	@InitBinder
	protected void initUserValidator(WebDataBinder webDataBinder){
		//局部绑定，每个请求都会调用一个，不兼容的类型会报错
		UserValidator userValidator = new UserValidator();
		if(webDataBinder.getTarget() != null && userValidator.supports(webDataBinder.getTarget().getClass())){
			webDataBinder.setValidator(userValidator);
		}
		
	}
	
}
