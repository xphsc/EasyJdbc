package com.xphsc.easyjdbc.executor;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 *  实体查询执行器
 * Created by ${huipei.x}
 */
public class CountByExampleExecutor extends AbstractExecutor<Integer> {

	private String querySql;
	private SQL sqlBuilder;
	private  Object[] parameters;


	public CountByExampleExecutor(SQL sqlBuilder,JdbcTemplate jdbcTemplate,Object[] parameters) {
		super(jdbcTemplate);
		this.sqlBuilder=sqlBuilder  ;
		this.parameters=parameters;
	}


	@Override
	public void prepare() {
		if (!this.sqlBuilder.toString().startsWith("SELECT COUNT")){
			String countRexp = "(?i)^select (?:(?!select|from)[\\s\\S])*(\\(select (?:(?!from)[\\s\\S])* from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from";
			String replacement = "SELECT COUNT(1) AS COUNT FROM";
			this.querySql = this.sqlBuilder.toString().replaceFirst(countRexp, replacement);
		} else {
			this.querySql = this.sqlBuilder.toString();
		}
		logger.debug("SQL语句:["+querySql+"]");
	}

	@Override
	protected Integer doExecute() throws JdbcDataException {
		if(null==this.parameters||this.parameters.length==0){
			return this.jdbcTemplate.queryForObject(this.querySql,Integer.class);
		} else {
			return this.jdbcTemplate.queryForObject(this.querySql, this.parameters,Integer.class);
		}
	}


}