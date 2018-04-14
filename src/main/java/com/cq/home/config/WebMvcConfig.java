package com.cq.home.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 *mvc配置
 * @author Administrator
 * 2018年4月14日 下午5:36:08
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
			converters.add(new BufferedImageHttpMessageConverter());//图片转换支持
		}
}
