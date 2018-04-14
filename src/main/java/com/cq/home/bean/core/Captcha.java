package com.cq.home.bean.core;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 *验证码对象
 * @author Administrator
 * 2018年4月14日 下午12:24:07
 *
 */
public class Captcha implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *
	 *验证码类型
	 * @author Administrator
	 * 2018年4月14日 下午12:25:59
	 *
	 */
	public enum CaptchaType{
		adminLoginType(170, 40, 4);
		
		private int width;
		
		private int height;

		private int count;//字符数量
		
		private CaptchaType(int width, int height, int count) {
			this.width = width;
			this.height = height;
			this.count = count;
		}

		public int getWidth() {
			return width;
		}


		public int getHeight() {
			return height;
		}


		public int getCount() {
			return count;
		}
		
		
	}
	
	private CaptchaType captchaType;
	
	private String code;

	private  BufferedImage data;
	
	public CaptchaType getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(CaptchaType captchaType) {
		this.captchaType = captchaType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BufferedImage getData() {
		return data;
	}

	public void setData(BufferedImage data) {
		this.data = data;
	}
	
	
}
