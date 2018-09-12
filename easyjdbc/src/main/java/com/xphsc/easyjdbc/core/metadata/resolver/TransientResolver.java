package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import java.lang.annotation.Annotation;


/**
 *  Transient注解解析器
 * Created by ${huipei.x}
 */
public class TransientResolver implements Resolver {
	@Override
	public void resolve(Element element, Annotation annotation) {
		
		FieldElement fieldElement = (FieldElement)element;
		fieldElement.setTransientField(Boolean.TRUE);
	}
	
}