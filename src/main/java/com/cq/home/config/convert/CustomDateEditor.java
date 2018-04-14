package com.cq.home.config.convert;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.format.Formatter;

/**
 * 日期转换
 * @author OJH
 *
 */
public class CustomDateEditor extends PropertyEditorSupport{
	
	public static final String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"};
	
	public static final String formatPattern = "yyyy-MM-dd HH:mm:ss";
	
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text == null) {
			setValue(null);
			return;
		}
		
		try {
			Date date = DateUtils.parseDate(text, parsePatterns);
			setValue(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
	

}
