package org.frameworkset.gencode.core.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.frameworkset.gencode.core.AbstractGencode;
import org.frameworkset.gencode.core.GencodeServiceImpl;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VelocityUtil;

public class GenI8N   extends AbstractGencode {
	private boolean isZh;
	public GenI8N(GencodeServiceImpl gencodeService,boolean isZh) {
		super(gencodeService);
		this.isZh = isZh;
		// TODO Auto-generated constructor stub
	}

	
	public void gen()
	{
		File conf = isZh?gencodeService.getI18nzh_CN():gencodeService.getI18nen_US();
		if(conf.exists())
		{
			conf.delete();
		}
		try {
			conf.createNewFile();
			 Template conftempalte = VelocityUtil.getTemplate("/gencode/conf/i18n.vm");
			 VelocityContext context = new VelocityContext();
			
			 
			 context.put("company", gencodeService.getModuleMetaInfo().getCompany());
			 context.put("gendate", gencodeService.getModuleMetaInfo().getDate());
			 context.put("author", gencodeService.getModuleMetaInfo().getAuthor());
			 context.put("version", gencodeService.getModuleMetaInfo().getVersion());
			 
			 context.put("fields", gencodeService.getAllfields());
			 context.put("isZh", isZh);
			 context.put("moduleCNName", gencodeService.getModuleMetaInfo().getModuleCNName());
			 
			 context.put("namespacei18n", gencodeService.getNamespacei18n());
			 
			
			
			 gencodeService.writFile(context,conftempalte,conf,gencodeService.getModuleMetaInfo().getEncodecharset());
			 
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String s ="台账管理";
		
	      System.out.println(SimpleStringUtil.ascii2native((SimpleStringUtil.native2ascii(  s ))));
	}
	

}
