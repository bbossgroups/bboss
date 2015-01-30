package org.frameworkset.gencode.core;

import org.frameworkset.gencode.entity.Method;

public interface MethodBodyGenerate {
	public void gen(Method method, String entityName,String paramName,String encodecharset,String exception)  throws Exception;

}
