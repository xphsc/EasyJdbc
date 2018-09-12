package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import java.lang.annotation.Annotation;


/**
 * 注解解析器接口
 * Created by ${huipei.x}
 */
public interface Resolver {
	
	public void resolve(Element element, Annotation annotation);

}
