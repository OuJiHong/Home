package com.cq.home.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import com.cq.home.bean.User;
import com.cq.home.bean.core.Message;
import com.cq.home.bean.core.Pageable;
import com.cq.home.bean.vo.UserExcelVo;
import com.cq.home.controller.BaseController;
import com.cq.home.exception.BizException;
import com.cq.home.service.UserService;
import com.cq.home.view.excel.CommonExcelView;
import com.cq.home.view.excel.ExcelFieldProcessor;
import com.cq.home.view.zip.ZipView;
import com.github.pagehelper.PageInfo;

/**
 * 用户控制器
 * @author OJH
 *
 */
@Controller
@RequestMapping(value="/admin/user")
public class AdminUserController extends BaseController{
	
	
	@Autowired
	private UserService userService;
	
	/**
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public PageInfo<User> list(Pageable pageable){
		return userService.findPage(pageable);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public Message add(@Valid User user) throws Exception{
		userService.add(user);
		return Message.success("添加成功");
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public Message update(User user) throws Exception{
		userService.update(user);
		return Message.success("修改成功");
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	@RequestMapping(value="/count", method=RequestMethod.GET)
	@ResponseBody
	public Long count(){
		return userService.count(null);
	}
	
	
	/**
	 * 导出所有数据
	 * @return
	 */
	@RequestMapping("exportUserList")
	public View exportUserList(Boolean zip) {
		
		List<User> list = userService.findList(null);
		List<UserExcelVo> excelList = new ArrayList<UserExcelVo>();
		for(User user : list) {
			UserExcelVo excelVo = new UserExcelVo();
			BeanUtils.copyProperties(user, excelVo);
			excelList.add(excelVo);
		}
		
		if(zip != null && zip) {
			ExcelFieldProcessor excelProcessor = new ExcelFieldProcessor(UserExcelVo.class);
			try {
				File tmpFile = File.createTempFile("tmp", ".xls");
				FileOutputStream outStream = new FileOutputStream(tmpFile);
				excelProcessor.outToStream(outStream);
				outStream.close();
				//产生视图
				ZipView zipView = new ZipView(tmpFile);
				return zipView;
			} catch (IOException e) {
				throw new BizException("导出处理错误", e);
			}
		}
		
		CommonExcelView excelView = new CommonExcelView("用户信息列表", excelList, UserExcelVo.class);
		return excelView;
	}
	

	/**
	 * 导入所有数据
	 * @return
	 */
	@RequestMapping("importUserList")
	public String importUserList(@RequestParam MultipartFile file, Model model) throws IOException {
		ExcelFieldProcessor excelFieldProcessor = new ExcelFieldProcessor(UserExcelVo.class);
		List<UserExcelVo> list = excelFieldProcessor.readAll(file.getInputStream());
		model.addAttribute("list", list);
		return "/user/importUserList";
	}
	
}
