package org.frameworkset.gencode.core;

import java.io.File;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class GenMVCConf extends AbstractGencode{
	public GenMVCConf(GencodeServiceImpl gencodeService)
	{
		super(gencodeService);
	}
	public void gen()
	{
		File conf = gencodeService.getMvcconf();
		if(conf.exists())
		{
			conf.delete();
		}
		try {
			conf.createNewFile();
			 Template conftempalte = VelocityUtil.getTemplate("gencode/conf/ioc.vm");
			 VelocityContext context = new VelocityContext();
			
			 
			 context.put("company", gencodeService.getModuleMetaInfo().getCompany());
			 context.put("gendate", gencodeService.getModuleMetaInfo().getDate());
			 context.put("author", gencodeService.getModuleMetaInfo().getAuthor());
			 context.put("version", gencodeService.getModuleMetaInfo().getVersion());
			 context.put("sqlfile", gencodeService.getSqlfilepath());
			 context.put("serviceClass", gencodeService.getServiceClass());
			 context.put("controlClass", gencodeService.getControlClass());
			 
			 context.put("moduleCNName", gencodeService.getModuleMetaInfo().getModuleCNName());
			 context.put("moduleName", gencodeService.getModuleMetaInfo().getModuleName());
			 context.put("entityVarName", gencodeService.getEntityParamName());
			 context.put("entityName", gencodeService.getEntityName());
			
			 gencodeService.writFile(context,conftempalte,conf,gencodeService.getModuleMetaInfo().getEncodecharset());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
