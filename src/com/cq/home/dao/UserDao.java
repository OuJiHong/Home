package com.cq.home.dao;

import com.cq.home.bean.User;

/**
 * 用户信息查询
 * @author OJH
 *
 */
public interface UserDao extends BaseDao<User>{
	
	/**
	 * 添加数据
	 * @param user
	 * @return
	 */
	public int add(User user);
	
	/**
	 * 更新数据
	 * @param user
	 * @return
	 */
	public int update(User user);
	
}
