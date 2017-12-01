package com.cq.home.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.lang.ArrayUtils;

import com.cq.home.validator.annotation.LimitKeyword.LimitKeywordConstraint;

/**
 *限制关键词
 * @author OJH
 * 2017年12月1日 下午4:46:52
 *
 */
@Documented
@Constraint(validatedBy=LimitKeywordConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD , ElementType.METHOD})
public @interface LimitKeyword {
	
	String message() default "不能包含敏感关键词";
	
	Class<?>[] groups() default {};
	

	Class<? extends Payload>[] payload() default { };
	
	/**
	 *校验实现
	 * @author OJH
	 * 2017年12月1日 下午5:00:53
	 *
	 */
	public static class LimitKeywordConstraint implements ConstraintValidator<LimitKeyword, String>{
		
		String[] shieldWords = new String[]{"营销", "特殊", "射击"};
		
		@Override
		public void initialize(LimitKeyword constraintAnnotation) {
			
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			
			if(value != null && ArrayUtils.contains(shieldWords, value)){
				return false;
			}
			
			return true;
		}
		
	}
	
	
}
