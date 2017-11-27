package com.cq.home.service;

import com.cq.home.bean.Msg;

/**
 * 消息服务
 * @author OJH
 *
 */
public interface MsgService extends BaseService<Msg>{
	
	/**
	 * 查找单个消息
	 * @param id
	 * @return
	 */
	public Msg findMsg(Long id);
}
