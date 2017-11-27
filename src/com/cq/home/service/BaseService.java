package com.cq.home.service;

import java.util.List;

import com.cq.home.bean.core.Pageable;
import com.github.pagehelper.PageInfo;

/**
 * 基础分页接口
 * @author OJH
 *
 */
public interface BaseService<T> {
	
	/**
	 * 查询所有
	 * @param params
	 * @return
	 */
	public List<T> findList(Object params);
	
	/**
	 * 分页调用
	 * @param pageable
	 * @return
	 */
	public PageInfo<T> findPage(Pageable pageable);
	
	/**
	 * 分页调用
	 * @param pageable
	 * @return
	 */
	public PageInfo<T> findPage(Pageable pageable, Object params);
	 
	/**
	 * 记录数量
	 * @param params
	 * @return
	 */
	public long count(Object params);
	
	
}
