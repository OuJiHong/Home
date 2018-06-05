package com.cq.home.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 日志处理,经过测试，必须有aspectj支持才行
 * @author 欧集红 
 * @Date 2018年4月25日
 * @version 1.0
 * 
 */
public class LogInterceptor implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println(System.currentTimeMillis() + "日志记录了...");
		return invocation.proceed();
	}

}
