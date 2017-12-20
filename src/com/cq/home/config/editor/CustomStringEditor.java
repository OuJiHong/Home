package com.cq.home.config.editor;

import java.beans.PropertyEditorSupport;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

/**
 *自定义属性编辑
 * @author OJH
 * 2017年12月20日 上午9:52:53
 *
 */
public class CustomStringEditor extends PropertyEditorSupport{
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		
		if(StringUtils.isEmpty(text)){
			super.setValue(null);//空字符设为null
		}else{
			String cleanValue = Jsoup.clean(text, Whitelist.none());
			super.setValue(cleanValue);
		}
		
	}
	
	
}
