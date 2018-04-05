package com.cq.home.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.cq.home.bean.core.Message;
import com.cq.home.util.JsonUtils;

@ControllerAdvice
public class CustomExceptionHandler{
	
	private static Log logger = LogFactory.getLog(CustomExceptionHandler.class);
	
	/**
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView customeHandleException(Exception ex, HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = null;
		
		String errorCode = UUID.randomUUID().toString();
		Message message  = Message.failed("系统内部错误，请联系管理员" + "("+errorCode+")");
		if(ex instanceof BindException){
			logger.error("数据校验异常:" + ex.getMessage());
			BindException bindException = (BindException)ex;
			if(bindException.getFieldError() != null){
				FieldError fieldError = bindException.getFieldError();
				message.setMsg(fieldError.getField() + "字段校验失败  - " + fieldError.getDefaultMessage());
			}else{
				message.setMsg("数据校验失败  - " + bindException.getGlobalError().getDefaultMessage());
			}
		}else if(ex instanceof BizException){
			BizException bizException = (BizException)ex;
			message.setMsg(bizException.getLocalizedMessage());
			logger.error("业务逻辑异常:" + bizException.getCode() +  "(" + bizException.getLocalizedMessage() + ")");
		}else{
			logger.error("系统内部错误 - " + errorCode, ex);
		}
		
		
		//动态响应数据或视图,不判断handler的注解标识
		String accept = request.getHeader("accept");
		if(accept.indexOf("text/html") != -1 || accept.indexOf("text/plain") != -1){
			modelAndView = new ModelAndView("500");
			modelAndView.addObject("message", message);
		}else{
			JsonUtils.responseJson(response, message);
		}
		
		return modelAndView;
	}
	
	
}
