package com.cq.home.service;

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
	public void add(User user) throws BizException;
	
	/**
	 * 修改用户
	 * @param user
	 */
	public void update(User user) throws BizException;
}
