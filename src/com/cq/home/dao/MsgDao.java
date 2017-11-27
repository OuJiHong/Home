package com.cq.home.dao;
import java.util.List;

import com.cq.home.bean.Msg;

public interface MsgDao extends BaseDao<Msg> {
	
//	@Select("select * from msg where id =  #{id}")
	public Msg  findMsg(Long id);

	
	public List<Msg> queryAll();
	
}
