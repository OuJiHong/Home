package com.cq.home.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cq.home.bean.User;

/**
 * 自定义敏感词验证
 * @author OJH
 *
 */
public class UserValidator  implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		if(User.class.isAssignableFrom(clazz)){
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if(user.getName() == null){
			errors.reject("name", "用户名不能为空");
		}
		
		if(user.getPassword() == null){
			errors.reject("password", "密码不能为空");
		}
		
		if(user.getType() == null){
			errors.reject("type", "用户类型必须指定");
		}
		
	}
	
	

}
