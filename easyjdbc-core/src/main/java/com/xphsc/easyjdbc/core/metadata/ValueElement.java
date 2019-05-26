/*
 * Copyright (c) 2018  huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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