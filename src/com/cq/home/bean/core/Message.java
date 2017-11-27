package com.cq.home.bean.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一消息数据交互
 * @author OJH
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type{
		success,
		failed;
	}
	
	private Type type;
	
	private String msg;

	/**
	 *附加数据 
	 */
	private Map<String,Object> data = new LinkedHashMap<String,Object>();
	
	public Message(Type type, String msg) {
		super();
		this.type = type;
		this.msg = msg;
	}


	public static Message success(String msg){
		return new Message(Type.success, msg);
	}
	
	public static Message failed(String msg){
		return new Message(Type.failed, msg);
	}
	
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Map<String, Object> getData() {
		return data;
	}


	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	
	/**
	 * 添加属性
	 * @param key
	 * @param value
	 */
	public Message addData(String key, Object value){
		getData().put(key, value);
		return this;
	}
	
}
