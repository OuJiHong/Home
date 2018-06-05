package com.gzedu.test.bean;

public class DemoBean {
	
	/**
	 * 
	 */
	private String Name;

	private String address;
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "DemoBean [Name=" + Name + ", address=" + address + "]";
	}

	
	
	
	
	
	
	
}
