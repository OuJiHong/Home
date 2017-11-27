package com.cq.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cq.home.bean.User;
import com.cq.home.dao.BaseDao;
import com.cq.home.dao.UserDao;
import com.cq.home.service.UserService;

/**
 * @author OJH
 *
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	
	@Autowired
	public UserDao userDao;
	
	@Override
	public BaseDao<User> getBaseDao() {
		return userDao;
	}
	
}
