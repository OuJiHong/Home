package com.cq.home.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cq.home.bean.Msg;
import com.cq.home.bean.core.Pageable;
import com.cq.home.controller.BaseController;
import com.cq.home.service.MsgService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/backend/msg")
public class MsgController extends BaseController{
	
	@Autowired
	private MsgService msgService;
	
	@RequestMapping("list")
	public PageInfo<Msg> index(Pageable pageable){
		return msgService.findPage(pageable);
	}
	
	
	@RequestMapping("/detail")
	public void detail(@RequestParam Long id, Model model){
		Msg msg = msgService.findMsg(id);
		model.addAttribute("msg", msg);
	}
	
	
}
