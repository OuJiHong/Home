package com.gzedu.test.service.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.test.service.DaoService;

/**
 * 实现类
 * @author 欧集红 
 * @Date 2018年6月5日
 * @version 1.0
 * 
 */
@Service
public class DaoServiceImpl implements DaoService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public int updateData(String sql, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		if(params != null) {
			for(Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	@Override
	public List<Map> findListForMap(String sql, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		if(params != null) {
			for(Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		//这里写死，限制数量，防止数量过多
		query.setMaxResults(100);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}

}
