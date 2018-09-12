package com.xphsc.easyjdbc.executor;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.transform.setter.ValueBatchSetter;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.ValueElement;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.EasyJdbcHelper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.LinkedList;
import java.util.List;


/**
 *   批量更新执行器
 * Created by ${huipei.x}
 */
public class BatchUpdateExecutor extends AbstractExecutor<int[]> {

	private final LinkedList persistents =new LinkedList();
	private final SQL sqlBuilder = SQL.BUILD();
	private LinkedList<LinkedList<ValueElement>> batchValueElements;
	
	public BatchUpdateExecutor(JdbcTemplate jdbcTemplate, List<?> persistents) {
		super(jdbcTemplate);
		this.persistents.addAll(persistents);
	}

	@Override
	public void prepare() {
		Class<?> persistentClass = this.persistents.get(0).getClass();
		this.checkEntity(persistentClass);
		EntityElement entityElement = ElementResolver.resolve(persistentClass);
		this.batchValueElements =new LinkedList();
		this.sqlBuilder.UPDATE(entityElement.getTable());
		for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
			if(fieldElement.isTransientField()) {
				continue;
			}
			if(fieldElement.isPrimaryKey()) {
				continue;
			}
			this.sqlBuilder.SET(fieldElement.getColumn() + " = ?");
		}
		this.sqlBuilder.WHERE(entityElement.getPrimaryKey().getColumn() + " = ?");
		for (Object persistent : persistents) {
			LinkedList<ValueElement> valueElements = new LinkedList();
			Object primaryKeyValue = null;
			for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
				if(fieldElement.isTransientField()) {
					continue;
				}
				if (fieldElement.isPrimaryKey()) {
					primaryKeyValue = EasyJdbcHelper.invokeMethod(persistent, fieldElement.getReadMethod()
									, "实体："+entityElement.getName()+" 主键："+fieldElement.getName()+" 获取值失败");
					Assert.notNull(primaryKeyValue, "实体:" + entityElement.getName() + ", 主键不能为空");
					continue;
				}
				Object value = EasyJdbcHelper.invokeMethod(persistent, fieldElement.getReadMethod()
						, "实体：" + entityElement.getName() + " 字段：" + fieldElement.getName() + " 获取值失败");
				valueElements.add(new ValueElement(value,fieldElement.isClob(),fieldElement.isBlob()));
			}
			valueElements.add(new ValueElement(primaryKeyValue,Boolean.FALSE,Boolean.FALSE));
			this.batchValueElements.add(valueElements);
		}
	}

	@Override
	protected int[] doExecute() throws JdbcDataException {
		String sql = this.sqlBuilder.toString().toUpperCase();
		logger.debug("SQL语句:["+sql+"]");
		return this.jdbcTemplate.batchUpdate(sql,new ValueBatchSetter(LOBHANDLER,this.batchValueElements));
	}



}