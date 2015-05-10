package org.frameworkset.gencode.core;

import java.io.File;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class GenReadme  extends AbstractGencode{
	public GenReadme(GencodeServiceImpl gencodeService)
	{
		super(gencodeService);
	}
	public void gen()
	{
		File conf = gencodeService.getReadme();
		if(conf.exists())
		{
			conf.delete();
		}
		try {
			conf.createNewFile();
			 Template conftempalte = VelocityUtil.getTemplate("gencode/conf/readme.vm");
			 VelocityContext context = new VelocityContext();			 
			 context.put("moduleCNName", gencodeService.getModuleMetaInfo().getModuleCNName());
			 context.put("moduleName", gencodeService.getModuleMetaInfo().getModuleName());
			 context.put("entityVarName", gencodeService.getEntityParamName());
			 context.put("entityName", gencodeService.getEntityName());
			 context.put("relativePath", gencodeService.getRelativePath());			 
			 context.put("wsclassinf", gencodeService.getWsclassinf());
			 context.put("servicePort", gencodeService.getServicePort());
			 if(gencodeService.getModuleMetaInfo().getSystem() != null)
			 {
				 context.put("system", gencodeService.getModuleMetaInfo().getSystem());
			 }
			 else
			 {
				 context.put("system", "");
			 }
			 context.put("genI18n", gencodeService.isGenI18n());
			 
			 gencodeService.writFile(context,conftempalte,conf,gencodeService.getModuleMetaInfo().getEncodecharset());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
