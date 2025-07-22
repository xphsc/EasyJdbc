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
package com.xphsc.easyjdbc.core.metadata.resolver;




import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.StringUtil;
import java.lang.annotation.Annotation;


/**
 * Column Annotation parser
 * Created by ${huipei.x}
 */
public class ColumnResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement) element;
		javax.persistence.Column column = (javax.persistence.Column) annotation;
		if (StringUtil.isNotBlank(column.name())) {
			fieldElement.setColumn(column.name());
		}
		fieldElement.setNullable(column.nullable());
		fieldElement.setUnique(column.unique());
		fieldElement.setLength(column.length());
		fieldElement.setColumnDefinition(column.columnDefinition());
		fieldElement.setInsertable(column.insertable());
		fieldElement.setUpdatable(column.updatable());
		fieldElement.setTable(column.table());
	}

}