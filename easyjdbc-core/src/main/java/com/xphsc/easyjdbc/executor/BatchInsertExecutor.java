/*
 * Copyright (c) 2018 huipei.x
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
package com.xphsc.easyjdbc.executor;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.transform.setter.ValueBatchSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.util.Jdbcs;

import java.util.LinkedList;
import java.util.List;


/**
 *  批量新增执行器
 * Created by ${huipei.x}
 */
public class BatchInsertExecutor extends AbstractExecutor<int[]> {

	private final LinkedList persistents = new LinkedList();
	private final SQL sqlBuilder = SQL.BUILD();
	private List<LinkedList<ValueElement>> batchValueElements;
	
	public <S> BatchInsertExecutor(LambdaSupplier<S> jdbcBuilder, List<?> persistents) {
		super(jdbcBuilder);
		this.persistents.addAll(persistents);
	}

	@Override
	public void prepare() {
		Class<?> persistentClass = this.persistents.get(0).getClass();
		this.checkEntity(persistentClass);
		EntityElement entityElement = ElementResolver.resolve(persistentClass);
		this.batchValueElements = new LinkedList();
		this.sqlBuilder.INSERT_INTO(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if (fieldElement.isTransientField()) {
				continue;
			}
			this.sqlBuilder.VALUES(fieldElement.getColumn(), "?");
		}
		for (Object persistent : persistents) {
			LinkedList<ValueElement> valueElements =new LinkedList();
			for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
				if(fieldElement.isTransientField()) {
					continue;
				}
				Object value = Jdbcs.invokeMethod(persistent, fieldElement.getReadMethod()
						, "entity：" + entityElement.getName() + " field：" + fieldElement.getName() + " Failure to obtain value");
				if(fieldElement.isPrimaryKey()) {
					value = super.generatedId(persistent,fieldElement, value);
				}
				valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
			}
			this.batchValueElements.add(valueElements);
		}
	}

	@Override
	protected int[] doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		return this.jdbcBuilder.batchUpdate(sql,new ValueBatchSetter(LOBHANDLER,this.batchValueElements));
	}

}