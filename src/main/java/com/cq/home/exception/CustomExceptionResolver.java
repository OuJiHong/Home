package com.cq.home.exception;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cq.home.bean.core.Message;
import com.cq.home.util.JsonUtils;


/**
 *
 *异常处理服务
 * @author Administrator
 * 2018年4月14日 下午3:50:21
 *
 */
@Component
public class CustomExceptionResolver extends SimpleMappingExceptionResolver{
	
	private static Log logger = LogFactory.getLog(CustomExceptionResolver.class);
	
	
	public CustomExceptionResolver() {
		//设置高优先级
		setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
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
			request.setAttribute("message", message);
			return super.doResolveException(request, response, handler, ex);
		}else{
			JsonUtils.responseJson(response, message);
			modelAndView = new ModelAndView();//返回一个空视图
		}
		
		return modelAndView;
		
	}
	
}
