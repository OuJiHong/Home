package com.cq.home.service;

import com.cq.home.bean.core.Captcha;
import com.cq.home.bean.core.Captcha.CaptchaType;

/**
 *
 *验证码服务
 * @author Administrator
 * 2018年4月14日 下午12:20:32
 *
 */
public interface CaptchaService {
	
	
	/**
	 * 产生指定类型的验证码
	 * @param captchaType
	 * @return
	 */
	Captcha createCaptcha(CaptchaType captchaType);
	
	
	/**
	 * 验证是否有效
	 * @param captchaType
	 * @param code
	 * @return
	 */
	boolean isValid(CaptchaType captchaType, String code);
	
	
}
