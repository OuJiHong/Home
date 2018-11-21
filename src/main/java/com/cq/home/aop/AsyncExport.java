package com.cq.home.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 异步导出
 * @author Administrator
 * 2018年10月24日 下午11:05:53
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface AsyncExport {
	
	/**
	 * 任务名称
	 * @return
	 */
	String value();
}
