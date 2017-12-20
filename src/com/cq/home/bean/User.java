package com.cq.home.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.cq.home.validator.annotation.LimitKeyword;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户类，测试用的
 * @author OJH
 *
 */
@ApiModel(value="用户信息模型")
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
	
	@ApiModelProperty(value="用户名",required=true)
	private String name;
	
	@ApiModelProperty(value="用户密码",required=true)
	private String password;
	
	@ApiModelProperty(value="用户手机号",required=true)
	private String phone;
	
	@ApiModelProperty(value="用户年龄")
	private Integer age;
	
	@ApiModelProperty(value="用户类型",required=true)
	private Type type;
	
	@ApiModelProperty(value="用户详情")
	private String detail;

	@NotEmpty
	@LimitKeyword
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotEmpty
	@Size(max=11)
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

	@NotNull
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
