package com.cq.home.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cq.home.bean.Msg;

public class MsgValidator  implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		if(Msg.class.isAssignableFrom(clazz)){
			return true;
		}
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Msg msg = (Msg) target;
		
		if(msg.getTitle() == null){
			errors.reject("title", "标题不能为空");
		}
		
		
	}
	
	

}
