package com.cq.home.config.convert;

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
public class CustomDateFormatter implements Formatter<Date>{
	
	public static final String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"};
	
	public static final String formatPattern = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public String print(Date object, Locale locale) {
		return DateFormatUtils.format(object, formatPattern);
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		return DateUtils.parseDate(text, parsePatterns);
	}


}
