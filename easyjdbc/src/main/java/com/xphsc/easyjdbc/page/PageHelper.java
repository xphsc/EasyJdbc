package com.xphsc.easyjdbc.page;




/**
 * Created by ${huipei.x}
 */
public class PageHelper  {
	private static PageAutoDialect autoDialect=new PageAutoDialect();
	public static String pagination(String dialectName,String sql, int startRow, int size){
		autoDialect.initDelegateDialect(dialectName);
		return autoDialect.getDelegate().pagination(sql,startRow,size);
	}


}
