package com.cq.home.config.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cq.home.bean.User;
import com.cq.home.dao.UserDao;


/**
 *
 *用户详情服务实现
 * @author Administrator
 * 2018年4月14日 下午1:31:56
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserDao  userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		User user = userDao.findByNameAndType(username, User.Type.admin);//查找管理员的用户信息
		
		if(user == null) {
			throw new UsernameNotFoundException("用户“"+username+"”未找到");
		}
		
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
		
		return userDetails;
	}

}
