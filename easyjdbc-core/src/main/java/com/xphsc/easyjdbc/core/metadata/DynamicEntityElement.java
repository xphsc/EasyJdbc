/*
 * Copyright (c) 2018-2019  huipei.x
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

import java.util.LinkedHashMap;
import java.util.Map;


/**
 *  Dynamic entity elements
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