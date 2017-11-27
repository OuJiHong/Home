package com.cq.home.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages="com.cq.home")
@EnableScheduling
//@EnableAspectJAutoProxy
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
