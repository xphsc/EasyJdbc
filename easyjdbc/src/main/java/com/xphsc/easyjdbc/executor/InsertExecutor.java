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
 *  新增执行器
 * Created by ${huipei.x}
 */
public class InsertExecutor extends AbstractExecutor<Integer> {

	private final Object persistent;
	private final SQL sqlBuilder = SQL.BUILD();
	private LinkedList<ValueElement> valueElements;

	public InsertExecutor(JdbcTemplate jdbcTemplate, Object persistent) {
		super(jdbcTemplate);
		this.persistent = persistent;
	}
	
	@Override
	public void prepare() {
		this.checkEntity(this.persistent.getClass());
		EntityElement entityElement = ElementResolver.resolve(this.persistent.getClass());
		this.valueElements =new LinkedList();
		this.sqlBuilder.INSERT_INTO(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			Object value = EasyJdbcHelper.invokeMethod(this.persistent, fieldElement.getReadMethod()
					, "实体：" + entityElement.getName() + " 字段：" + fieldElement.getName() + " 获取值失败");
			if (fieldElement.isPrimaryKey()) {
				value = super.generatedId(this.persistent,fieldElement, value);
				Assert.notNull(value, "实体:" + entityElement.getName() + ", 主键不能为空");
			}
			this.sqlBuilder.VALUES(fieldElement.getColumn(), "?");
			this.valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
		}
	}

	@Override
	protected Integer doExecute() throws JdbcDataException{
		String sql = this.sqlBuilder.toString().toUpperCase();
		logger.debug("SQL语句:["+sql+"]");
		return this.jdbcTemplate.update(sql,new ValueSetter(LOBHANDLER,this.valueElements));
	}


}