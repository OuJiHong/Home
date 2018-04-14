package com.cq.home.freemarker.method;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

@Component
public class CurrencyMethod implements TemplateMethodModelEx{
	
	private DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		String formatVal = "";
		if(arguments.size() >= 1){
			//第一个参数为需要格式化的值
			Object value = DeepUnwrap.unwrap((TemplateModel)arguments.get(0));
			
			String strVal = ObjectUtils.toString(value);
			if(strVal == null){
				return strVal;
			}
			
			if(NumberUtils.isNumber(strVal)){
				double doubleVal = NumberUtils.toDouble(strVal);
				formatVal = decimalFormat.format(doubleVal);
			}else{
				throw new TemplateModelException("currency方法接收的参数不是数字" + strVal);
			}
			
		}
		
		if(arguments.size() >= 2){
			//第一个参数为需要格式化的值
			Object useCurrency = DeepUnwrap.unwrap((TemplateModel)arguments.get(1));
			if(useCurrency instanceof Boolean && (Boolean)useCurrency){
				formatVal += "￥";
			}
			
		}
		
		return formatVal;
	}

}
