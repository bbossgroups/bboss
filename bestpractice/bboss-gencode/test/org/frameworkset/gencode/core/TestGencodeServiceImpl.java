package org.frameworkset.gencode.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.frameworkset.gencode.entity.ConditionField;
import org.frameworkset.gencode.entity.ModuleMetaInfo;
import org.frameworkset.gencode.entity.SortField;
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
		ConditionField bm = new ConditionField();
		
		bm.setFieldName("bm");
		bm.setLike(true);
		bm.setOr(true);
		
		gencodeService.addCondition(bm);
		ConditionField softLevel = new ConditionField();
		
		softLevel.setLike(false);
		softLevel.setOr(false);
		softLevel.setFieldName("softLevel");
	
		gencodeService.addCondition(softLevel);
		
		SortField id = new SortField();
		id.setColumnName("ID");
		id.setFieldName("id");
		id.setDesc(true);
		gencodeService.addSortField(id);
		SortField sbm = new SortField();
		sbm.setColumnName("BM");
		sbm.setFieldName("bm");
		sbm.setDesc(false);
		gencodeService.addSortField(sbm);
		gencodeService.genCode(moduleMetaInfo);
	}

}
