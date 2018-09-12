package com.xphsc.easyjdbc.executor;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.transform.setter.ValueSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.EasyJdbcHelper;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	public UpdateExecutor(JdbcTemplate jdbcTemplate, Object persistent, boolean ignoreNull) {
		super(jdbcTemplate);
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
		Object primaryKeyValue = EasyJdbcHelper.invokeMethod(this.persistent, primaryKey.getReadMethod()
				, "实体：" + entityElement.getName() + " 主键：" + primaryKey.getName() + " 获取值失败");
		Assert.notNull(primaryKeyValue, "实体:" + entityElement.getName() + ", 主键不能为空");
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			if(fieldElement.isPrimaryKey()) {
				continue;
			}
			Object value = EasyJdbcHelper.invokeMethod(this.persistent, fieldElement.getReadMethod()
					, "实体：" + entityElement.getName() + " 字段：" + fieldElement.getName() + " 获取值失败");
			if(ignoreNull && null == value) {
				continue;
			}
			this.sqlBuilder.SET(fieldElement.getColumn() + " = ?");
			this.valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
		}
		this.sqlBuilder.WHERE(primaryKey.getColumn() + " = ?");
		this.valueElements.add(new ValueElement(primaryKeyValue,primaryKey.isClob(),primaryKey.isBlob()));
	}

	@Override
	protected Integer doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString().toUpperCase();
		logger.debug("SQL语句:["+sql+"]");
		return this.jdbcTemplate.update(sql,new ValueSetter(LOBHANDLER,this.valueElements));
	}

}