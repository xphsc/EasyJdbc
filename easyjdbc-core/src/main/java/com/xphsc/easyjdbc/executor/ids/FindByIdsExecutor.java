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
package com.xphsc.easyjdbc.executor.ids;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.executor.AbstractExecutor;
import com.xphsc.easyjdbc.core.transform.EntityRowMapper;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;


/**
 *  根据ids 查询全部记录的执行器
 * Created by ${huipei.x}
 */
public class FindByIdsExecutor<T> extends AbstractExecutor<T> {

	private final Class<?> persistentClass;
	private final Iterable primaryKeyValues;
	private final SQL sqlBuilder = SQL.BUILD();
	private EntityElement entityElement;

	public FindByIdsExecutor(JdbcBuilder jdbcTemplate, Class<?> persistentClass, Iterable values) {
		super(jdbcTemplate);
		this.persistentClass = persistentClass;
		this.primaryKeyValues = values;
	}

	@Override
	public void prepare() {
		StringBuilder sb = new StringBuilder();
		this.checkEntity(this.persistentClass);
		this.entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.FROM(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			this.sqlBuilder.SELECT(fieldElement.getColumn());
		}
		sb.append(entityElement.getPrimaryKey().getColumn() + " in ");
		String inValues="";
		for (Object value : primaryKeyValues) {
			inValues+="'"+value+"',";
		}
		inValues = inValues.substring(0, inValues.length()-1);
		sb.append(" (" + inValues+")");
		this.sqlBuilder.WHERE(sb.toString());
	}

	@Override
	protected T doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		return (T) this.jdbcTemplate.query(sql,new EntityRowMapper<T>(LOBHANDLER,this.entityElement,this.persistentClass));
	}


}