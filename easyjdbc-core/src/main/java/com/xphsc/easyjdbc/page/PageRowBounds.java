/*
 * Copyright (c) 2018 huipei.x
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
package com.xphsc.easyjdbc.page;




/**
 * Created by ${huipei.x}
 */
public class PageRowBounds {

	private static PageAutoDialect autoDialect;

	public static String pagination(String dialectName,String sql, int startRow, int size){
		autoDialect=new PageAutoDialect();
		autoDialect.initDelegateDialect(dialectName);
		autoDialect.clearDelegate();
		return autoDialect.getDelegate().pagination(sql,startRow,size);
	}


}
