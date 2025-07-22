/*
 * Copyright (c) 2018-2019  huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.core.metadata.resolver;



import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import java.lang.annotation.Annotation;


/**
 *  Lob Annotation parser
 * Created by ${huipei.x}
 */
public class LobResolver implements Resolver {

	@Override
	public void resolve(Element element, Annotation annotation) {
		FieldElement fieldElement = (FieldElement) element;
		fieldElement.setClob(isClob(fieldElement.getField().getType()));
		fieldElement.setBlob(isBlob(fieldElement.getField().getType()));
	}

	private boolean isClob(Class<?> type) {
		String simpleName = type.getSimpleName();
		if ("String".equals(simpleName)
				|| "Character[]".equals(simpleName)
				|| "char[]".equals(simpleName)
				|| "Clob".equals(simpleName)) {
			return true;
		}
		return false;
	}

	private boolean isBlob(Class<?> type) {
		String simpleName = type.getSimpleName();
		if ("Byte[]".equals(simpleName)
				|| "byte[]".equals(simpleName)
				|| "Blob".equals(simpleName)) {
			return true;
		}
		return false;
	}

}