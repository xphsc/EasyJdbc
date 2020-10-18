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


import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.lambda.Reflections;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.IdGenerators;
import com.xphsc.easyjdbc.core.metadata.id.SnowFlakeId;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.Jdbcs;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import java.util.UUID;

/**
 *  Abstract Executor
 * Created by ${huipei.x}
 */
public abstract class AbstractExecutor<T> implements Executor<T> {
	protected static final LobHandler LOBHANDLER = new DefaultLobHandler();
	protected final JdbcBuilder jdbcBuilder;
	public <S> AbstractExecutor(LambdaSupplier<S> jdbcBuilder){
		this.jdbcBuilder = Reflections.classForLambdaSupplier(jdbcBuilder);
	}

	@Override
	public T execute() throws JdbcDataException {
		prepare();
		return doExecute();
	}


	protected abstract void prepare();
	protected abstract T doExecute() throws JdbcDataException;
	protected Object generatedId(Object persistent,FieldElement fieldElement,Object value){
		Object id=null;
		if((null == value||"".equals(value)||value.equals(0)||value.equals(0L))
			&& fieldElement.isGeneratedValue()
			){
			if(IdGenerators.UUID.equals(fieldElement.getGenerator())){
				id = UUID.randomUUID().toString().replaceAll("-", "");
				String errorMsg = "entity："+persistent.getClass().getName()+" Primary key："+fieldElement.getName()+" Setting value failed";
				Jdbcs.invokeMethod(persistent, fieldElement.getWriteMethod(), errorMsg, id);
			}else if(IdGenerators.SNOWFLAKEID.equals(fieldElement.getGenerator())){
				SnowFlakeId snowFlake = new SnowFlakeId(2, 3);
				String errorMsg = "entity："+persistent.getClass().getName()+" Primary key："+fieldElement.getName()+" Setting value failed";
				if("java.lang.Integer".equals(fieldElement.getType().getName())){
				  Integer entityId=new Long(snowFlake.nextId()).intValue();
					id=entityId;
				} else if("int".equals(fieldElement.getType().getName())){
					int entityId=new Long(snowFlake.nextId()).intValue();
					id=entityId;
				}
				else if("java.lang.String".equals(fieldElement.getType().getName())){
					String entityId=new Long(snowFlake.nextId()).toString();
					id=entityId;
				}else{
					id=snowFlake.nextId();
				}
				Jdbcs.invokeMethod(persistent, fieldElement.getWriteMethod(), errorMsg, id);
			}

			return id;
		}
		return value;
	}
	
	protected boolean isEntity(Class<?> persistentClass){
		return null != persistentClass.getAnnotation(javax.persistence.Entity.class);	
	}
	
	protected void checkEntity(Class<?> persistentClass){
		Assert.isTrue(isEntity(persistentClass), persistentClass + " If it is an entity type, use the @Entity annotation for identification");
	}



}