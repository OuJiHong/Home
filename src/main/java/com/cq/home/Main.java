package com.cq.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@ComponentScan(basePackages="com.cq.home")//SpringBootApplication已经使用默认扫描，可以省略
//@EnableAspectJAutoProxy//已经默认启用
public class Main {
	
	/**
	 * 启动服务
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Spring Boot 服务启动...");
		SpringApplication.run(Main.class, args);
	}
	
}
