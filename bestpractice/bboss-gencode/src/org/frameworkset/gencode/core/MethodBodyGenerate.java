package org.frameworkset.gencode.core;

import org.frameworkset.gencode.entity.Method;

public interface MethodBodyGenerate {
	/**
	 * 
	 * @param method
	 * @param entityName
	 * @param paramName
	 * @param encodecharset
	 * @param exception
	 * @param methodtype 1:service component 2:action component
	 * @throws Exception
	 */
	public void gen(Method method, String entityName,String entityVarName,String paramName,String encodecharset,String exception,int componentType)  throws Exception;

}
