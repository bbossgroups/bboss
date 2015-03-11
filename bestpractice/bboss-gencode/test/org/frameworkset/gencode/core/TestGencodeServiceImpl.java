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
		moduleMetaInfo.setTableName("td_app_bom");//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setModuleName("appbom");//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName("台账");//指定模块中文名称
		moduleMetaInfo.setDatasourceName("bspf");//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath("org.frameworkset.demo");//java程序对应的包路径
		moduleMetaInfo.setServiceName("AppbomManagerService");
		moduleMetaInfo.setSourcedir("d:/sources");//生成文件存放的物理目录，如果不存在，会自动创建
		moduleMetaInfo.setIgnoreEntityFirstToken(true); //忽略表的第一个下滑线签名的token，例如表名td_app_bom中，只会保留app_bom部分，然后根据这部分来生成实体、配置文件名称
		moduleMetaInfo.setAuthor("yinbp");//程序作者
		moduleMetaInfo.setCompany("sany");//公司信息
		moduleMetaInfo.setVersion("v1.0");//版本信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);//指定日期
		
		/************以下代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等*******/
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
		/************以上代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等********/
		
		/************以下代码片段指定界面排序字段**********************************/
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
		/************以上代码片段指定界面排序字段**********************************/
		
		gencodeService.genCode(moduleMetaInfo);//执行代码生成逻辑
	}

}
