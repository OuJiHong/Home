package com.cq.home.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * 配置国际化资源
 * @author OJH
 *
 */
@Component("messageSource")
public class MessageSourceFactoryBean implements FactoryBean<MessageSource>{

	@Override
	public MessageSource getObject() throws Exception {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/message");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	@Override
	public Class<?> getObjectType() {
		return MessageSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
