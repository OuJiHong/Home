package com.cq.home.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author OJH
 *
 */
@Component
public class SpringUtils implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
	}
	
	
	/**
	 * 获取上下文环境对象
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return SpringUtils.applicationContext;
	}
	
	/**
	 * 获取国际化资源消息
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getMessage(String code, Object... args){
		return SpringUtils.applicationContext.getMessage(code, args, LocaleContextHolder.getLocale());
	}

	
	/**
	 * 获取当前请求对象
	 * @return
	 */
	public static HttpServletRequest currentRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return requestAttributes.getRequest();
	}
	
}
