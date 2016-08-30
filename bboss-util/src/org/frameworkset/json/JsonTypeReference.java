package org.frameworkset.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

 

public class JsonTypeReference<T> implements Comparable<JsonTypeReference<T>>{
//	private static String type2 = "com.fasterxml.jackson.core.type.TypeReference";
//	private static String type1 = "org.codehaus.jackson.type.TypeReference";
	 protected  Type _type; 
	 
	
	public JsonTypeReference()
	{
		Type superClass = getClass().getGenericSuperclass();
//        if (superClass instanceof Class<?>) { // sanity check, should never happen
//            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
//        }
        /* 22-Dec-2008, tatu: Not sure if this case is safe -- I suspect
         *   it is possible to make it fail?
         *   But let's deal with specific
         *   case when we know an actual use case, and thereby suitable
         *   workarounds for valid case(s) and/or error to throw
         *   on invalid one(s).
         */
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}
	public Type getType() { return _type; }
	 

 
	  @Override
	public int compareTo(JsonTypeReference<T> o) { return 0; }

}
