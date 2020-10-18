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
import com.xphsc.easyjdbc.core.entity.InsertMode;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.transform.setter.ValueSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.core.metadata.type.FillDateTypeHandler;
import com.xphsc.easyjdbc.util.Jdbcs;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *  新增执行器
 * Created by ${huipei.x}
 */
public class InsertExecutor extends AbstractExecutor<Object> {

	private final Object persistent;
	private final SQL sqlBuilder = SQL.BUILD();
	private List<ValueElement> valueElements;
	private boolean returnKey=false;
	private Object primaryKey = null;
	private  String insertMode;
	public <S> InsertExecutor(LambdaSupplier<S> jdbcBuilder, Object persistent) {
		super(jdbcBuilder);
		this.persistent = persistent;
	}

	public <S> InsertExecutor(LambdaSupplier<S> jdbcBuilder, Object persistent, boolean returnKey) {
		super(jdbcBuilder);
		this.persistent = persistent;
		this.returnKey = returnKey;
	}
	public <S> InsertExecutor(LambdaSupplier<S> jdbcBuilder, Object persistent, InsertMode insertMode) {
		super(jdbcBuilder);
		this.persistent = persistent;
		this.insertMode = insertMode.name();
	}
	public <S> InsertExecutor(LambdaSupplier<S> jdbcBuilder, Object persistent,  boolean returnKey,InsertMode insertMode) {
		super(jdbcBuilder);
		this.persistent = persistent;
		this.returnKey = returnKey;
		this.insertMode = insertMode.name();
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
			if (fieldElement.isCreatedDateField()) {
				value=FillDateTypeHandler.fillDate(fieldElement);
			}
			if(StringUtil.isNotBlank(insertMode)){
				if(insertMode.equals(InsertMode.IGNORENULL.toString())&&null==value) {
					continue;
				}
			}

			this.sqlBuilder.VALUES(fieldElement.getColumn(), "?");
			this.valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
		}
	}

	@Override
	protected Object doExecute() throws JdbcDataException{
		final String sql = this.sqlBuilder.toString();
		if(returnKey){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcBuilder.update(new PreparedStatementCreator() {
									@Override
									public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
										PreparedStatement ps = con.prepareStatement(sql.toString(), 1);
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
			return this.jdbcBuilder.update(sql,new ValueSetter(LOBHANDLER,this.valueElements));
		}
	}

}