package org.frameworkset.gencode.core;

import org.frameworkset.gencode.entity.Method;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class IndexMethodBodyGenerate implements MethodBodyGenerate {

	@Override
	public void gen(Method method, String entityName, String entityVarName,
			String paramName, String encodecharset, String exception,
			int componentType, GencodeServiceImpl gencodeService)
			throws Exception {
		Template addmethodbodytempalte = VelocityUtil.getTemplate("gencode/java/body/index.vm");
		 VelocityContext context = new VelocityContext();
		 
		 
		 
		 String body = GencodeServiceImpl.writetostring(context,addmethodbodytempalte,encodecharset);
		 method.setBody(body);

	}

}
