package com.cq.home.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Message;
import com.cq.home.bean.core.Pageable;
import com.cq.home.controller.BaseController;
import com.cq.home.service.UserService;
import com.github.pagehelper.PageInfo;

/**
 * 用户控制器
 * @author OJH
 *
 */
@Controller
@RequestMapping(value="/admin/user")
public class AdminUserController extends BaseController{
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public PageInfo<User> list(Pageable pageable){
		return userService.findPage(pageable);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Message add(@Valid User user) throws Exception{
		userService.add(user);
		return Message.success("添加成功");
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public Message update(User user) throws Exception{
		userService.update(user);
		return Message.success("修改成功");
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	@RequestMapping(value="/count", method=RequestMethod.GET)
	@ResponseBody
	public Long count(){
		return userService.count(null);
	}
	
	
}
