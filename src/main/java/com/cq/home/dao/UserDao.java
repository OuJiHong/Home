package com.cq.home.dao;

import com.cq.home.bean.User;

/**
 * 用户信息查询
 * @author OJH
 *
 */
public interface UserDao extends BaseDao<User>{
	
	/**
	 * 通过用户id查询用户信息
	 * @param userId
	 * @return
	 */
	public User find(Long userId);
	
	/**
	 * 通过用户名查询用户信息
	 * @param userId
	 * @return
	 */
	public User findByName(String userName);
	
	/**
	 * 通过用户名和类型查询用户信息
	 * @param userName
	 * @param type
	 * @return
	 */
	public User findByNameAndType(String userName, User.Type type);
	
	
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
