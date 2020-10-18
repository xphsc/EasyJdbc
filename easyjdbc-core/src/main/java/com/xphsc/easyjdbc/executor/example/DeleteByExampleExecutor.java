/*
 * Copyright (c) 2018-2019 huipei.x
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
package com.xphsc.easyjdbc.executor.example;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.executor.AbstractExecutor;
import com.xphsc.easyjdbc.util.Assert;


/**
 *  实体删除执行器
 * Created by ${huipei.x}
 */
public class DeleteByExampleExecutor extends AbstractExecutor<Integer> {


	private SQL sqlBuilder;
	private final Class<?> persistentClass;
	private  Object[] parameters;
	public <S> DeleteByExampleExecutor(LambdaSupplier<S> jdbcBuilder,SQL applyWhere,Object[] parameters,Class<?> persistentClass) {
		super(jdbcBuilder);
		this.sqlBuilder = applyWhere;
		this.persistentClass=persistentClass;
		this.parameters=parameters;
	}


	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		EntityElement entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.DELETE_FROM(entityElement.getTable());
	}


	@Override
	protected Integer doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		Assert.isTrue(sql.contains("WHERE"),"Delete must have where condition!");
		return this.jdbcBuilder.update(sql, this.parameters);
	}

}