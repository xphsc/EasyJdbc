package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.StringUtil;
import javax.persistence.UniqueConstraint;
import java.lang.annotation.Annotation;


/**
 * Table注解解析器
 * Created by ${huipei.x}
 */
public class TableResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		EntityElement entityElement = (EntityElement) element;
		Class<?> persistentClass = entityElement.getPersistentClass();
		Assert.isNull(persistentClass.getAnnotation(javax.persistence.MappedSuperclass.class),
				"实体：" + persistentClass.getName() + ",注解错误。 MappedSuperclass、Table两个注解不能同时用在一个类上");
		javax.persistence.Table table = (javax.persistence.Table) annotation;
		if (StringUtil.isNotBlank(table.name())) {
			entityElement.setTable(table.name());
		}
		entityElement.setCatalog(table.catalog());
		entityElement.setSchema(table.schema());
		UniqueConstraint[] uniqueConstraints = table.uniqueConstraints();
		entityElement.setUniqueConstraints(uniqueConstraints);
	}

}