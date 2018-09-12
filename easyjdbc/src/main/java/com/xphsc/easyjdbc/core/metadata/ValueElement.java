package com.xphsc.easyjdbc.core.metadata;


/**
 *  值元素
 * Created by ${huipei.x}
 */
public class ValueElement implements Element {

	private static final long serialVersionUID = 5769556940509335112L;
	
	private Object value;
	private boolean clob;
	private boolean blob;
	
	public ValueElement(){}
	
	public ValueElement(Object value){
		this.value = value;
	}
	
	public ValueElement(Object value,boolean clob,boolean blob){
		this.value = value;
		this.clob = clob;
		this.blob = blob;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
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