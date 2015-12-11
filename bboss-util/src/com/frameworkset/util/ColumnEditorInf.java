package com.frameworkset.util;

import org.frameworkset.util.annotations.wraper.ColumnWraper;

public interface ColumnEditorInf {
	 /**
     * 查询时，表字段值转换为对象属性值，Gets the property value.将值转换为属性对应类型的值
     * @param fromValue
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    Object getValueFromObject(ColumnWraper columnWraper,Object fromValue) ;   
	 /**
     * 查询时，表字段值转换为对象属性值，Gets the property value.将值转换为属性对应类型的值
     * @param fromValue
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    Object getValueFromString(ColumnWraper columnWraper,String fromValue);
    /**
     * insert,update,delete时值转换方法，对象属性值转换为表字段值
     * @param columnWraper
     * @param fromValue
     * @return
     */
    Object toColumnValue(ColumnWraper columnWraper,Object fromValue);
    /**
     * insert,update,delete时值转换方法，对象属性值转换为表字段值
     * @param columnWraper
     * @param fromValue
     * @return
     */
    Object toColumnValue(ColumnWraper columnWraper,String fromValue);
   
}
