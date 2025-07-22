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
package com.xphsc.easyjdbc.executor.example;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.parser.DefaultSQLParser;
import com.xphsc.easyjdbc.core.parser.SQLParser;
import com.xphsc.easyjdbc.executor.AbstractExecutor;
import com.xphsc.easyjdbc.util.Assert;


/**
 *  实体查询执行器
 * Created by ${huipei.x}
 */
public class CountByExampleExecutor extends AbstractExecutor<Long> {

	private String querySql;
	private SQL sqlBuilder;
	private Object[] parameters;


	public <S> CountByExampleExecutor(SQL sqlBuilder, LambdaSupplier<S> jdbcBuilder, Object[] parameters) {
		super(jdbcBuilder);
		this.sqlBuilder = sqlBuilder;
		this.parameters = parameters;
	}


	@Override
	public void prepare() {
		Assert.isTrue(!this.sqlBuilder.toString().contains("GROUP BY"), "The current SQL statement contains the default count (1) aggregate function, and there must be no GROUP BY!");
		if (!this.sqlBuilder.toString().startsWith("SELECT COUNT")) {
			String countRexp = "(?i)^select (?:(?!select|from)[\\s\\S])*(\\(select (?:(?!from)[\\s\\S])* from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from";
			String replacement = "SELECT COUNT(1) AS COUNT FROM";
			this.querySql = this.sqlBuilder.toString().replaceFirst(countRexp, replacement);
		} else {
			this.querySql = this.sqlBuilder.toString();
		}
		SQLParser sqlParser = new DefaultSQLParser();
		if (sqlParser.hasOrders(querySql)) {
			this.querySql = sqlParser.removeOrders(querySql);
		}

	}

	@Override
	protected Long doExecute() throws JdbcDataException {
		if (null == this.parameters || this.parameters.length == 0) {
			return this.jdbcBuilder.queryForObject(this.querySql, Long.class);
		} else {
			return this.jdbcBuilder.queryForObject(this.querySql, this.parameters, Long.class);
		}
	}


}