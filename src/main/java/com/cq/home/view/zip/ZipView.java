package com.cq.home.view.zip;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.cq.home.util.ZipUtils;

/**
 *
 *压缩包视图
 * @author Administrator
 * 2018年4月20日 下午11:28:51
 *
 */
public class ZipView extends AbstractView{
	
	
	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.ms-download";
	
	/**
	 * 输出的临时目录
	 */
	private String tempOutFolder = File.separator + "tmp" + File.separator 
			+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
	
	/**
	 * 下载的文件名
	 */
	private String fileName;
	
	/**
	 * 
	 * 需要压缩的资源
	 */
	private File source;
	
	/**
	 * 设置资源
	 * @param source
	 */
	public ZipView(File source) {
		setContentType(CONTENT_TYPE);
		this.source = source;
	}
	
	/**
	 * 设置资源
	 * @param source
	 */
	public ZipView(String fileName, File source) {
		setContentType(CONTENT_TYPE);
		this.fileName = fileName;
		this.source = source;
	}
	
	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}
	
	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		super.prepareResponse(request, response);
		
		if(this.fileName == null) {
			this.fileName = source.getName();
		}
		
		String tmpName = this.fileName + ".zip";
		
		try {
			tmpName = new String(tmpName.getBytes(), "ISO-8859-1");//防止文件名乱码
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition","attachment; filename=" + tmpName);
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String realPath = request.getServletContext().getRealPath("/");
		
		File outFile = ZipUtils.compress(source, realPath + tempOutFolder);
		

		// Set the content type.
		response.setContentType(getContentType());

		OutputStream outputStream = response.getOutputStream();
		
		ZipUtils.transferData(outFile, outputStream);
		outputStream.flush();
		
	}

}
