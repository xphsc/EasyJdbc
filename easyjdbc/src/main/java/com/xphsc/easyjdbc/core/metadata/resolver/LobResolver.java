package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import java.lang.annotation.Annotation;


/**
 *  Lob注解解析器
 * Created by ${huipei.x}
 */
public class LobResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement)element;
		fieldElement.setClob(isClob(fieldElement.getField().getType()));
		fieldElement.setBlob(isBlob(fieldElement.getField().getType()));
	}
	
	private boolean isClob(Class<?> type){
		String simpleName = type.getSimpleName();
		if("String".equals(simpleName)
				||"Character[]".equals(simpleName)
				||"char[]".equals(simpleName)
				||"Clob".equals(simpleName)){
			return true;
		}
		return false;	
	}
	
	private boolean isBlob(Class<?> type){
		String simpleName = type.getSimpleName();
		if("Byte[]".equals(simpleName)
				||"byte[]".equals(simpleName)
				||"Blob".equals(simpleName)){
			return true;
		}
		return false;	
	}

}