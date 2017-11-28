package com.cq.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cq.home.bean.User;
import com.cq.home.dao.BaseDao;
import com.cq.home.dao.UserDao;
import com.cq.home.exception.BizException;
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

	@Override
	public void add(User user) throws BizException{
		int effective = userDao.add(user);
		if(effective != 1){
			throw new BizException("user.add.error");
		}
	}

	@Override
	public void update(User user) throws BizException {
		int effective = userDao.update(user);
		if(effective != 1){
			throw new BizException("user.update.error");
		}
		
	}
	
}
