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
package com.xphsc.easyjdbc;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.lambda.Reflections;
import com.xphsc.easyjdbc.core.lambda.StringSupplier;
import com.xphsc.easyjdbc.executor.CountExecutor;
import com.xphsc.easyjdbc.executor.FindExecutor;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.Collects;
import com.xphsc.easyjdbc.util.StringUtil;
import java.util.*;


/**
 * Created by ${huipei.x}
 */
public class EasyJdbcSelector {

	private final JdbcBuilder jdbcBuilder;
	private final String dialectName;
	private String sql;
	private Integer offset;
	private Integer limit;
	private Class<?> entityClass;
	private PageInfo<?> pageInfo;
	private  Map<String,String> mappings ;
	private final LinkedList<Object> parameters;

	protected <T> EasyJdbcSelector(LambdaSupplier<T> jdbcBuilder, StringSupplier dialectName){
		this.jdbcBuilder= Reflections.classForLambdaSupplier(jdbcBuilder);
		this.dialectName=dialectName.get();
		parameters= new LinkedList<Object>();
	}

	private EasyJdbcSelector getSelf(){
		 return this;
	}
	

	/**
	 * Entity type
	 * @param entityClass Entity type
	 */
	public EasyJdbcSelector entityClass(Class<?> entityClass){
		this.entityClass = entityClass;
		return getSelf();
	}


	public EasyJdbcSelector pageInfo(int pageNum,int pageSize){
		Assert.isTrue(pageNum >= 1, "PageNum must be greater than or equal to 1");
		Assert.isTrue(pageSize > 0, "PageSize must be greater than 0");
		if(pageInfo==null){
			pageInfo=new PageInfo();
		}
		this.pageInfo.pageNum = pageNum;
		this.pageInfo.pageSize = pageSize;
		return getSelf();
	}

   public EasyJdbcSelector offsetPage(int  offset,int limit){
		Assert.isTrue(offset >= 0, "Offset must be greater than or equal to 0");
		Assert.isTrue(limit > 0, "Limit must be greater than 0");
		this.offset = offset;
		this.limit=limit;
		return getSelf();
	}

	/**
	 * Column <--> Class Field Mapping
	 * @param column Column names
	 * @param field Class field name
	 */
	public EasyJdbcSelector mapping(String column,String field){
		Assert.hasText(column, "The column of the mapping cannot be empty");
		Assert.hasText(field, "Mapping attributes cannot be empty");
		if(mappings==null){
			mappings= new HashMap();
		}
		this.mappings.put(field,column);
		return getSelf();
	}
	
	/**
	 * Query SQL
	 * @param sql SQL statement
	 */
	public EasyJdbcSelector sql(String sql){
		this.sql = sql;
		return getSelf();
	}
	
	/**
	 * parameter
	 * @param parameter parameter
	 */
	public EasyJdbcSelector parameter(Object parameter) {
		Assert.notNull(parameter, "Parameters cannot be null");
		this.parameters.add(parameter);
		return getSelf();
	}
	
	/**
	 * parameter
	 * @param parameters parameter
	 */
	public EasyJdbcSelector parameters(Object... parameters) {
		Assert.notNull(parameters, "Parameters cannot be null");
		for(Object parameter:parameters) {
			this.parameters.add(parameter);
		}
		return getSelf();
	}

	/**
	 * Start line
	 * @param startRow
	 */
	@Deprecated
	public EasyJdbcSelector startRow(int startRow){
		Assert.isTrue(startRow>=0, "StartRow must be greater than or equal to 0");
		this.offset = startRow;
		return getSelf();
	}

	/**
	 *  limit
	 * @param limit
	 */
	@Deprecated
	public EasyJdbcSelector limit(int limit){
		Assert.isTrue(limit>0, "Limit must be greater than 0");
		this.limit = limit;
		return getSelf();
	}

	/**
	 * Getting a single entity
	 */
	public <T> T get() throws JdbcDataException{
		Assert.notNull(entityClass, "Entity type cannot be empty");
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL statement cannot be empty");
		FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(this::getJdbcBuilder,dialectName,entityClass,sql,parameters.toArray());
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		return Collects.isNotEmpty(results)?results.get(0): null;
	}

	/**
	 * get a single entity
	 */
	public <T> Optional<T> getOne() throws JdbcDataException{
		return Optional.ofNullable(get());
	}

	/**
	 * Query Entity List
	 */

	public <T> List<T>  list() throws JdbcDataException{
		Assert.notNull(entityClass, "Entity type cannot be empty");
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL statement cannot be empty");
		FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(
				this::getJdbcBuilder, dialectName,entityClass,sql
				,parameters.toArray(),this.mappings,offset,limit);
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		return results;
	}

	public <T> PageInfo<T> page() throws JdbcDataException{
		Assert.notNull(entityClass, "Entity type cannot be empty");
		List<T> results=null;
		long total=1L;
		if(offset==null&&limit==null){
			if(pageInfo==null){
				pageInfo=new PageInfo();
			}
			if(StringUtil.isBlank(this.sql)) {
				this.sql = this.sqlBuilder.toString();
			}
			Assert.hasText(sql, "SQL statement cannot be empty");
			FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(
					this::getJdbcBuilder, dialectName,entityClass,sql
					,parameters.toArray(),this.mappings,pageInfo);
			 results = executor.execute();
			 total=count();
			return new PageInfoImpl<T>(results,total,pageInfo.getPageNum(),pageInfo.getPageSize());
		}else{
			int pageNum=(int) Math.ceil((double) ((offset +limit) / limit));
			int pageSize=limit;
			return new PageInfoImpl<T>(results,total,pageNum,pageSize);
		}

	}

	/**
	 * Number of query entities
	 * @return Number of entries
	 */
	public long  count() throws JdbcDataException {
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL statement cannot be empty");
		CountExecutor executor =  new CountExecutor(this::getJdbcBuilder,sql,parameters.toArray());
		long count = executor.execute();
		executor = null;//hlep gc.
		return count;
	}

	// ================= Building SQL
	private final SQL sqlBuilder = SQL.BUILD();
	
	public EasyJdbcSelector SELECT(String columns) {
		this.sqlBuilder.SELECT(columns);
		return getSelf();
	}

	public EasyJdbcSelector SELECT_DISTINCT(String columns) {
		this.sqlBuilder.SELECT_DISTINCT(columns);
		return getSelf();
	}

	public EasyJdbcSelector FROM(String table) {
		this.sqlBuilder.FROM(table);
		return getSelf();
	}

	public EasyJdbcSelector JOIN(String join) {
		this.sqlBuilder.JOIN(join);
		return getSelf();
	}

	public EasyJdbcSelector INNER_JOIN(String join) {
		this.sqlBuilder.INNER_JOIN(join);
		return getSelf();
	}

	public EasyJdbcSelector LEFT_OUTER_JOIN(String join) {
		this.sqlBuilder.LEFT_OUTER_JOIN(join);
		return getSelf();
	}

	public EasyJdbcSelector RIGHT_OUTER_JOIN(String join) {
		this.sqlBuilder.RIGHT_OUTER_JOIN(join);
		return getSelf();
	}

	public EasyJdbcSelector OUTER_JOIN(String join) {
		this.sqlBuilder.OUTER_JOIN(join);
		return getSelf();
	}

	public EasyJdbcSelector WHERE(String conditions) {
		this.sqlBuilder.WHERE(conditions);
		return getSelf();
	}

	public EasyJdbcSelector OR() {
		this.sqlBuilder.OR();
		return getSelf();
	}

	public EasyJdbcSelector AND() {
		this.sqlBuilder.AND();
		return getSelf();
	}

	public EasyJdbcSelector GROUP_BY(String columns) {
		this.sqlBuilder.GROUP_BY(columns);
		return getSelf();
	}

	public EasyJdbcSelector HAVING(String conditions) {
		this.sqlBuilder.HAVING(conditions);
		return getSelf();
	}

	public EasyJdbcSelector ORDER_BY(String columns) {
		this.sqlBuilder.ORDER_BY(columns);
		return getSelf();
	}

	private JdbcBuilder getJdbcBuilder() {
		return jdbcBuilder;
	}
}