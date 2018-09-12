package com.xphsc.easyjdbc.transform;


import com.xphsc.easyjdbc.core.metadata.DynamicEntityElement;
import com.xphsc.easyjdbc.core.metadata.DynamicFieldElement;
import com.xphsc.easyjdbc.util.EasyJdbcHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.LobHandler;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 动态RowMapper
 * Created by ${huipei.x}
 */
public class DynamicEntityRowMapper<T> implements RowMapper<T>{
	
	private final LobHandler lobHandler;
	private final DynamicEntityElement dynamicEntityElement;
	private final Class<?> dynamicEntityClass;
	private final boolean isMap;

	public DynamicEntityRowMapper(LobHandler lobHandler
			,DynamicEntityElement dynamicEntityElement,Class<?> dynamicEntityClass) {
		this.lobHandler = lobHandler;
		this.dynamicEntityElement = dynamicEntityElement;
		this.dynamicEntityClass = dynamicEntityClass;
		this.isMap = true;
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T instance = EasyJdbcHelper.newInstance(this.dynamicEntityClass);
		ResultSetMetaData rsm =rs.getMetaData();
		int col = rsm.getColumnCount();   //获得列的个数
		for (int i = 1; i <= col; i++) { 
			String columnLabel = rsm.getColumnLabel(i).toUpperCase();
			int columnType = rsm.getColumnType(i);
			DynamicFieldElement dynamicFieldElement = this.dynamicEntityElement
								.getDynamicFieldElements().get(columnLabel.toUpperCase());
			if(null == dynamicFieldElement) {
				continue;
			}
			Object value = EasyJdbcHelper.getResultValue(rs, i, columnType, dynamicFieldElement.getType());
			if(value==null) {
				continue;
			}
			String errorMsg = "实体："+this.dynamicEntityElement.getName()+" 字段："+dynamicFieldElement.getName()+" 设置值失败";
			EasyJdbcHelper.invokeMethod(instance, dynamicFieldElement.getWriteMethod(), errorMsg, value);
		}
		return instance;
	}

}