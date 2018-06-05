package com.cq.home.view.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.ObjectUtils;
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

import com.cq.home.exception.BizException;
/**
 * excelField 注解的处理,基于POI处理
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
public class ExcelFieldProcessor <T> {
	
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
		titleStyle((short)240, Font.BOLDWEIGHT_BOLD,HSSFColor.BLACK.index,CellStyle.ALIGN_CENTER),
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
		private int cellIndex = -1;//读取时获取标题索引位置
		private String name;//属性名称
		private String title;//标题
		
		public MappingEntry(String name, String title) {
			super();
			this.name = name;
			this.title = title;
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
	private Workbook workbook = new HSSFWorkbook();
	
	
	/**
	 * 分页大小
	 */
	private int pageSize = 65500;
	
	/**
	 * 内部记录位置 
	 */
	private int _rowIndex = -1;
	
	/**
	 * 默认为第一个工作表
	 */
	private int _sheetIndex = 0;
	
	/**
	 * @param convertType 普通的POJO类型
	 */
	public ExcelFieldProcessor(Class<T> convertType) {
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
	public ExcelFieldProcessor(List<MappingEntry> mappingEntries) {
		this.mappingEntries = mappingEntries;
		//初始化位置,按原始顺序排列
		for(int i = 0; i < this.mappingEntries.size(); i++) {
			MappingEntry m = this.mappingEntries.get(i);
			if(m != null) {
				m.cellIndex = i;
			}
		}
		initTitle(this.mappingEntries);//初始化标题
	}
	
	

	
	/**
	 * 从一个数据流中加载一个新的工作簿
	 * @param excelInputStream
	 */
	public void load(InputStream excelInputStream) {
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(excelInputStream);//excel2003
		}catch(Exception e) {
			try {
				workbook = new XSSFWorkbook(excelInputStream);//excel2007
			} catch (IOException e1) {
				throw new IllegalArgumentException("读取excel失败", e1);
			}
		}finally {
			if(excelInputStream != null) {
				try {
					excelInputStream.close();
				} catch (IOException e) {
					logger.error("excelInputStream close error - " + e.getMessage());
				}
			}
		}
		
		load(workbook);
	}
	
	/**
	 * 加载一个新的工作簿
	 * @param workbook
	 */
	public void load(Workbook workbook) {
		this.workbook = workbook;
		this.defaultStyle = null;//以模版的样式为准
		
		//按照加载的标题，重新建立字段映射顺序
		int numberOfSheets = this.workbook.getNumberOfSheets();
		//重置工作表
		this._sheetIndex = 0 ;
		
		titleEnd:
		while(_sheetIndex < numberOfSheets) {
			resetPosition();
			Sheet sheet = currentSheet();
			//因为返回的行是索引，所以是<=
			while(position() <= sheet.getLastRowNum()) {
				Row row = sheet.getRow(nextPosition());
				if(row == null) {
					continue;
				}
				
				//处理标题
				boolean processSuccess = processTitleInfo(row, this.mappingEntries);
				if(processSuccess) {
					break titleEnd;
				}
				
			}
			
			_sheetIndex++;
		}
		
		
	}
	
	
	/**
	 * 产生映射集合
	 * @param convertType
	 * @return
	 */
	private List<MappingEntry> generatedMappingEntries(Class<?> convertType){
		List<MappingEntry> list = new ArrayList<MappingEntry>();
		//包含父类的字段,如果重名可能会覆盖
		Class<?> tempClass = convertType;
		List<Class<?>> orderClasses = new ArrayList<Class<?>>();
		while(tempClass != null && tempClass != Object.class) {
			orderClasses.add(tempClass);
			tempClass = tempClass.getSuperclass();
		}
		
		Collections.reverse(orderClasses);//由父类到子类的顺序查找，使得列表字段有序
		int cellIndex = 0;
		for(Class<?> clazz : orderClasses) {
			Field[] fields = clazz.getDeclaredFields();//暂时只检测字段，不检测get方法，以便统一规范
			for(Field field : fields) {
				ExcelField excelField = field.getAnnotation(ExcelField.class);
				if(excelField != null) {
					String name = excelField.value();
					if(name.length() == 0) {
						name = field.getName();
					}
					MappingEntry entry = new MappingEntry(field.getName(), name );
					entry.cellIndex = cellIndex;
					list.add(entry);
					
					cellIndex++;//按原始顺序排列cell
				}
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
		
		Row row = addNextRow(titleData);//添加标题
		//设置标题样式
		for(int i = 0 ; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			CellStyle cellStyle = this.titleStyle.getObject(workbook);
			cell.setCellStyle(cellStyle);
			cell.getRow().setHeight((short)(this.titleStyle.fontSize * 2));
		}
		
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
	 * 核心处理方法，产生结果
	 * @param data
	 * @param convertType
	 * @return
	 */
	public Workbook write(Collection<T> dataCollection) {
		
		for(Object data : dataCollection) {
			addNextRow(data);
		}
		
		return this.workbook;
	}

	/**
	 * 读取数据
	 * @return
	 */
	public List<T> readAll(InputStream inputStream, boolean ignoreError){
		this.load(inputStream);
		
		//标题处理时可能读取了多个工作表，所以这里不能重置_sheetIndex的值
		List<T> list = new ArrayList<T>();
		int numberOfSheets = this.workbook.getNumberOfSheets();
		
		//去掉循环，只读取第一页，很多模版的第二页不是需要导入的数据
		if(_sheetIndex < numberOfSheets) {
			//这里不能重置行索引，因为读取的标题行要排除
			Sheet sheet = currentSheet();
			//因为返回的行是索引，所以是<=
			while(position() <= sheet.getLastRowNum()) {
				try {
					T t = readNextRow();
					if(t != null) {
						list.add(t);
					}
				} catch (Exception e) {
					if(ignoreError) {
						logger.debug("数据读取失败,在第'"+ position() +"'行记录：" + e.getMessage());
					}else {
						throw new BizException("数据读取失败,在第'"+  position() +"'行记录", e);
					}
				}
			}
			
			//_sheetIndex++;
		}
		
		
		return list;
	}
	

	
	/**
	 * 读取一行数据
	 * @param row
	 * @param mappingEntries
	 * @return
	 * @throws Exception
	 */
	private <T> T readNextRow() throws Exception{
		Sheet sheet = currentSheet();
		if(sheet == null) {
			return null;
		}
		
		Row row = sheet.getRow(nextPosition());
		if(row  == null) {
			return null;
		}
		
		Object obj = this.convertType.newInstance();
		for(MappingEntry entry : mappingEntries) {
			if(entry.cellIndex == -1) {
				//不存在的位置
				continue;
			}
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
	private Row addNextRow(Object data) {
		Sheet sheet = currentSheet();
		//是否需要分页,老版本的excel有行数限制
		if(this.workbook instanceof HSSFWorkbook) {
			if(sheet == null || _rowIndex >= pageSize) {
				sheet = workbook.createSheet();
				_sheetIndex = workbook.getSheetIndex(sheet);
				resetPosition();
			}
		}
		
		
		Row row = sheet.createRow(nextPosition());
		for(int i = 0 ; i < mappingEntries.size();i++) {
			MappingEntry entry = mappingEntries.get(i);
			//按索引位置写入
			if(entry.cellIndex == -1) {
				continue;
			}
			Cell cell = row.createCell(entry.cellIndex);
			try {
				Object value = PropertyUtils.getProperty(data, entry.name);
				setCellValue(cell, entry, value);
			} catch (Exception e) {
				logger.debug("数据写入失败：" + e.getMessage());
			} 
		}
		
		return row;
	}
	
	/**
	 * 设置cell的值 
	 * @param cell
	 * @param value
	 */
	protected void setCellValue(Cell cell, MappingEntry mappingEntry, Object value) {
		if(value == null) {
			value = "";
		}
		
		Object tmpCellValue = value;//临时记录实际设置的数据
		
		if(value instanceof Boolean) {
			tmpCellValue = (Boolean)value;
			cell.setCellValue((Boolean)tmpCellValue);
		}else if(value instanceof Number){
			tmpCellValue = (Number)value;
			cell.setCellValue(((Number)tmpCellValue).doubleValue());
		}else if(value instanceof Date){
			tmpCellValue = defaultDateFormat.format((Date)value);
			cell.setCellValue((String)tmpCellValue);
		}else if(value instanceof Calendar){
			Calendar tmpVal = (Calendar)value;
			tmpCellValue = defaultDateFormat.format(tmpVal.getTime());
			cell.setCellValue((String)tmpCellValue);
		}else {
			tmpCellValue = ObjectUtils.toString(value.toString());
			cell.setCellValue(value.toString());
		}
			
		
		if(this.defaultStyle != null) {
			CellStyle cellStyle = this.defaultStyle.getObject(workbook);
			cell.setCellStyle(cellStyle);
			cell.getRow().setHeight((short)(this.defaultStyle.fontSize * 1.6));
		}
		
	}
	
	
	/**
	 * 自适应列
	 */
	public void autoSizeColumn() {
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			for(int j = 0 ; j < mappingEntries.size();j++) {
				int width = (mappingEntries.get(j).title.getBytes().length  + 4)* 256;//按两个字节计算
				sheet.setColumnWidth(j, width);
				//sheet.autoSizeColumn(j);//此方法不能自适应中文
			}
		}
		
	}
	
	/**
	 * 写入到目标
	 * @param stream
	 */
	public void outToStream(OutputStream stream) {
		//为了防止关闭类似于ServletResponse的输出流，所以不再自动close
		try {
			this.workbook.write(stream);
			stream.flush();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 写入到目标文件
	 * @param outFile
	 */
	public void outToFile(File outFile) {
		FileOutputStream stream = null;
		if(!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();//自动创建目录
		}
		
		try {
			outFile.createNewFile();
			stream = new FileOutputStream(outFile);
			this.workbook.write(stream);
			stream.flush();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}finally {
			if(stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					//record log
					logger.error("close failed - " + e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * 返回下一行，位置移动
	 * @return
	 */
	public int nextPosition() {
		++_rowIndex;
		return _rowIndex;
	}
	
	/**
	 * 当前行位置
	 * @return
	 */
	public int position() {
		return _rowIndex;
	}
	
	/**
	 * 重置位置
	 */
	public void resetPosition() {
		_rowIndex = -1;
	}

	/**
	 * 当前工作表
	 * @return
	 */
	public Sheet currentSheet() {
		int sheetSize = this.workbook.getNumberOfSheets();
		if(_sheetIndex <0 || _sheetIndex >= sheetSize) {
			return null;
		}
		return this.workbook.getSheetAt(_sheetIndex);
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

	/**
	 * 获取处理过后的工作簿
	 * @return
	 */
	public Workbook getWorkbook() {
		return workbook;
	}

	
}