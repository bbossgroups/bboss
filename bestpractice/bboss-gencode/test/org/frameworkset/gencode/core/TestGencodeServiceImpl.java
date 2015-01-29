package org.frameworkset.gencode.core;

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
		gencodeService.genCode(moduleMetaInfo);
	}

}
