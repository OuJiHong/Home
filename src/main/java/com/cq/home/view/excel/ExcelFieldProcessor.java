package com.cq.home.view.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cq.home.util.ZipUtils;

/**
 * excelField 注解的处理,基于POI处理
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
public class ExcelFieldProcessor {
	
	private static Logger logger  = LoggerFactory.getLogger(ExcelFieldProcessor.class);
	
	/**
	 * 定义数据转换，根据需要扩展转换器
	 */
	private ConvertUtilsBean convertUtilsBean  = new ConvertUtilsBean();
	
	{
		//常用日期格式
		String[] patterns = new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss" };
		//初始化配置
		DateConverter dateConverter = new DateConverter(null);
		dateConverter.setPatterns(patterns);
		
		convertUtilsBean.register(false, true, 0);//不抛异常，使用null为默认值
		convertUtilsBean.register(dateConverter, Date.class);
		
	}
	
	/**
	 * 可选样式
	 * @author 欧集红 
	 * @Date 2018年4月16日
	 * @version 1.0
	 * 
	 */
	public static enum StyleFactory{
		titleStyle((short)260, Font.BOLDWEIGHT_BOLD,HSSFColor.BLACK.index,CellStyle.ALIGN_CENTER),
		normalStyle((short)240, Font.BOLDWEIGHT_NORMAL,HSSFColor.BLACK.index,CellStyle.ALIGN_LEFT)
		;

		private short fontSize;
		
		private short  fontBoldWeight;
		
		private short fontColor;
		
		private short textAlignment;
		
		/**
		 *  缓存的样式记录
		 */
		private Map<Workbook, CellStyle> _cacheStyleMap = new HashMap<Workbook, CellStyle>();
		
		private StyleFactory(short fontSize, short fontBoldWeight, short fontColor, short textAlignment) {
			this.fontSize = fontSize;
			this.fontBoldWeight = fontBoldWeight;
			this.fontColor = fontColor;
			this.textAlignment = textAlignment;
		}


		/**
		 * 创建样式
		 * @param workbook
		 * @return
		 */
		public  CellStyle getObject(Workbook workbook) {
			CellStyle cacheStyle = _cacheStyleMap.get(workbook);
			if(cacheStyle != null) {
				return cacheStyle;
			}
			
			CellStyle cellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontHeight(this.fontSize);
			font.setBoldweight(this.fontBoldWeight);
			font.setColor(this.fontColor);
			cellStyle.setFont(font);
			cellStyle.setAlignment(this.textAlignment);
			
			_cacheStyleMap.put(workbook, cellStyle);//缓存
			
			return cellStyle;
		}
		
		
	}
	
	/**
	 * 字段属性与标题映射
	 * @author 欧集红 
	 * @Date 2018年4月16日
	 * @version 1.0
	 * 
	 * @param <K>
	 * @param <V>
	 */
	public static class MappingEntry{
		private int cellIndex;//读取时获取标题索引位置
		private String name;//属性名称
		private String title;//标题
		private String format = "";//格式化,默认一个空值
		
		public MappingEntry(String name, String title) {
			super();
			this.name = name;
			this.title = title;
		}
		
		public MappingEntry(String name, String title, String format) {
			super();
			this.name = name;
			this.title = title;
			this.format = format;
		}
		
	}
	
	/**
	 * 标题样式
	 */
	private StyleFactory titleStyle = StyleFactory.titleStyle;
	
	/**
	 * 默认样式
	 */
	private StyleFactory defaultStyle = StyleFactory.normalStyle;
	
	/**
	 * 默认日期格式化
	 */
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 转换的类型
	 */
	private Class<?> convertType;
	
	/**
	 *映射关联集合 
	 */
	private List<MappingEntry> mappingEntries;
	
	/**
	 *excel工作薄 
	 */
	private Workbook workbook;
	
	
	/**
	 *分页编号 
	 */
	private int pageNumber = 1;
	
	/**
	 * 分页大小
	 */
	private int pageSize = 65500;
	
	/**
	 * 内部记录位置 
	 */
	private int _rowIndex = 0;
	
	private Sheet _sheet;
	
	/**
	 * 最大列宽统计
	 */
	private Map<Integer, Integer> _columnMaxWidthRecord = new HashMap<Integer, Integer>();
	
	
	/**
	 * @param convertType
	 */
	public ExcelFieldProcessor(Class<?> convertType) {
		this(new HSSFWorkbook(), convertType);
	}
	
	/**
	 * @param convertType
	 */
	public ExcelFieldProcessor(List<MappingEntry> mappingEntries) {
		this(new HSSFWorkbook(), mappingEntries);
	}
	
	
	/**
	 * @param convertType 普通的POJO类型
	 */
	public ExcelFieldProcessor(Workbook workbook, Class<?> convertType) {
		this.workbook = workbook;//必须
		this.convertType = convertType;
		//转换
		if(this.convertType != null) {
			this.mappingEntries = generatedMappingEntries(convertType);
			initTitle(this.mappingEntries);//初始化标题
		}
		
	}
	
	/**
	 * @param mappingEntries
	 */
	public ExcelFieldProcessor(Workbook workbook, List<MappingEntry> mappingEntries) {
		this.workbook = workbook;//必须
		this.mappingEntries = mappingEntries;
		initTitle(this.mappingEntries);//初始化标题
	}
	
	
	/**
	 * 产生映射集合
	 * @param convertType
	 * @return
	 */
	private List<MappingEntry> generatedMappingEntries(Class<?> convertType){
		List<MappingEntry> list = new ArrayList<MappingEntry>();
		Field[] fields = convertType.getDeclaredFields();//暂时只检测字段，不检测get方法，以便统一规范
		for(Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if(excelField != null) {
				String name = excelField.value();
				if(name.length() == 0) {
					name = field.getName();
				}
				MappingEntry entry = new MappingEntry(field.getName(), name , excelField.format());
				list.add(entry);
			}
		}
		
		return list;
	}
	
	/**
	 * 初始化标题
	 * @param mappingEntries
	 */
	private void initTitle(List<MappingEntry> mappingEntries) {
		Map<String,Object> titleData = new LinkedHashMap<String,Object>();
		for(MappingEntry mappingEntry : mappingEntries) {
			titleData.put(mappingEntry.name, mappingEntry.title);
		}
		
		addRow(titleData, mappingEntries, true);//添加标题
	}
	
	/**
	 * 核心处理方法，产生结果
	 * @param data
	 * @param convertType
	 * @return
	 */
	public Workbook write(Collection<?> dataCollection) {
		
		for(Object data : dataCollection) {
			addRow(data, this.mappingEntries, false);
		}
		
		return this.workbook;
	}

	/**
	 * 读取数据
	 * @return
	 */
	public <T> List<T> readAll(InputStream inputStream){
		try {
			this.workbook = new HSSFWorkbook(inputStream);//excel2003
		}catch(Exception e) {
			try {
				this.workbook = new XSSFWorkbook(inputStream);//exce2007
			} catch (IOException e1) {
				throw new IllegalArgumentException("读取excel失败", e1);
			}
		}
		
		List<T> list = new ArrayList<T>();
		int numberOfSheets = this.workbook.getNumberOfSheets();
		boolean titleProcessed = false;//是否成功处理标题
		for(int i = 0 ; i < numberOfSheets; i++) {
			Sheet sheet = this.workbook.getSheetAt(i);
			//因为返回的行是索引，所以是<=
			for(int j = 0; j <= sheet.getLastRowNum(); j++) {
				Row row = sheet.getRow(j);
				if(row == null) {
					continue;
				}
				
				//处理标题
				if(!titleProcessed) {
					titleProcessed = processTitleInfo(row, this.mappingEntries);
					continue;
				}
				
				
				try {
					T t = read(row, this.mappingEntries);
					list.add(t);
				} catch (Exception e) {
					logger.debug("数据读取失败：" + e.getMessage());
				}
				
			}
		}
		return list;
	}
	
	/**
	 * 处理标题信息，获取标题位置
	 * @param titleRow
	 * @param mappingEntries
	 */
	private boolean processTitleInfo(Row titleRow, List<MappingEntry> mappingEntries) {
		int lastCellNum = titleRow.getLastCellNum();
		//因为lastCellNum返回的是大小，所以用<
		boolean processSuccess = false;
		for(int i = 0 ; i < lastCellNum; i++) {
			Cell cell  = titleRow.getCell(i);
			if(cell == null) {
				continue;
			}
			String value = cell.getStringCellValue();
			for(MappingEntry entry : mappingEntries) {
				if(entry.title != null && value != null && entry.title.trim().equals(value.trim())) {
					entry.cellIndex = i;
					processSuccess = true;//有数据，表示成功
					break;
				}
			}
		}
		
		return processSuccess;
	}
	
	/**
	 * 读取一行数据
	 * @param row
	 * @param mappingEntries
	 * @return
	 * @throws Exception
	 */
	private <T> T read(Row row, List<MappingEntry> mappingEntries) throws Exception{
		Object obj = this.convertType.newInstance();
		for(MappingEntry entry : mappingEntries) {
			Cell cell = row.getCell(entry.cellIndex);
			if(cell == null) {
				continue;
			}
			Object value = null;
			int cellType = cell.getCellType();
			switch(cellType) {
				case Cell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
				break;
				case Cell.CELL_TYPE_NUMERIC:
					value = cell.getNumericCellValue();
				break;
				default:
					value = cell.getStringCellValue();
			}
			
			Class<?> propType = PropertyUtils.getPropertyType(obj, entry.name);
			if(value != null && !value.getClass().isAssignableFrom(propType)) {
				//如果数据类型不兼容，转型
				value = this.convertUtilsBean.convert(value, propType);
			}
			PropertyUtils.setProperty(obj, entry.name, value);
		}
		
		return (T)obj;
	}
	
	/**
	 * 添加一行数据
	 * @param data
	 * @param mappingEntries
	 */
	private void addRow(Object data, List<MappingEntry> mappingEntries, boolean isTitle) {
		if(_sheet == null) {
			_sheet = workbook.createSheet();
		}
		
		if(_rowIndex >= pageSize) {
			pageNumber++;
			_sheet = workbook.createSheet();
			_rowIndex = 0;
		}
		
		Row row = _sheet.createRow(nextRowIndex());
		for(int i = 0 ; i < mappingEntries.size();i++) {
			MappingEntry entry = mappingEntries.get(i);
			Cell cell = row.createCell(i);
			try {
				Object value = PropertyUtils.getProperty(data, entry.name);
				setCellValue(cell, entry, value, isTitle);
			} catch (Exception e) {
				logger.debug("数据写入失败：" + e.getMessage());
			} 
		}
		
	}
	
	/**
	 * 设置cell的值 
	 * @param cell
	 * @param value
	 */
	protected void setCellValue(Cell cell, MappingEntry mappingEntry, Object value, boolean isTitle) {
		if(value == null) {
			value = "";
		}
		
		//非标题才格式化数据
		if(!isTitle && mappingEntry.format.trim().length() > 0) {
			//格式化数据
			try {
				String newValue = String.format(mappingEntry.format, value);
				cell.setCellValue(newValue);
			}catch(Exception e) {
				throw new IllegalArgumentException("格式化字段‘"+mappingEntry.name+"’失败，使用的表达式为：" + mappingEntry.format, e);
			}
			
		}else {
			if(value instanceof Boolean) {
				cell.setCellValue((Boolean)value);
			}else if(value instanceof Number){
				cell.setCellValue(((Number)value).doubleValue());
			}else if(value instanceof Date){
				cell.setCellValue(defaultDateFormat.format((Date)value));
			}else if(value instanceof Calendar){
				Calendar tmpVal = (Calendar)value;
				cell.setCellValue(defaultDateFormat.format(tmpVal.getTime()));
			}else {
				cell.setCellValue(value.toString());
			}
			
		}
		
		//是否是标题
		if(isTitle) {
			CellStyle cellStyle = this.titleStyle.getObject(workbook);
			cell.setCellStyle(cellStyle);
		}else {
			CellStyle cellStyle = this.defaultStyle.getObject(workbook);
			cell.setCellStyle(cellStyle);
		}
		
		
		//统计最大列宽
		try {
			Integer cellWidth = value.toString().getBytes("GBK").length;
			Integer oldWidth = _columnMaxWidthRecord.get(cell.getColumnIndex());
			if(oldWidth == null || oldWidth < cellWidth) {
				_columnMaxWidthRecord.put(cell.getColumnIndex(), cellWidth);
			}
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();//ignore
		}
		
		
	}
	
	
	/**
	 * 自适应列
	 */
	public void autoSizeColumn() {
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			for(int j = 0 ; j < mappingEntries.size();j++) {
				Integer cellWidth = _columnMaxWidthRecord.get(j);
				if(cellWidth != null) {
					sheet.setColumnWidth(j, cellWidth * this.titleStyle.fontSize);
				}else {
					sheet.autoSizeColumn(j);
				}
				
			}
		}
		
		
	}
	
	/**
	 * 写入到目标
	 * @param stream
	 */
	public void outToStream(OutputStream stream) {
		try {
			this.workbook.write(stream);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 返回下一行
	 * @return
	 */
	private int nextRowIndex() {
		return _rowIndex++;
	}
	

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public StyleFactory getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(StyleFactory titleStyle) {
		this.titleStyle = titleStyle;
	}

	public StyleFactory getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(StyleFactory defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public DateFormat getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	
	
	
}
