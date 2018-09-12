package com.xphsc.easyjdbc.core.metadata.resolver;




import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.StringUtil;
import java.lang.annotation.Annotation;


/**
 * Column注解解析器
 * Created by ${huipei.x}
 */
public class ColumnResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement)element;
		javax.persistence.Column column = (javax.persistence.Column) annotation;
		if (StringUtil.isNotBlank(column.name())) {
			fieldElement.setColumn(column.name());
		}
		fieldElement.setNullable(column.nullable());
		fieldElement.setUnique(column.unique());
		fieldElement.setLength(column.length());
		fieldElement.setColumnDefinition(column.columnDefinition());
		fieldElement.setInsertable(column.insertable());
		fieldElement.setUpdatable(column.updatable());
		fieldElement.setTable(column.table());
	}

}