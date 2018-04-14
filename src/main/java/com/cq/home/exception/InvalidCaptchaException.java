package com.cq.home.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 *无效验证码
 * @author Administrator
 * 2018年4月14日 下午1:47:28
 *
 */
public class InvalidCaptchaException extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCaptchaException(String msg) {
		super(msg);
	}

}
