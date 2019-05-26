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
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.core.transform.DynamicEntityRowMapper;
import com.xphsc.easyjdbc.core.transform.EntityRowMapper;
import com.xphsc.easyjdbc.core.metadata.DynamicEntityElement;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.page.PageRowBounds;
import org.springframework.jdbc.core.RowMapper;
import java.util.Map;


/**
 *   实体查询执行器
 * Created by ${huipei.x}
 */
public class FindExecutor<E> extends AbstractExecutor<E> {

	private final String dialectName;
	private final Class<?> persistentClass;
	private final String sql;
	private final Integer startRow;
	private final Integer limit;
	private final Map<String,String> dynamicMappings;
	private final Object[] parameters;
	private String querySql;
	private EntityElement entityElement;
	private DynamicEntityElement dynamicEntityElement;
	private boolean isDynamic;
	
	public FindExecutor(JdbcBuilder jdbcTemplate, String dialectName, Class<?> persistentClass, String sql) {
		this(jdbcTemplate, dialectName,persistentClass,sql,null,null,null,null);
	}
	
	public FindExecutor(JdbcBuilder jdbcTemplate, String dialectName
			, Class<?> persistentClass, String sql, Object[] parameters) {
		this(jdbcTemplate, dialectName,persistentClass,sql,parameters,null,null,null);
	}
	
	public FindExecutor(JdbcBuilder jdbcTemplate, String dialectName
			, Class<?> persistentClass, String sql, Object[] parameters
			, Map<String, String> dynamicMappings, Integer startRow, Integer limit) {
		super(jdbcTemplate);
		this.dialectName = dialectName;
		this.persistentClass = persistentClass;
		this.sql = sql;
		this.parameters = parameters;
		this.dynamicMappings = dynamicMappings;
		this.startRow = startRow;
		this.limit = limit;
	}

	public FindExecutor(JdbcBuilder jdbcTemplate, String dialectName
			, Class<?> persistentClass, String sql, Object[] parameters
			, Map<String, String> dynamicMappings, PageInfo page) {
		super(jdbcTemplate);
		this.dialectName = dialectName;
		this.persistentClass = persistentClass;
		this.sql = sql;
		this.parameters = parameters;
		this.dynamicMappings = dynamicMappings;
		this.startRow = page.getPageNum()>1?(page.getPageNum()-1)*page.getPageSize():page.getOffset();
		this.limit =  page.getPageSize()>0?page.getPageSize():page.getLimit();
	}

	@Override
	public void prepare() {
		if(this.isEntity(this.persistentClass)){
			this.isDynamic = false;
			this.entityElement = ElementResolver.resolve(this.persistentClass);
		} else {
			this.isDynamic = true;
			this.dynamicEntityElement = ElementResolver.resolveDynamic(this.persistentClass, this.dynamicMappings);
		}
		if(null!=startRow&&-1!=startRow&&null!=this.limit&&this.limit>0){
			this.querySql = PageRowBounds.pagination(dialectName, this.sql, startRow, this.limit);
		} else {
			this.querySql  = this.sql;
		}
	}

	@Override
	@SuppressWarnings("all")
	protected E doExecute() throws JdbcDataException {
		RowMapper rowMapper = null;
		if(this.isDynamic){
			rowMapper = new DynamicEntityRowMapper(LOBHANDLER,this.dynamicEntityElement,this.persistentClass);
		} else {
			rowMapper = new EntityRowMapper(LOBHANDLER,this.entityElement,this.persistentClass);
		}
		if(null==this.parameters||this.parameters.length==0){
			return (E) this.jdbcTemplate.query(this.querySql,rowMapper);
		} else {
			return (E) this.jdbcTemplate.query(this.querySql,this.parameters,rowMapper);
		}

	}

}