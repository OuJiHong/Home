package com.cq.home.aop;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SqlTimeLog {
	
	private static final Log log = LogFactory.getLog(SqlTimeLog.class);
	
	
	@Pointcut("execution(* com.cq.home.dao.*.*(..))")
	public void pointcut(){
		
	}
	
	@Before("pointcut()")
	public void before(JoinPoint joinPoint){
		log.info(joinPoint.toLongString() + "-sql执行开始时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	@After("pointcut()")
	public void after(JoinPoint joinPoint){
		log.info(joinPoint.toLongString() + "-sql执行结束时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
}
