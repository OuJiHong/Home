package com.cq.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@ComponentScan(basePackages="com.cq.home")//SpringBootApplication已经使用默认扫描，可以省略
//@EnableAspectJAutoProxy//已经默认启用
@PropertySource(value= {"file:${user.home}/jdbc.properties"})
public class Main {
	
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	/**
	 * 启动服务
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Spring Boot 服务启动 " );
		SpringApplication.run(Main.class, args);
	}
	
}