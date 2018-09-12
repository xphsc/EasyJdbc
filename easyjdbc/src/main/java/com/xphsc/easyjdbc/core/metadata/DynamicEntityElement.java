package com.xphsc.easyjdbc.core.metadata;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 *  动态实体元素
 * Created by ${huipei.x}
 */
public class DynamicEntityElement implements Element {

	private static final long serialVersionUID = 2439829119799811675L;

	private String name;// 实体名称
	private Map<String,DynamicFieldElement> dynamicFieldElements = new LinkedHashMap<String,DynamicFieldElement>();//属性
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, DynamicFieldElement> getDynamicFieldElements() {
		return dynamicFieldElements;
	}
	public void addDynamicFieldElements(String columnName, DynamicFieldElement dynamicFieldElement) {
		this.dynamicFieldElements.put(columnName, dynamicFieldElement);
	}
	
}