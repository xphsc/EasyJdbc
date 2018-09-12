package com.xphsc.easyjdbc.core.metadata;

import java.lang.reflect.Method;


/**
 *  动态实体字段
 * Created by ${huipei.x}
 */
public class DynamicFieldElement implements Element {
	
	private static final long serialVersionUID = -7813961877039577015L;
	
	private boolean map;//字段名
	private String name;//字段名
	private String column;//列名
	private Class<?> type;//字段类型
	private Method readMethod;
	private Method writeMethod;
	private boolean clob;//是否二进制clob类型
	private boolean blob;//是否二进制blob类型
	
	public boolean isMap() {
		return map;
	}
	public void setMap(boolean map) {
		this.map = map;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Method getReadMethod() {
		return readMethod;
	}
	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}
	public Method getWriteMethod() {
		return writeMethod;
	}
	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}
	public boolean isClob() {
		return clob;
	}
	public void setClob(boolean clob) {
		this.clob = clob;
	}
	public boolean isBlob() {
		return blob;
	}
	public void setBlob(boolean blob) {
		this.blob = blob;
	}

}