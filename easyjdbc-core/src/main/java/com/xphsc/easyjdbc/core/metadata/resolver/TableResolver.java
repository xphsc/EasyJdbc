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
package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.StringUtil;
import javax.persistence.UniqueConstraint;
import java.lang.annotation.Annotation;


/**
 * Table注解解析器
 * Created by ${huipei.x}
 */
public class TableResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		EntityElement entityElement = (EntityElement) element;
		Class<?> persistentClass = entityElement.getPersistentClass();
		Assert.isNull(persistentClass.getAnnotation(javax.persistence.MappedSuperclass.class),
				"实体：" + persistentClass.getName() + ",注解错误。 MappedSuperclass、Table两个注解不能同时用在一个类上");
		javax.persistence.Table table = (javax.persistence.Table) annotation;
		if (StringUtil.isNotBlank(table.name())) {
			entityElement.setTable(table.name());
		}
		entityElement.setCatalog(table.catalog());
		entityElement.setSchema(table.schema());
		UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
		entityElement.setUniqueConstraints(uniqueConstraints);
	}

}