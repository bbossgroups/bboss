package org.frameworkset.gencode.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.frameworkset.gencode.entity.ModuleMetaInfo;
import org.junit.Test;

public class TestGencodeServiceImpl {
	@Test
	public void testGenEntityCode() throws Exception
	{
		 
		GencodeServiceImpl gencodeService = new GencodeServiceImpl();
		ModuleMetaInfo moduleMetaInfo = new ModuleMetaInfo();
		moduleMetaInfo.setTableName("td_app_bom");
		moduleMetaInfo.setModuleName("appbom");
		moduleMetaInfo.setDatasourceName("bspf");
		moduleMetaInfo.setPackagePath("org.frameworkset.demo");
		moduleMetaInfo.setServiceName("AppbomManagerService");
		moduleMetaInfo.setSourcedir("d:/sources");
		moduleMetaInfo.setIgnoreEntityFirstToken(true);
		moduleMetaInfo.setAuthor("yinbp");
		moduleMetaInfo.setCompany("sany");
		moduleMetaInfo.setVersion("v1.0");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);
		gencodeService.genCode(moduleMetaInfo);
	}

}
