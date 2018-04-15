package com.cq.home.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cq.home.bean.core.Captcha;
import com.cq.home.bean.core.Captcha.CaptchaType;
import com.cq.home.service.CaptchaService;
import com.cq.home.util.SpringUtils;

/**
 *
 *验证码服务
 * @author Administrator
 * 2018年4月14日 下午12:28:11
 *
 */
@Service
public class CaptchaServiceImpl implements CaptchaService{

	
	private static String CAPTCAH_PREFIX = CaptchaServiceImpl.class.getName() + "_";
	
	private static Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);
	
	
	
	@Override
	public Captcha createCaptcha(CaptchaType captchaType) {
		int width = captchaType.getWidth();
		int height = captchaType.getHeight();
		BufferedImage bufferedImage  = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = bufferedImage.createGraphics();
		graphics.setColor(randomColor(230,255));
		graphics.fillRect(0, 0, width, height);
		for(int i = 0 ; i < 50; i++) {
			int x = RandomUtils.nextInt(width);
			int y = RandomUtils.nextInt(height);
			int size = RandomUtils.nextInt(10);
			graphics.setColor(randomColor(0,100));
			graphics.drawLine(x, y, x + size, y + size);
		}
		
		String randomStr = RandomStringUtils.randomAlphanumeric(captchaType.getCount());
		Font font = createFont(captchaType.getCount(), width, height);
		graphics.setFont(font);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		int strY = (height / 2) + (fontMetrics.getAscent() / 3);
		for(int i = 0; i < randomStr.length(); i++) {
			String code = randomStr.substring(i , i +1);
			graphics.setColor(randomColor(0,100));
			int strX = 2 +  font.getSize() * i;
			
			graphics.drawString(code, strX, strY);
		}
		
		graphics.dispose();
		bufferedImage.flush();
		
		Captcha captcha = new Captcha();
		captcha.setCaptchaType(captchaType);
		captcha.setCode(randomStr);
		captcha.setData(bufferedImage);
		
		//cache
		SpringUtils.currentRequest().getSession().setAttribute(CAPTCAH_PREFIX + captchaType.name(), captcha);
		return captcha;
	}

	
	/**
	 * 随机颜色
	 * @param start
	 * @param end
	 * @return
	 */
	private Color randomColor(int start, int end ) {
		int range = end - start;
		if(range < 0) {
			range = 1;
		}
		int r = RandomUtils.nextInt(range) + start;
		int g = RandomUtils.nextInt(range) + start;
		int b = RandomUtils.nextInt(range) + start;
		Color color = new Color(r, g, b);
		return color;
	}
	
	/**
	 * 计算字体大小
	 * @param width
	 * @param height
	 * @return
	 */
	private Font createFont(int count, int width, int height) {
		int size = 12;
		size = (width / count);
		if(size > height) {
			size = height;
		}
		Font font = new Font("Default", Font.BOLD, size);
		return font;
	}
	
	
	@Override
	public boolean isValid(CaptchaType captchaType, String code) {
		Captcha cacheCaptcha = (Captcha)SpringUtils.currentRequest().getSession().getAttribute(CAPTCAH_PREFIX + captchaType.name());
		if(cacheCaptcha != null) {
			if(cacheCaptcha.getCode().equalsIgnoreCase(code)) {
				return true;
			}
			logger.info("验证码类型：" + captchaType.name()  + ",验证码值：" + code + ",实际需要：" + cacheCaptcha.getCode());
		}
		
		logger.info("指定验证码类型未找到：" + captchaType.name());
		return false;
	}

}
