package com.cq.home.controller.backend;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 交易接收
 * @author OJH
 *
 */
@Controller
@RequestMapping("/backend/trans")
public class TransReceiveController {
	
	private static final Log log = LogFactory.getLog(TransReceiveController.class);
	
	/**
	 * @return
	 */
	@RequestMapping("/receive")
	@ResponseBody
	public String recive(HttpServletRequest request, String name) throws Exception{
		log.info("接收到请求：" + request.getPathInfo());
		if(name == null){
			throw new Exception("必须传入name");
		}
		return "RECV_ORD_ID";
	}
	
}
