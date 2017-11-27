package com.cq.home.exception;

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
	
	private String msg;


	public BizException(String code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public BizException(String code, String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
