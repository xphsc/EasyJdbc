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
package com.xphsc.easyjdbc;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.executor.CountExecutor;
import com.xphsc.easyjdbc.executor.FindExecutor;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by ${huipei.x}
 */
public class EasyJdbcSelector {

	private final JdbcBuilder jdbcTemplate;
	private final String dialectName;
	private String sql;
	private Integer offset;
	private Integer limit;
	private Class<?> entityClass;
	private PageInfo<?> pageInfo;
	private  Map<String,String> mappings ;
	private final LinkedList<Object> parameters;

	protected EasyJdbcSelector(JdbcBuilder jdbcTemplate, String dialectName){
		this.jdbcTemplate = jdbcTemplate;
		this.dialectName = dialectName;
		parameters= new LinkedList<Object>();
	}

	private EasyJdbcSelector getSelf(){
		 return this;
	}
	

	/**
	 * 实体类型
	 * @param entityClass 实体类型
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
		Assert.isTrue(limit > 0, "PageSize must be greater than 0");
		this.offset = offset;
		this.limit=limit;
		return getSelf();
	}

	/**
	 * 列<---->类字段 映射
	 * @param column 列名
	 * @param field 类字段名
	 */
	public EasyJdbcSelector mapping(String column,String field){
		Assert.hasText(column, "映射的列不能为空");
		Assert.hasText(field, "映射的属性不能为空");
		if(mappings==null){
			mappings= new HashMap();
		}
		this.mappings.put(field,column);
		return getSelf();
	}
	
	/**
	 * 查询SQL
	 * @param sql SQL语句
	 */
	public EasyJdbcSelector sql(String sql){
		this.sql = sql;
		return getSelf();
	}
	
	/**
	 * 参数
	 * @param parameter 参数
	 */
	public EasyJdbcSelector parameter(Object parameter) {
		Assert.notNull(parameters, "参数不能为空");
		this.parameters.add(parameter);
		return getSelf();
	}
	
	/**
	 * 参数
	 * @param parameters 参数
	 */
	public EasyJdbcSelector parameters(Object... parameters) {
		Assert.notNull(parameters, "参数不能为空");
		for(Object parameter:parameters) {
			this.parameters.add(parameter);
		}
		return getSelf();
	}

	/**
	 * 起始行
	 * @param startRow
	 */
	@Deprecated
	public EasyJdbcSelector startRow(int startRow){
		Assert.isTrue(startRow>=0, "startRow必须大于等于0");
		this.offset = startRow;
		return getSelf();
	}

	/**
	 * 查询条数
	 * @param limit
	 */
	@Deprecated
	public EasyJdbcSelector limit(int limit){
		Assert.isTrue(limit>0, "limit必须要大于0");
		this.limit = limit;
		return getSelf();
	}

	/**
	 * 获取单个实体
	 */
	public <T> T get() throws JdbcDataException{
		Assert.notNull(entityClass, "实体类型不能为空");
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL语句不能为空");
		FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,dialectName,entityClass,sql,parameters.toArray());
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		int size = (results != null ? results.size() : 0);
		if(size>1){
			new IncorrectResultSizeDataAccessException(1, size);
		}
		if(size==1){
			return results.get(0);
		}
		return null;
	}
	/**
	 * 查询实体列表
	 */

	public <T> List<T>  list() throws JdbcDataException{
		Assert.notNull(entityClass, "实体类型不能为空");
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL语句不能为空");
		FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(
				jdbcTemplate, dialectName,entityClass,sql
				,parameters.toArray(),this.mappings,offset,limit);
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		return results;
	}

	public <T> PageInfo<T> page() throws JdbcDataException{
		Assert.notNull(entityClass, "实体类型不能为空");
		List<T> results=null;
		long total=1L;
		if(offset==null&&limit==null){
			if(pageInfo==null){
				pageInfo=new PageInfo();
			}
			if(StringUtil.isBlank(this.sql)) {
				this.sql = this.sqlBuilder.toString();
			}
			Assert.hasText(sql, "SQL语句不能为空");
			FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(
					jdbcTemplate, dialectName,entityClass,sql
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
	 * 查询实体数量
	 * @return 条目数
	 */
	public int  count() throws JdbcDataException {
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL语句不能为空");
		CountExecutor executor =  new CountExecutor(jdbcTemplate,sql,parameters.toArray());
		int count = executor.execute();
		executor = null;//hlep gc.
		return count;
	}

	// ================= 构建SQL
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

}