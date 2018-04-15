package com.cq.home.bean.core;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.cq.home.bean.User;

/**
 *
 *包含本地用户信息
 * @author Administrator
 * 2018年4月15日 下午4:00:43
 *
 */
public class UserLocalDetails extends org.springframework.security.core.userdetails.User{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *本地用户对象 
	 */
	private User user;
	
	
	public UserLocalDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities) ;
	}
	
	
	public UserLocalDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
}
