package com.frameworkset.util;

import org.frameworkset.util.annotations.wraper.ColumnWraper;

public interface ColumnEditorInf<T> {
	 /**
     * Gets the property value.
     * @param fromValue
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    T getValueFromObject(ColumnWraper columnWraper,Object fromValue) ;    
    T getValueFromString(ColumnWraper columnWraper,String fromValue);
   
}
