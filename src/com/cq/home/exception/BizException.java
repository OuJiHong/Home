package com.cq.home.exception;

import com.cq.home.util.SpringUtils;

/**
 * 业务异常
 * @author OJH
 *
 */
public class BizException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private Object[] args;


	public BizException(String code, Object... args) {
		super(SpringUtils.getMessage(code));
		this.code = code;
		this.args = args;
	}
	
	public BizException(String code,Throwable cause, Object... args) {
		super(SpringUtils.getMessage(code), cause);
		this.code = code;
		this.args = args;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}


	
	
}
