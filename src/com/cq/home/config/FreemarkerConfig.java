package com.cq.home.config;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.cq.home.freemarker.directive.UserListDirective;
import com.cq.home.freemarker.method.CurrencyMethod;

import freemarker.template.TemplateException;

@Configuration
public class FreemarkerConfig{
	
	private static final Log logger = LogFactory.getLog(FreemarkerConfig.class);
	
	@Autowired
	private freemarker.template.Configuration configuration;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private CurrencyMethod currencMethod;
	
	@Autowired
	private UserListDirective userListDirective;
	
	/**
	 * 默认显示图片
	 */
	@Value("${home.defaultImage}")
	private String defaultImage;
	
	@PostConstruct
	public void setSharedVariable(){
		try {
			String base = servletContext.getContextPath();
			configuration.setSharedVariable("base", base);
			configuration.setSharedVariable("currency", currencMethod);
			configuration.setSharedVariable("userList", userListDirective);
			configuration.setSharedVariable("defaultImage", base + StringUtils.cleanPath(defaultImage));
		} catch (TemplateException e) {
			logger.error(e);
		}
	}


	
}
