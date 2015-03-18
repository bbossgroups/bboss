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
		moduleMetaInfo.setTableName("ACT_RU_TASK");//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setModuleName("task");//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName("表信息管理");//指定模块中文名称
		moduleMetaInfo.setDatasourceName("bspf");//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath("org.frameworkset.demo");//java程序对应的包路径
//		moduleMetaInfo.setServiceName("AppbomManagerService");
		moduleMetaInfo.setSourcedir("d:/sources/tabileinfo");//生成文件存放的物理目录，如果不存在，会自动创建
		moduleMetaInfo.setIgnoreEntityFirstToken(true); //忽略表的第一个下滑线签名的token，例如表名td_app_bom中，只会保留app_bom部分，然后根据这部分来生成实体、配置文件名称
		moduleMetaInfo.setAuthor("yinbp");//程序作者
		moduleMetaInfo.setCompany("bboss");//公司信息
		moduleMetaInfo.setVersion("v1.0");//版本信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);//指定日期
		gencodeService.setTheme("default");
		
		/************以下代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等*******/
		ConditionField bm = new ConditionField();
		
		bm.setColumnname("TASK_DEF_KEY_");
		bm.setLike(true);
		bm.setOr(true);
		
		gencodeService.addCondition(bm);
		ConditionField softLevel = new ConditionField();
		
		softLevel.setLike(false);
		softLevel.setOr(false);
		softLevel.setColumnname("OWNER_");
	
		gencodeService.addCondition(softLevel);
		/************以上代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等********/
		
		/************以下代码片段指定界面排序字段**********************************/
		SortField id = new SortField();
		
		id.setColumnname("TASK_DEF_KEY_");
		id.setDesc(true);
		gencodeService.addSortField(id);
		SortField sbm = new SortField();
		sbm.setColumnname("OWNER_");
		sbm.setDesc(false);
		gencodeService.addSortField(sbm);
		/************以上代码片段指定界面排序字段**********************************/
		
		gencodeService.genCode(moduleMetaInfo);//执行代码生成逻辑
	}
	@Test
	public void testGenOraclePrimayKeyEntityCode() throws Exception
	{
		 /**
		  * -- Create table
create table TA_AUTHO_AREA
(
  AREA_ID          NUMBER not null,
  PARENT_AREA_CODE VARCHAR2(12) not null,
  AREA_NAME        VARCHAR2(20) not null,
  AREA_CODE        VARCHAR2(12) not null,
  SORT_NUM         NUMBER
);

-- Add comments to the columns 
comment on column TA_AUTHO_AREA.AREA_ID
  is '行政区划ID';
comment on column TA_AUTHO_AREA.PARENT_AREA_CODE
  is '父行政区划编码';
comment on column TA_AUTHO_AREA.AREA_NAME
  is '行政区划名称';
comment on column TA_AUTHO_AREA.AREA_CODE
  is '行政区划编码';
comment on column TA_AUTHO_AREA.SORT_NUM
  is '排序号';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TA_AUTHO_AREA
  add constraint PK_TA_AUTHO_AREA primary key (AREA_ID);
		  */
		GencodeServiceImpl gencodeService = new GencodeServiceImpl();
		ModuleMetaInfo moduleMetaInfo = new ModuleMetaInfo();
		moduleMetaInfo.setTableName("ta_autho_area");//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setPkname("ta_autho_area");//设置oracle sequence名称，用来生成表的主键,对应TABLEINFO表中TABLE_NAME字段的值
		moduleMetaInfo.setModuleName("area");//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName("行政区划");//指定模块中文名称
		moduleMetaInfo.setDatasourceName("bspf");//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath("com.yg.core");//java程序对应的包路径
		//moduleMetaInfo.setServiceName("AreaManagerService");
		moduleMetaInfo.setSourcedir("d:/sources/area");//生成文件存放的物理目录，如果不存在，会自动创建
		moduleMetaInfo.setIgnoreEntityFirstToken(true); //忽略表的第一个下滑线签名的token，例如表名td_app_bom中，只会保留app_bom部分，然后根据这部分来生成实体、配置文件名称
		moduleMetaInfo.setAuthor("jinzhen");//程序作者
		moduleMetaInfo.setCompany("yuanguo");//公司信息
		moduleMetaInfo.setVersion("v1.0");//版本信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);//指定日期
		gencodeService.setTheme("default");
		
		/************以下代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等*******/
		ConditionField bm = new ConditionField();
		bm.setColumnname("AREA_NAME");
		bm.setLike(true);
		bm.setOr(true);
		gencodeService.addCondition(bm);
		
		
		ConditionField bm1 = new ConditionField();
		bm1.setColumnname("AREA_CODE");
		bm1.setLike(true);
		bm1.setOr(true);
		gencodeService.addCondition(bm1);

		
		
//		ConditionField softLevel = new ConditionField();
//		softLevel.setLike(false);
//		softLevel.setOr(false);
//		softLevel.setFieldName("user_type");
//		gencodeService.addCondition(softLevel);
		/************以上代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等********/
		
		/************以下代码片段指定界面排序字段**********************************/
		SortField id = new SortField();
		id.setColumnname("SORT_NUM");
		id.setDesc(true);
		id.setDefaultSortField(true);
		gencodeService.addSortField(id);
		
//		SortField sbm = new SortField();
//		sbm.setFieldName("register_time");
//		sbm.setDesc(false);
//		gencodeService.addSortField(sbm);
		/************以上代码片段指定界面排序字段**********************************/
		
		gencodeService.genCode(moduleMetaInfo);//执行代码生成逻辑
	}
}
