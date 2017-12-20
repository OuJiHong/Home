package com.cq.home.controller.backend;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Message;
import com.cq.home.bean.core.Pageable;
import com.cq.home.controller.BaseController;
import com.cq.home.service.UserService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户控制器
 * @author OJH
 *
 */
@Api(tags="用户信息管理")
@RestController
@RequestMapping(value="/backend/user", produces="application/json")
public class UserController extends BaseController{
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * @return
	 */
	@ApiOperation("查询用户信息列表")
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public PageInfo<User> list(Pageable pageable){
		return userService.findPage(pageable);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@ApiOperation("添加用户信息")
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public Message add(@Valid User user) throws Exception{
		userService.add(user);
		return Message.success("添加成功");
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@ApiOperation("修改用户信息")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public Message update(User user) throws Exception{
		userService.update(user);
		return Message.success("修改成功");
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	@ApiOperation("查询用户总数量")
	@RequestMapping(value="/count", method=RequestMethod.GET)
	public Long count(){
		return userService.count(null);
	}
	
	
}
