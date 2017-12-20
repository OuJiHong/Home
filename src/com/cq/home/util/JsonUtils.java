package com.cq.home.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具
 * @author OJH
 *
 */
public abstract class JsonUtils {
	
	private static final Log log = LogFactory.getLog(JsonUtils.class);
	
	/**
	 * 
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj){
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("toJson error", e);
		}
		
		return null;
	}
	
	
	/**
	 * @param content
	 * @param valueType
	 * @return
	 */
	public static <T> T convertToObject(String content, Class<T> valueType){
		try {
			return objectMapper.readValue(content, valueType);
		} catch (Exception e) {
			log.error("convertToObject error", e);
		} 
		
		return null;
	}
	
	/**
	 * @param response
	 * @param content
	 */
	public static void responseJson(HttpServletResponse response, Object obj){
		String jsonStr = toJson(obj);
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jsonStr);
		} catch (IOException e) {
			log.error("responseJson error", e);
		}
		
	}
	
}
