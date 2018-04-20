package com.cq.home.view.excel;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 通用的excel视图
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
public class CommonExcelView extends AbstractView{
	
	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.ms-excel";

	/** The extension to look for existing templates */
	private static final String EXTENSION = ".xls";
	
	private Workbook workbook;//兼容做法，直接提供workbook
	
	private String fileName;//下载名称
	
	private Collection<?> dataCollection;//数据
	
	private Class<?> convertType;//转换的对象类型
	
	/**
	 * 接受一个整理好的workbook
	 * @param workbook
	 */
	public  CommonExcelView(Workbook workbook) {
		setContentType(CONTENT_TYPE);
		this.workbook = workbook;
	}
	
	public <T> CommonExcelView(Collection<T> dataCollection, Class<T> convertType) {
		setContentType(CONTENT_TYPE);
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	
	public <T> CommonExcelView(String fileName, Collection<T> dataCollection, Class<T> convertType) {
		setContentType(CONTENT_TYPE);
		this.fileName = fileName;
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}
	
	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		super.prepareResponse(request, response);
		//附加响应头
		if(this.fileName != null) {
			String tmpName = this.fileName.replaceAll("\\.xls$|\\.xlsx$", "");
			if(this.workbook != null && this.workbook instanceof XSSFWorkbook) {
				tmpName += ".xlsx";
			}else {
				tmpName += ".xls";
			}
			
			try {
				tmpName = new String(tmpName.getBytes(), "ISO-8859-1");//防止文件名乱码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setHeader("Content-Disposition","attachment; filename=" + tmpName);
		}
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//根据提供的workbook处理
		if(workbook == null) {
			workbook = new HSSFWorkbook();//默认类型
		}
		
		ExcelFieldProcessor excelFieldProcessor = new ExcelFieldProcessor(workbook, convertType);
		if(dataCollection != null && convertType != null) {
			excelFieldProcessor.write(dataCollection);
			excelFieldProcessor.autoSizeColumn();
		}
		

		// Set the content type.
		response.setContentType(getContentType());

		
		OutputStream stream = response.getOutputStream();
		excelFieldProcessor.outToStream(stream);
		stream.flush();
		
	}
	
}
