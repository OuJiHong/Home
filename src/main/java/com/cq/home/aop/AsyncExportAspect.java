package com.cq.home.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 *针对注解AsyncExport的切面配置
 * @author Administrator
 * 2018年10月24日 下午11:07:53
 *
 */
@Component
@Aspect
public class AsyncExportAspect {
	
	private Logger logger = LoggerFactory.getLogger(AsyncExportAspect.class);
	
	
	/**
	 * 
	 * 通知参数指定,通过指示符@annotation()指定了注解@AsyncExport参数
	 * @return
	 */
	@Around(value="@annotation(asyncExport)")
	public Object around(ProceedingJoinPoint joinPoint, AsyncExport asyncExport) throws Throwable{
		logger.info("进入异步导出通知:" + asyncExport);
		return joinPoint.proceed();
	}
	
}
