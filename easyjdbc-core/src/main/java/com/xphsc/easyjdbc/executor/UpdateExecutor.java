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
import com.xphsc.easyjdbc.core.metadata.version.DefaultVersion;
import com.xphsc.easyjdbc.core.metadata.version.NextVersion;
import com.xphsc.easyjdbc.core.transform.setter.ValueSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.core.metadata.type.FillDateTypeHandler;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.Jdbcs;
import java.util.LinkedList;


/**
 *  更新执行器
 * Created by ${huipei.x}
 */
public class UpdateExecutor extends AbstractExecutor<Integer> {

	private final Object persistent;
	private final boolean ignoreNull;
	private final SQL sqlBuilder = SQL.BUILD();
	
	private LinkedList<ValueElement> valueElements;
	
	public <S> UpdateExecutor(LambdaSupplier<S> jdbcBuilder , Object persistent, boolean ignoreNull) {
		super(jdbcBuilder);
		this.persistent = persistent;
		this.ignoreNull = ignoreNull;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistent.getClass());
		EntityElement entityElement = ElementResolver.resolve(this.persistent.getClass());
		this.valueElements =new LinkedList();
		this.sqlBuilder.UPDATE(entityElement.getTable());
		FieldElement primaryKey = entityElement.getPrimaryKey();
		Object primaryKeyValue = Jdbcs.invokeMethod(this.persistent, primaryKey.getReadMethod()
				, "entity：" + entityElement.getName() + " Primary key：" + primaryKey.getName() + " Failure to obtain value");
		Assert.notNull(primaryKeyValue, "entity:" + entityElement.getName() + ", Primary key cannot be empty");
		FieldElement version = entityElement.getVersion();
		Object versionValue=null;
       if(version!=null){
		    versionValue = Jdbcs.invokeMethod(this.persistent, version.getReadMethod()
				   , "entity：" + entityElement.getName() + " version：" + version.getName() + " Failure to obtain value");
	  }
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			if(fieldElement.isPrimaryKey()) {
				continue;
			}

			Object value = Jdbcs.invokeMethod(this.persistent, fieldElement.getReadMethod()
					, "entity：" + entityElement.getName() + " field：" + fieldElement.getName() + "Failure to obtain value");
			if(ignoreNull && null == value) {
				continue;
			}
			if (fieldElement.isModifieDateField()) {
				value= FillDateTypeHandler.fillDate(fieldElement);
			}
			if(fieldElement.isVersion()) {
				NextVersion  nextVersion=new DefaultVersion();
				Object newVersion=nextVersion.nextVersion(versionValue);
				if(newVersion!=null){
					this.sqlBuilder.SET(version.getColumn()+"="+newVersion);
				}
			}else{
				this.sqlBuilder.SET(fieldElement.getColumn() + " = ?");
				this.valueElements.add(new ValueElement(value,fieldElement.isClob(), fieldElement.isBlob()));
			}

		}

		this.sqlBuilder.WHERE(primaryKey.getColumn() + " = ?");
		this.valueElements.add(new ValueElement(primaryKeyValue, primaryKey.isClob(), primaryKey.isBlob()));
		if(versionValue!=null){
			this.sqlBuilder.WHERE(version.getColumn() + " = ?");
			this.valueElements.add(new ValueElement(versionValue, primaryKey.isClob(), primaryKey.isBlob()));
		}


	}

	@Override
	protected Integer doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString();
		return this.jdbcBuilder.update(sql,new ValueSetter(LOBHANDLER,this.valueElements));
	}






}