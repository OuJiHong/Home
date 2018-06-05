package com.cq.home.view.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注为excel字段
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelField {
	
	/**
	 * 可选，对应标题
	 * @return
	 */
	String value() default "";
	
}
