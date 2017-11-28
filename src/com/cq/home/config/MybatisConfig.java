package com.cq.home.config;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MybatisConfig {
	
	@Bean
	public DataSource createDataSource(){
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/home?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		dataSource.setUsername("ojh");
		dataSource.setPassword("123456");
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSource);//使用的是当前datasource
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate createSqlSession(SqlSessionFactory sqlSessionFactory){
		SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSession;
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
	 * 自动扫描mapper接口
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer createMapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.cq.home.dao");
		return mapperScannerConfigurer;
	}
	
}
