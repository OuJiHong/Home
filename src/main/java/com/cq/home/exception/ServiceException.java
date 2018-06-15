package com.cq.home.exception;

/**
 * 服务异常
 * @author 欧集红 
 * @Date 2018年6月15日
 * @version 1.0
 * 
 */
public class ServiceException extends BizException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(String code, Object... args) {
		super(code, args);
	}
	
	public ServiceException(String code,Throwable cause, Object... args) {
		super(code, cause, args);
	}

}
