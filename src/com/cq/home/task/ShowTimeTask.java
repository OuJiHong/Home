package com.cq.home.task;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 测试一个定时任务
 * @author OJH
 *
 */
@Component
public class ShowTimeTask {
	
	private static final Log logger = LogFactory.getLog(ShowTimeTask.class);
	
	@Scheduled(cron="0 0/5 * * * ? ")
	public void showTime(){
		logger.info("现在时间：" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
}
