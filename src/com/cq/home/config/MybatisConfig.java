package com.cq.home.config;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.cq.home.sundry.ProxySqlSessionTemplate;

@Configuration
public class MybatisConfig {
	
	/**
	 * 暂时只能创建一个数据源到容器中，防止springBoot的自动初始化操作失败DataSourceInitializer
	 * 写数据源
	 * @return
	 */
	@Bean(name="writeDataSource")
	public DataSource createWriteDataSource(){
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://115.159.161.236:3306/home?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		dataSource.setUsername("ojh");
		dataSource.setPassword("123456");
		return dataSource;
	}
	
	/**
	 * 读数据源,不创建实例到容器中
	 * @return
	 */
	private DataSource createReadDataSource(){
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://115.159.161.236:3306/home?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		dataSource.setUsername("ojh");
		dataSource.setPassword("123456");
		return dataSource;
	}
	
	/**
	 * 可写的sql会话工厂
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name="writeSqlSessionFactory")
	public SqlSessionFactory createWriteSqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSource);//使用的是当前datasource
		return sqlSessionFactoryBean.getObject();
	}
	
	/**
	 * 可读的sql会话工厂
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name="readSqlSessionFactory")
	public SqlSessionFactory createReadSqlSessionFactory() throws Exception{
		DataSource readDataSource = createReadDataSource();//创建一次
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(readDataSource);//使用的是当前datasource
		return sqlSessionFactoryBean.getObject();
	}
	
	/**
	 * 实现读写分离
	 * 可通过构建一个sqlSessionTemplate的代理类，实现读写分离，查询方法调用读的数据库，写方法调用写的数据库
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean(name="proxySqlSessionTemplate")
	public SqlSessionTemplate createSqlSession(@Qualifier("writeSqlSessionFactory") SqlSessionFactory writeSqlSessionFactory,
			@Qualifier("readSqlSessionFactory") SqlSessionFactory readSqlSessionFactory){
		SqlSessionTemplate writeSqlSessionTemplate = new SqlSessionTemplate(writeSqlSessionFactory);
		ProxySqlSessionTemplate proxySqlSessionTemplate = new ProxySqlSessionTemplate(readSqlSessionFactory, writeSqlSessionTemplate);
		return proxySqlSessionTemplate;
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
		mapperScannerConfigurer.setSqlSessionTemplateBeanName("proxySqlSessionTemplate");
		mapperScannerConfigurer.setBasePackage("com.cq.home.dao");
		return mapperScannerConfigurer;
	}
	
}
