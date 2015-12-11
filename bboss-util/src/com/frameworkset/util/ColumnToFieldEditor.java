package com.frameworkset.util;

import org.frameworkset.util.annotations.wraper.ColumnWraper;

public  abstract class ColumnToFieldEditor  implements ColumnEditorInf  {

	public ColumnToFieldEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract Object getValueFromObject(ColumnWraper columnWraper, Object fromValue) ;

	@Override
	public abstract Object getValueFromString(ColumnWraper columnWraper, String fromValue) ;

	@Override
	public Object toColumnValue(ColumnWraper columnWraper, Object fromValue) {
		// TODO Auto-generated method stub
		return  fromValue;
	}

	@Override
	public Object toColumnValue(ColumnWraper columnWraper, String fromValue) {
		// TODO Auto-generated method stub
		return  fromValue;
	}

}
