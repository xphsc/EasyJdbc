package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.Assert;
import java.lang.annotation.Annotation;


/**
 *  Id注解解析器
 * Created by ${huipei.x}
 */
public class IdResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement)element;
		EntityElement entityElement = fieldElement.getEntityElement();
		Assert.isNull(entityElement.getPrimaryKey()
				, "实体：" + fieldElement.getName() + "只能有一个主键");
		fieldElement.setPrimaryKey(Boolean.TRUE);
		entityElement.setPrimaryKey(fieldElement);
	}

}