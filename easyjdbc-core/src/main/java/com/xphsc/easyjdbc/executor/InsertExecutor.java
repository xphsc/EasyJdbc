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
import com.xphsc.easyjdbc.core.transform.setter.ValueSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.util.Jdbcs;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *  新增执行器
 * Created by ${huipei.x}
 */
public class InsertExecutor extends AbstractExecutor<Object> {

	private final Object persistent;
	private final SQL sqlBuilder = SQL.BUILD();
	private LinkedList<ValueElement> valueElements;
    boolean isReturnKey=false;
	Object primaryKey = null;
	public InsertExecutor(JdbcBuilder jdbcTemplate, Object persistent) {
		super(jdbcTemplate);
		this.persistent = persistent;
	}

	public InsertExecutor(JdbcBuilder jdbcTemplate, Object persistent, boolean isReturnKey) {
		super(jdbcTemplate);
		this.persistent = persistent;
		this.isReturnKey = isReturnKey;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistent.getClass());
		EntityElement entityElement = ElementResolver.resolve(this.persistent.getClass());
		this.valueElements =new LinkedList();
		this.sqlBuilder.INSERT_INTO(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			Object value = Jdbcs.invokeMethod(this.persistent, fieldElement.getReadMethod()
					, "entity：" + entityElement.getName() + " field：" + fieldElement.getName() + " Failure to obtain value");
			if (fieldElement.isPrimaryKey()) {
				value = super.generatedId(this.persistent,fieldElement, value);
				primaryKey=value;
			}
			this.sqlBuilder.VALUES(fieldElement.getColumn(), "?");
			this.valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
		}
	}

	@Override
	protected Object doExecute() throws JdbcDataException{
		final String sql = this.sqlBuilder.toString().toUpperCase();
		if(isReturnKey){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
									@Override
									public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
										PreparedStatement ps = con.prepareStatement(sql.toString(), new String[]{"id"});
										int i = 1;
										for (ValueElement object : valueElements) {
											ps.setObject(i, object.getValue());
											i++;
										}
										return ps;
									}
								},
					keyHolder);
			return keyHolder.getKey()!=null?keyHolder.getKey():primaryKey;
		}else{
			return this.jdbcTemplate.update(sql,new ValueSetter(LOBHANDLER,this.valueElements));
		}
	}



}