package com.cq.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.cq.home.config.auth.CustomDaoAuthenticationProvider;

/**
 *
 *用户认证控制
 * @author Administrator
 * 2018年4月14日 下午3:16:00
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//默认参数名username,password, 登录请求post方法的/login, 失敗url /login?error
		http.authorizeRequests().antMatchers("/admin/getCaptcha").permitAll();
		
		http.authorizeRequests().antMatchers("/admin/**").authenticated();
		http.formLogin().loginPage("/admin/login").defaultSuccessUrl("/admin/index").permitAll();
		
		http.logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/logout?logout")
			.invalidateHttpSession(true).permitAll();
		
		
		http.headers().frameOptions().disable();
		
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customDaoAuthenticationProvider);
	}
	
	
}
