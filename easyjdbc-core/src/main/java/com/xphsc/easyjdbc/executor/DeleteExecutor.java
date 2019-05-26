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
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;

/**
 *   删除执行器
 * Created by ${huipei.x}
 */
public class DeleteExecutor extends AbstractExecutor<Integer> {
	
	private final Class<?> persistentClass;
	private final Object primaryKeyValue;
	private final SQL sqlBuilder = SQL.BUILD();

	public DeleteExecutor(JdbcBuilder jdbcTemplate, Class<?> persistentClass, Object primaryKeyValue) {
		super(jdbcTemplate);
		this.persistentClass = persistentClass;
		this.primaryKeyValue = primaryKeyValue;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		EntityElement entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.DELETE_FROM(entityElement.getTable());
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn()+" = ?");
	}

	@Override
	protected Integer doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		return this.jdbcTemplate.update(sql,this.primaryKeyValue);
	}


}