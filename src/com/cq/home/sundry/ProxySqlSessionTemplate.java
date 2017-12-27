package com.cq.home.sundry;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

/**
 *
 *读写分离
 * @author OJH
 * 2017年12月27日 上午11:11:23
 *
 */
public class ProxySqlSessionTemplate extends SqlSessionTemplate{
	
	/**
	 * 可写配置
	 */
	private SqlSessionTemplate writeSqlSessionTemplate;
	
	/**
	 * 默认是可读配置
	 * @param sqlSessionFactory
	 */
	public ProxySqlSessionTemplate(SqlSessionFactory readSqlSessionFactory, SqlSessionTemplate writeSqlSessionTemplate) {
		super(readSqlSessionFactory);
		
		this.writeSqlSessionTemplate = writeSqlSessionTemplate;
	}


	
	@Override
	public int insert(String statement) {
		return writeSqlSessionTemplate.insert(statement);
	}
	
	@Override
	public int insert(String statement, Object parameter) {
		return writeSqlSessionTemplate.insert(statement, parameter);
	}
	
	@Override
	public int update(String statement) {
		return writeSqlSessionTemplate.update(statement);
	}
	
	@Override
	public int update(String statement, Object parameter) {
		return writeSqlSessionTemplate.update(statement, parameter);
	}
	
	@Override
	public int delete(String statement) {
		return writeSqlSessionTemplate.delete(statement);
	}
	
	@Override
	public int delete(String statement, Object parameter) {
		return writeSqlSessionTemplate.delete(statement, parameter);
	}
	
	
	
}
