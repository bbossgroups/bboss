package org.frameworkset.gencode.core;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.util.VelocityUtil;

public class GenPagineSQL  extends AbstractGencode {
	private SQL sql;
	public GenPagineSQL(GencodeServiceImpl gencodeService,SQL sql) {
		super(gencodeService);
		this.sql = sql;
		
	}

	
	public void gen()
	{
		
		try {
			 
			 Template conftempalte = VelocityUtil.getTemplate("/gencode/conf/paginesql.vm");
			 VelocityContext context = new VelocityContext();
			
			 
			 context.put("conditions", gencodeService.getConditions());
			 context.put("sorts", gencodeService.getSortFields());
			 context.put("tableName", gencodeService.getModuleMetaInfo().getTableName());
			 
			 sql.setSql(gencodeService.writetostring(  context,conftempalte,gencodeService.getModuleMetaInfo().getEncodecharset()));
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
