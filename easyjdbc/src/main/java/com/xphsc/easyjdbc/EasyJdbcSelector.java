package com.xphsc.easyjdbc;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.executor.CountExecutor;
import com.xphsc.easyjdbc.executor.FindExecutor;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by ${huipei.x}
 */
public class EasyJdbcSelector {

	private final JdbcTemplate jdbcTemplate;
	private final String dialectName;

	protected EasyJdbcSelector(JdbcTemplate jdbcTemplate, String dialectName){
		this.jdbcTemplate = jdbcTemplate;
		this.dialectName = dialectName;
	}

	private EasyJdbcSelector getSelf(){
		 return this;
	}
	
	private String sql;
	private int startRow;
	private int limit;
	private Class<?> entityClass;
	private PageInfo<?> pageInfo=new PageInfo();
	private final Map<String,String> mappings = new HashMap();
	private final LinkedList<Object> parameters = new LinkedList<Object>();

	/**
	 * 实体类型
	 * @param entityClass 实体类型
	 */
	public EasyJdbcSelector entityClass(Class<?> entityClass){
		this.entityClass = entityClass;
		return getSelf();
	}

	public EasyJdbcSelector pageInfo(PageInfo<?> pageInfo){
		Assert.isTrue(pageInfo.getPageNum() >= 1, "pageNum必须大于等于1");
		Assert.isTrue(pageInfo.getPageSize() > 0, "pageSize必要大于0");
		this.pageInfo = pageInfo;
		return getSelf();
	}

	public EasyJdbcSelector pageInfo(int pageNum,int pageSize){
		Assert.isTrue(pageNum >= 1, "pageNum必须大于等于1");
		Assert.isTrue(pageSize > 0, "pageSize必要大于0");
		this.pageInfo.pageNum = pageNum;
		this.pageInfo.pageSize = pageSize;
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
	public EasyJdbcSelector startRow(int startRow){
		Assert.isTrue(startRow>=0, "startRow必须大于等于0");
		this.startRow = startRow;
		return getSelf();
	}

	/**
	 * 查询条数
	 * @param limit
	 */
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
				,parameters.toArray(),this.mappings,startRow,limit);
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		return results;
	}

	public <T> PageInfo<T> page() throws JdbcDataException{
		Assert.notNull(entityClass, "实体类型不能为空");
		if(StringUtil.isBlank(this.sql)) {
			this.sql = this.sqlBuilder.toString();
		}
		Assert.hasText(sql, "SQL语句不能为空");
		FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(
				jdbcTemplate, dialectName,entityClass,sql
				,parameters.toArray(),this.mappings,pageInfo);
		List<T> results = executor.execute();
		executor = null;//hlep gc.
		long total=count();
		return new PageInfoImpl<T>(results,total,pageInfo.getPageNum(),pageInfo.getPageSize());
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