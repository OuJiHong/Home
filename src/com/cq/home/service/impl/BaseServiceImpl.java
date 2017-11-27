package com.cq.home.service.impl;

import java.util.List;
import java.util.Map;

import com.cq.home.bean.core.Pageable;
import com.cq.home.dao.BaseDao;
import com.cq.home.service.BaseService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 基础服务
 * @author OJH
 *
 */
public abstract class BaseServiceImpl<T> implements BaseService<T>{
	
	/**
	 * 以此方法限定必须获取的dao
	 * @return
	 */
	public abstract BaseDao<T> getBaseDao();
	
	@Override
	public List<T> findList(Object params) {
		return getBaseDao().findList(params);
	}

	@Override
	public PageInfo<T> findPage(Pageable pageable) {
		return findPage(pageable, null);
	}

	@Override
	public PageInfo<T> findPage(Pageable pageable, Object params) {
		PageHelper.startPage(pageable);
		List<T> list = findList(params);
		PageInfo<T> pageInfo = new PageInfo<T>(list);
		return pageInfo;
	}

	@Override
	public long count(final Object params) {
		long count = PageHelper.count(new ISelect() {
			
			@Override
			public void doSelect() {
				findList(params);
			}
			
		});
		
		return count;
	}
	
	
}
