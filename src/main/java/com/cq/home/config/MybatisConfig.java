package com.cq.home.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MybatisConfig {
	

	/**
	 * 创建sqlSession工厂实体
	 * @return
	 */
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean.getObject();
	}
	
	
	/**
	 * 单独创建mapper实例
	 * @param sqlSession
	 * @return
	 * @throws Exception
	 */
	/*@Bean
	public MsgDao createMsgDao(SqlSessionTemplate sqlSession) throws Exception{
		MapperFactoryBean<MsgDao> mapperFactoryBean = new MapperFactoryBean<MsgDao>();
		mapperFactoryBean.setSqlSessionTemplate(sqlSession);
		mapperFactoryBean.setMapperInterface(MsgDao.class);
		return mapperFactoryBean.getObject();
	}*/
	
	/**
	 * 自动扫描mapper接口,不能提前加载，会有properties属性注入不了的问题
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer createMapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.cq.home.dao");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return mapperScannerConfigurer;
	}
	
}
