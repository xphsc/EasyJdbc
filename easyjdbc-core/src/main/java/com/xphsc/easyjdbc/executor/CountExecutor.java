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
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.parser.DefaultSQLParser;
import com.xphsc.easyjdbc.core.parser.SQLParser;



/**
 *  实体查询执行器
 * Created by ${huipei.x}
 */
public class CountExecutor extends AbstractExecutor<Integer> {

	private final String sql;
	private final Object[] parameters;
	private String querySql;
	
	public <S> CountExecutor(LambdaSupplier<S> jdbcBuilder, String sql, Object[] parameters) {
		super(jdbcBuilder);
		this.sql = sql;
		this.parameters = parameters;
	}

	public <S> CountExecutor(LambdaSupplier<S> jdbcTemplate, Class<?> persistentClass) {
		super(jdbcTemplate);
		this.checkEntity(persistentClass);
		EntityElement entityElement= ElementResolver.resolve(persistentClass);
		SQL sqlBuilder = SQL.BUILD().FROM(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			sqlBuilder.SELECT(fieldElement.getColumn());
		}
		this.sql = sqlBuilder.toString();
		this.parameters = null;
	}


	@Override
	public void prepare() {
		if (!this.sql.startsWith("SELECT COUNT")){
			String countRexp = "(?i)^select (?:(?!select|from)[\\s\\S])*(\\(select (?:(?!from)[\\s\\S])* from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from";
			String replacement = "SELECT COUNT(1) AS COUNT FROM";
			this.querySql = this.sql.replaceFirst(countRexp, replacement);
		}
		else {
			this.querySql = this.sql;
		}
		SQLParser sqlParser=new DefaultSQLParser();
		if(sqlParser.hasOrders(querySql)){
			this.querySql=sqlParser.removeOrders(querySql);
		}
	}



	@Override
	protected Integer doExecute() throws JdbcDataException {
		if(null==this.parameters||this.parameters.length==0){
			return this.jdbcBuilder.queryForObject(this.querySql,Integer.class);
		} else {
			return this.jdbcBuilder.queryForObject(this.querySql, this.parameters,Integer.class);
		}
	}


}