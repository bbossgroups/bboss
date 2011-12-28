package com.frameworkset.util;

public interface WrapperEditorInf<T> extends EditorInf<T>
{
	
	 /**
     * Gets the property value.
     * @param fromValue
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    T getValueFromObject(Object fromValue,Object oldvalue) ;
    
    T getValueFromString(String fromValue,Object oldvalue);
    String getStringValue();

}
