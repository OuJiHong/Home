package com.cq.home.dao;

import java.util.List;

/**
 * 基础数据存取接口
 * @author OJH
 *
 * @param <T>
 */
public interface BaseDao<T> {
	
	/**
	 * 查询列表
	 * @param params
	 * @return
	 */
	public List<T> findList(Object params);
}
