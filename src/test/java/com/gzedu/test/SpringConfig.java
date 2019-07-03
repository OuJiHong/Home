package com.gzedu.test;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.SystemPropertyUtils;

/**
 * 基于注解的配置
 * 
 * @author 欧集红 
 * @Date 2018年4月25日
 * @version 1.0
 * 
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
public class SpringConfig {
	
	public static final String SYS_JDBC_DRIVER= "${sys.jdbc.driver}";
	
	public static final String SYS_JDBC_URL= "${sys.jdbc.url}";
	
	public static final String SYS_JDBC_USERNAME = "${sys.jdbc.username}";
	
	public static final String SYS_JDBC_PASSWORD = "${sys.jdbc.password}";
	
	/**
	 * 创建数据源
	 * 
	 * oracle.jdbc.driver.OracleDriver
	 * 
	 * @return
	 */
	@Bean
	public DataSource datasource() {
		//由环境提供配置
		String driver = SystemPropertyUtils.resolvePlaceholders(SYS_JDBC_DRIVER);
		String url = SystemPropertyUtils.resolvePlaceholders(SYS_JDBC_URL);
		String username = SystemPropertyUtils.resolvePlaceholders(SYS_JDBC_USERNAME);
		String password = SystemPropertyUtils.resolvePlaceholders(SYS_JDBC_PASSWORD);
		SingleConnectionDataSource datasource = new SingleConnectionDataSource(url, username, password, true);
		datasource.setDriverClassName(driver);
		return datasource;
		
	}
	
	/**
	 * 构建entity
	 * @return
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource datasource) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		/**
		 *  classpath：只会到你的class路径中查找找文件。
		 *	classpath*：不仅包含class路径，还包括jar文件中（class路径）进行查找。
		 */
		factoryBean.setPersistenceXmlLocation("classpath:/persistence.xml");
		
		HibernateJpaVendorAdapter hibernateVenderAdapter = new HibernateJpaVendorAdapter();
		hibernateVenderAdapter.setDatabase(Database.ORACLE);
		hibernateVenderAdapter.setShowSql(true);
		hibernateVenderAdapter.setGenerateDdl(false);
		factoryBean.setJpaVendorAdapter(hibernateVenderAdapter);
		
		factoryBean.setDataSource(datasource);
		return factoryBean;
	}
	
	/**
	 * 构建事务管理
	 * @return
	 */
	@Bean
	public PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(emf);
		return jpaTransactionManager;
	}
	
	
	
}
