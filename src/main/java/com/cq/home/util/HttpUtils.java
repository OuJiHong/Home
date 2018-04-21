package com.cq.home.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 *http请求工具
 * @author Administrator
 * 2018年4月21日 下午12:16:04
 *
 */
public class HttpUtils {
	
	private static HttpClient httpClient;
	
	private static String defaultEncoding = "UTF-8";//默认编码 
	
	static {
		//初始化httpClient
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		requestConfigBuilder.setConnectionRequestTimeout(30000);//连接请求时间
		requestConfigBuilder.setConnectTimeout(10000);//连接超时时间
		
		clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
		
		httpClient = clientBuilder.build();
	}
	
	
	/**
	 * 通用请求,内部使用
	 * @param url
	 * @param methodName
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private static HttpResponse request(String uri, String methodName, Map<String,Object> params) throws ClientProtocolException, IOException {
		RequestBuilder requestBuilder = RequestBuilder.create(methodName);
		requestBuilder.setUri(uri);
		if(params != null) {
			for(Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				//是否是集合数据,支持集合数据
				if(value instanceof Iterable) {
					//添加同名数据
					Iterable<?> iterable = (Iterable<?>)value;
					for(Iterator<?> iterator = iterable.iterator(); iterator.hasNext();) {
						String childVal = ObjectUtils.toString(iterator.next());
						requestBuilder.addParameter(key, childVal);
					}
				}else {
					requestBuilder.addParameter(key, ObjectUtils.toString(value));
				}
			}
		}
		
		HttpUriRequest httpUriRequest = requestBuilder.build();
		HttpResponse httpResponse = httpClient.execute(httpUriRequest);
		
		return httpResponse;
	}
	
	
	/**
	 * get请求
	 * @param uri
	 * @param params 可以为null
	 * @return
	 */
	public static String requestGet(String uri, Map<String,Object> params) {
		String result = null;
		try {
			HttpResponse httpResponse = request(uri, HttpGet.METHOD_NAME, params);
			try {
				if(httpResponse.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
					throw new IllegalArgumentException("请求失败，响应状态不正确：" + httpResponse.getStatusLine().getStatusCode());
				}
				result = EntityUtils.toString(httpResponse.getEntity(), defaultEncoding);
			}finally {
				if(httpResponse instanceof Closeable) {
					((Closeable)httpResponse).close();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("数据请求失败 - " + uri , e);
		} 
		
		return result;
	}
	
	
	/**
	 * post请求
	 * @param url
	 * @param params 可以 为null
	 * @return
	 */
	public static String requestPost(String uri, Map<String,Object> params) {
		String result = null;
		try {
			HttpResponse httpResponse = request(uri, HttpPost.METHOD_NAME, params);
			try {
				if(httpResponse.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
					throw new IllegalArgumentException("请求失败，响应状态不正确：" + httpResponse.getStatusLine().getStatusCode());
				}
				result = EntityUtils.toString(httpResponse.getEntity(), defaultEncoding);
			}finally {
				if(httpResponse instanceof Closeable) {
					((Closeable)httpResponse).close();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("数据请求失败 - " + uri , e);
		} 
		
		return result;
	}

	
	/**
	 * get请求字节数据，可用于下载内容
	 * @param url
	 * @param params
	 * @return
	 */
	public static byte[] requestGetData(String uri, Map<String,Object> params) {
		byte[] result = null;
		try {
			HttpResponse httpResponse = request(uri, HttpGet.METHOD_NAME, params);
			try {
				if(httpResponse.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
					throw new IllegalArgumentException("请求失败，响应状态不正确：" + httpResponse.getStatusLine().getStatusCode());
				}
				result = EntityUtils.toByteArray(httpResponse.getEntity());
			}finally {
				if(httpResponse instanceof Closeable) {
					((Closeable)httpResponse).close();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("数据请求失败 - " + uri , e);
		} 
		
		return result;
	}
	
	
	//-----------------------------------------------------

	public static String getDefaultEncoding() {
		return defaultEncoding;
	}


	public static void setDefaultEncoding(String defaultEncoding) {
		HttpUtils.defaultEncoding = defaultEncoding;
	}
	
	
}
