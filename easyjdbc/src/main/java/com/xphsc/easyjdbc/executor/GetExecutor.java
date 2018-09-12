package com.xphsc.easyjdbc.executor;




import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.transform.EntityRowMapper;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 *  根据ID 查询单条记录的执行器
 * Created by ${huipei.x}
 */
public class GetExecutor<T> extends AbstractExecutor<T> {

	private final Class<?> persistentClass;
	private final Object primaryKeyValue;
	private final SQL sqlBuilder = SQL.BUILD();
	private EntityElement entityElement;

	public GetExecutor(JdbcTemplate jdbcTemplate, Class<?> persistentClass, Object primaryKeyValue) {
		super(jdbcTemplate);
		this.persistentClass = persistentClass;
		this.primaryKeyValue = primaryKeyValue;
	}

	@Override
	public void prepare() {
		this.checkEntity(this.persistentClass);
		this.entityElement = ElementResolver.resolve(this.persistentClass);
		this.sqlBuilder.FROM(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			this.sqlBuilder.SELECT(fieldElement.getColumn());
		}
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn() + " = ?");
	}

	@Override
	protected T doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString().toUpperCase();
		logger.debug("SQL语句:["+sql+"]");
		return this.jdbcTemplate.queryForObject(sql,new EntityRowMapper<T>(LOBHANDLER,this.entityElement,this.persistentClass),this.primaryKeyValue);
	}


}