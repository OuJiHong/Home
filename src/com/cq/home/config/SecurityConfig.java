package com.cq.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.cq.home.config.auth.CustomDaoAuthenticationProvider;

/**
 *
 *用户认证控制
 * @author Administrator
 * 2018年4月14日 下午3:16:00
 *
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//默认参数名username,password, 登录请求post方法的/login, 失敗url /login?error
		http.authorizeRequests().antMatchers("/admin/getCaptcha").permitAll();
		
		http
			.authorizeRequests().antMatchers("/admin/**").authenticated().and()
			.formLogin().loginPage("/admin/login").defaultSuccessUrl("/admin/index").permitAll().and()
			.httpBasic();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customDaoAuthenticationProvider);
	}
	
	
}
