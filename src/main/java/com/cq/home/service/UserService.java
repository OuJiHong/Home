package com.cq.home.service;

import org.apache.ibatis.annotations.Param;

import com.cq.home.bean.User;
import com.cq.home.exception.BizException;

/**
 * 用户查询
 * @author OJH
 *
 * @param <T>
 */
public interface UserService extends BaseService<User>{
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void add(User user);
	
	/**
	 * 修改用户
	 * @param user
	 */
	public void update(User user);
	
	/**
	 * 通过用户名和类型查询用户信息
	 * @param userName
	 * @param type
	 * @return
	 */
	public User findByNameAndType(String userName,  User.Type type);
	
}
