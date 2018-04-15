package com.cq.home.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MybatisConfig {
	
	/**
	 * 数据配置文件
	 */
	private String dbcpPropName = "dbcp.properties";
	
	
	/**
	 * 在DataSourceAutoConfiguration中的DataSourceProperties会出现空指针，不知道什么情况。springBoot版本1.2系列
	 * 所以自定义数据源
	 */
	@Bean
	public DataSource dataSource() throws Exception{
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(dbcpPropName));
		return BasicDataSourceFactory.createDataSource(properties);
	}

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
	 * 自动扫描mapper接口
	 * @return
	 */
	@Bean
	@DependsOn("sqlSessionFactory")
	public MapperScannerConfigurer createMapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.cq.home.dao");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return mapperScannerConfigurer;
	}
	
}
