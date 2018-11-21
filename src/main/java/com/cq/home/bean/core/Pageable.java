package com.cq.home.bean.core;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页对象
 * @author OJH
 *
 */
@ApiModel(value="分页对象")
public class Pageable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 分页插件约定参数
	 */
	@ApiModelProperty("分页大小")
	private Integer pageSize = 10;
	
	/**
	 * 分页插件约定参数
	 * 
	 */
	@ApiModelProperty("当前页码")
	private Integer pageNum = 1;

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 使用字符串兼容设置
	 * @param pageSize
	 */
	public void setPageSize(String pageSize) {
		if(NumberUtils.isDigits(pageSize)){
			this.pageSize = NumberUtils.toInt(pageSize);
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	/**
	 * 使用字符串兼容设置
	 * @param pageNum
	 */
	public void setPageNum(String pageNum) {
		if(NumberUtils.isDigits(pageNum)){
			this.pageNum = NumberUtils.toInt(pageNum);
		}
		
	}

	
	
}
