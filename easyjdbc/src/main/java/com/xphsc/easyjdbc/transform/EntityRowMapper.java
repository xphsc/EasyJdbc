package com.xphsc.easyjdbc.transform;


import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.EasyJdbcHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 实体RowMapper
 * Created by ${huipei.x}
 */
public class EntityRowMapper<T> implements RowMapper<T>{

	private final LobHandler lobHandler;
	private final EntityElement entityElement;
	private final Class<?> persistentClass;

	public EntityRowMapper(LobHandler lobHandler,EntityElement entityElement,Class<?> persistentClass) {
		this.persistentClass = persistentClass;
		this.entityElement = entityElement;
		this.lobHandler = lobHandler;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T instance = EasyJdbcHelper.newInstance(this.persistentClass);
		ResultSetMetaData rsm =rs.getMetaData();
		int col = rsm.getColumnCount();
		for (int i = 1; i <= col; i++) {  
			String columnLabel = rsm.getColumnLabel(i).toUpperCase();
			int columnType = rsm.getColumnType(i);
			FieldElement fieldElement = this.entityElement.getFieldElements().get(columnLabel.toUpperCase());
			if(null == fieldElement) {
				continue;
			}
			Object value = null;
			if(fieldElement.isClob()){
				value = this.lobHandler.getClobAsString(rs, i);
			} else if(fieldElement.isBlob()){
				value = this.lobHandler.getBlobAsBytes(rs, i);
			} else {
				value = EasyJdbcHelper.getResultValue(rs, i, columnType, fieldElement.getType());
			}
			if(value==null) {
				continue;
			}
			if(null == fieldElement.getWriteMethod()) {
				throw new EasyJdbcException("实体："+this.entityElement.getName()+" 字段："+fieldElement.getName()+" 没有set方法");
			}
			String errorMsg = "实体："+this.entityElement.getName()+" 字段："+fieldElement.getName()+" 设置值失败";
			EasyJdbcHelper.invokeMethod(instance, fieldElement.getWriteMethod(), errorMsg, value);
		}
		return instance;
	}

}