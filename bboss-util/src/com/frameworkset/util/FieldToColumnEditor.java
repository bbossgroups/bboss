package com.frameworkset.util;

import org.frameworkset.util.annotations.wraper.ColumnWraper;

public abstract class FieldToColumnEditor  implements ColumnEditorInf  {

	public FieldToColumnEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValueFromObject(ColumnWraper columnWraper, Object fromValue) {
		// TODO Auto-generated method stub
		return fromValue;
	}

	@Override
	public Object getValueFromString(ColumnWraper columnWraper, String fromValue) {
		// TODO Auto-generated method stub
		return fromValue;
	}

	@Override
	public abstract Object toColumnValue(ColumnWraper columnWraper, Object fromValue) ;

	@Override
	public abstract Object toColumnValue(ColumnWraper columnWraper, String fromValue) ;

}
