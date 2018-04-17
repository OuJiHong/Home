package com.cq.home.view.excel;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * 通用的excel视图
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
public class CommonExcelView<T> extends AbstractExcelView{
	
	private String fileName;//下载名称
	
	private Collection<T> dataCollection;//数据
	
	private Class<T> convertType;//转换的对象类型
	
	public CommonExcelView(Collection<T> dataCollection, Class<T> convertType) {
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	
	public CommonExcelView(String fileName, Collection<T> dataCollection, Class<T> convertType) {
		this.fileName = fileName;
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		 ExcelFieldProcessor<T> excelFieldProcessor = new ExcelFieldProcessor<>(workbook, convertType);
		 excelFieldProcessor.process(dataCollection);
		 excelFieldProcessor.autoSizeColumn();
	}

	
	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		super.prepareResponse(request, response);
		//附加响应头
		if(this.fileName != null) {
			response.setHeader("Content-Disposition","attachment; filename=" + this.fileName);
		}
	}
	
}
