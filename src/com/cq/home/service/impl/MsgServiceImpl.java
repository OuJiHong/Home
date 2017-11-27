package com.cq.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cq.home.bean.Msg;
import com.cq.home.dao.BaseDao;
import com.cq.home.dao.MsgDao;
import com.cq.home.service.MsgService;

/**
 * @author OJH
 *
 */
@Service
public class MsgServiceImpl extends BaseServiceImpl<Msg> implements MsgService{
	
	@Autowired
	private MsgDao msgDao;
	
	
	@Override
	public Msg findMsg(Long id) {
		return msgDao.findMsg(id);
	}


	@Override
	public BaseDao<Msg> getBaseDao() {
		return msgDao;
	}

	
	

}
