package org.frameworkset.gencode.core;

import org.frameworkset.gencode.entity.Method;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class GetMethodBodyGenerate implements MethodBodyGenerate {

	@Override
	public void gen(Method method, String entityName,String entityVarName,String paramName,String encodecharset,String exception,int componentType,GencodeServiceImpl gencodeService) throws Exception {
		 Template addmethodbodytempalte = VelocityUtil.getTemplate("gencode/java/body/getmethodbody.vm");
		 VelocityContext context = new VelocityContext();
		 
		 
		 context.put("entityName", entityName);
		 context.put("paramName", paramName);
		 context.put("exception", exception);
		 context.put("componentType", componentType);
		 context.put("entityVarName", entityVarName);
		 context.put("serviceParamName", gencodeService.getServiceParamName());
		 String body = GencodeServiceImpl.writetostring(context,addmethodbodytempalte,encodecharset);
		 method.setBody(body);

	}

}
