package com.gzedu.test.service;

import java.util.List;
import java.util.Map;

public interface DaoService {
	
	/**
	 * 更新数据
	 * @param sql
	 * @param params
	 */
	public int updateData(String sql, Map<String,Object> params);
	
	/**
	 * 查询列表
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map> findListForMap(String sql, Map<String,Object> params);
	
}
