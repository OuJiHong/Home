package com.cq.home.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class TomcatConfig {
	
	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {
	        @Override 
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	            container.setSessionTimeout(10, TimeUnit.MINUTES);
	            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,  "/404"));
	            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,  "/500"));
	        }
	    };
	}

	
}
