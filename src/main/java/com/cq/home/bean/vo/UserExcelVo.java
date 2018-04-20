package com.cq.home.bean.vo;

import java.util.Date;

import com.cq.home.view.excel.ExcelField;

/**
 *用户数据表格视图
 * @author Administrator
 * 2018年4月20日 下午11:24:14
 *
 */
public class UserExcelVo {

	@ExcelField("用户名")
	private String name;
	
	@ExcelField("用户密码")
	private String password;
	
	@ExcelField("用户手机号")
	private String phone;
	
	@ExcelField("用户年龄")
	private Integer age;
	
	@ExcelField("用户类型")
	private String type;
	
	@ExcelField("用户详情")
	private String detail;

	@ExcelField("用户ID")
	private Long  id;
	
	@ExcelField("创建日期")
	private Date createDate;
	
	@ExcelField("修改日期")
	private Date modifyDate;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "UserExcelVo [name=" + name + ", password=" + password + ", phone=" + phone + ", age=" + age + ", type="
				+ type + ", detail=" + detail + ", id=" + id + ", createDate=" + createDate + ", modifyDate="
				+ modifyDate + "]";
	}
	
	
	
	
}
