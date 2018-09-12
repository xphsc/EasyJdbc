package com.xphsc.easyjdbc.executor;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 *  实体查询执行器
 * Created by ${huipei.x}
 */
public class CountExecutor extends AbstractExecutor<Integer> {

	private final String sql;
	private final Object[] parameters;
	private String querySql;
	
	public CountExecutor(JdbcTemplate jdbcTemplate, String sql, Object[] parameters) {
		super(jdbcTemplate);
		this.sql = sql;
		this.parameters = parameters;
	}

	public CountExecutor(JdbcTemplate jdbcTemplate, Class<?> persistentClass) {
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