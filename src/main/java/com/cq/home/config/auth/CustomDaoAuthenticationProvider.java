package com.cq.home.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cq.home.bean.core.Captcha.CaptchaType;
import com.cq.home.exception.InvalidCaptchaException;
import com.cq.home.service.CaptchaService;
import com.cq.home.util.SpringUtils;

@Service
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider{
	
	@Autowired
	private CaptchaService captchaService;
	
	@Autowired
	public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService) {
		setUserDetailsService(userDetailsService);//初始化
	}
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//附加验证码验证
		String captchaCode = SpringUtils.currentRequest().getParameter("captchaCode");
		if(StringUtils.isEmpty(captchaCode)) {
			throw new InvalidCaptchaException("验证码未找到");
		}
		
		if(!captchaService.isValid(CaptchaType.adminLoginType, captchaCode)) {
			throw new InvalidCaptchaException("验证码不正确");
		}
		
		return super.authenticate(authentication);
	}
	
	
	
}
