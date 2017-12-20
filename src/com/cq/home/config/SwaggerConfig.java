package com.cq.home.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *swagger2配置
 * @author OJH
 * 2017年12月20日 上午9:38:55
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	/**
	 * 创建摘要信息
	 * @return
	 */
	@Bean
	public Docket createDefaultDocket(){
		ApiInfo apiInfo = new ApiInfo("后台管理系统API参考", "用于测试后台系统的接口", "1.0", "urn:tos", ApiInfo.DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<VendorExtension>());
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(apiInfo);
		return docket;
	}
	
}
