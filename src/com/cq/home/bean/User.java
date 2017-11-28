package com.cq.home.bean;

/**
 * 用户类，测试用的
 * @author OJH
 *
 */
public class User extends BaseEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户类别
	 * @author OJH
	 *
	 */
	public enum Type{
		normal,
		advanced
	}
	
	
	private String name;
	
	private String password;
	
	private String phone;
	
	private Integer age;
	
	private Type type;
	
	private String detail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	
}
