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
import com.xphsc.easyjdbc.core.transform.EntityRowMapper;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;



/**
 *  根据ID 查询单条记录的执行器
 * Created by ${huipei.x}
 */
public class GetExecutor<T> extends AbstractExecutor<T> {

	private final Class<?> persistentClass;
	private final Object primaryKeyValue;
	private final SQL sqlBuilder = SQL.BUILD();
	private EntityElement entityElement;

	public <S> GetExecutor(LambdaSupplier<S> jdbcBuilder, Class<?> persistentClass, Object primaryKeyValue) {
		super(jdbcBuilder);
		this.persistentClass = persistentClass;
		this.primaryKeyValue = primaryKeyValue;
	}


	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		this.entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.FROM(entityElement.getTable());
		for (FieldElement fieldElement : entityElement.getFieldElements().values()) {
			if (fieldElement.isTransientField()) {
				continue;
			}
			this.sqlBuilder.SELECT(fieldElement.getColumn());
		}
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn() + " = ?");
	}

	@Override
	protected T doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		return this.jdbcBuilder.queryForObject(sql, new EntityRowMapper<T>(LOBHANDLER, this.entityElement, this.persistentClass), this.primaryKeyValue);
	}


}