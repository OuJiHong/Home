package com.cq.home.exception;

import com.cq.home.util.SpringUtils;

/**
 * 业务异常,必须是RuntimeException，因为默认的事务回滚是要抛出运行时异常
 * @author OJH
 *
 */
public class BizException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private Object[] args;


	public BizException(String code, Object... args) {
		super(code);
		this.code = code;
		this.args = args;
	}
	
	public BizException(String code,Throwable cause, Object... args) {
		super(code, cause);
		this.code = code;
		this.args = args;
	}

	
	/* (non-Javadoc)
	 * 返回本地化消息
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		return SpringUtils.getMessage(code, args);
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
