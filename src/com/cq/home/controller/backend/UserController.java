package com.cq.home.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Pageable;
import com.cq.home.controller.BaseController;
import com.cq.home.service.UserService;
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
	 * 获取总数量
	 * @return
	 */
	@RequestMapping("/count")
	public Long count(){
		return userService.count(null);
	}
	
}
