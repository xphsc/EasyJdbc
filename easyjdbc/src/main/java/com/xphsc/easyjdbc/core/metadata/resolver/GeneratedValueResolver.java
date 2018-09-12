package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.Assert;
import java.lang.annotation.Annotation;


/**
 *  GeneratedValue注解解析器
 * Created by ${huipei.x}
 */
public class GeneratedValueResolver implements Resolver {
	
	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement)element;
		EntityElement entityElement = fieldElement.getEntityElement();
		javax.persistence.GeneratedValue generatedValue = (javax.persistence.GeneratedValue) annotation;
		Assert.isTrue(fieldElement.isPrimaryKey(),
				"实体：" + entityElement.getName() + ",注解错误。 GeneratedValue只能注解在主键上");
		fieldElement.setGeneratedValue(Boolean.TRUE);
		fieldElement.setStrategy(generatedValue.strategy());
		fieldElement.setGenerator(generatedValue.generator());
	}
	
}
