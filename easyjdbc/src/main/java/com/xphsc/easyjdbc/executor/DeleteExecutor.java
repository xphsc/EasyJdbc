package com.xphsc.easyjdbc.executor;




import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *   删除执行器
 * Created by ${huipei.x}
 */
public class DeleteExecutor extends AbstractExecutor<Integer> {
	
	private final Class<?> persistentClass;
	private final Object primaryKeyValue;
	private final SQL sqlBuilder = SQL.BUILD();

	public DeleteExecutor(JdbcTemplate jdbcTemplate, Class<?> persistentClass, Object primaryKeyValue) {
		super(jdbcTemplate);
		this.persistentClass = persistentClass;
		this.primaryKeyValue = primaryKeyValue;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		EntityElement entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.DELETE_FROM(entityElement.getTable());
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn()+" = ?");
	}

	@Override
	protected Integer doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString().toUpperCase();
		logger.debug("SQL语句:["+sql+"]");
		return this.jdbcTemplate.update(sql,this.primaryKeyValue);
	}


}