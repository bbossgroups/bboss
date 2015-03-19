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
		moduleMetaInfo.setTableName("ACT_RU_TASK".toLowerCase());//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setModuleName("task");//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName("表信息管理");//指定模块中文名称
		moduleMetaInfo.setDatasourceName("bspf");//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath("org.frameworkset.demo");//java程序对应的包路径
//		moduleMetaInfo.setServiceName("AppbomManagerService");
		moduleMetaInfo.setSourcedir("d:/sources/task");//生成文件存放的物理目录，如果不存在，会自动创建
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
	
	
	@Test
	public void testGenJspsourcedirCode() throws Exception
	{
		 
		GencodeServiceImpl gencodeService = new GencodeServiceImpl();
		ModuleMetaInfo moduleMetaInfo = new ModuleMetaInfo();
		moduleMetaInfo.setTableName("ta_autho_area");//指定表名，根据表结构来生成所有的文件
		moduleMetaInfo.setPkname("ta_autho_area");//设置oracle sequence名称，用来生成表的主键,对应TABLEINFO表中TABLE_NAME字段的值
		moduleMetaInfo.setSystem("lcjf");//lcjf,系统代码，如果指定了system，那么对应的jsp页面会存放到lcjf/area目录下面，对应的mvc组件装配文件存在在/WEB-INF/conf/lcjf下面，否则jsp在/area下，mvc组件装配文件存在在/WEB-INF/conf/area下
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
	
	@Test
	public void testGentuoCode() throws Exception
	{
		 
		/**
		 * -- Create table
create table TA_FGW_XMSBK
(
  sbxmid      VARCHAR2(30) not null,
  yqid        VARCHAR2(30),
  xmgkdw      VARCHAR2(50),
  sfsbj       INTEGER default 1 not null,
  jsxzid      INTEGER,
  xmlbid      VARCHAR2(30),
  region_id   CHAR(25),
  hyid        VARCHAR2(30),
  dbxmid      VARCHAR2(30) default 0,
  sfyjbyy     CHAR(2) default '0',
  dbxm        CHAR(2) default '0',
  jjid        VARCHAR2(30),
  itemname    VARCHAR2(255),
  gsztz       NUMBER(10,2),
  ksnd        VARCHAR2(4),
  jsnd        VARCHAR2(4),
  xmdw        VARCHAR2(255),
  jsdz        VARCHAR2(255),
  lxrjfs      VARCHAR2(255),
  jsgm        VARCHAR2(2048),
  qqgzjz      VARCHAR2(1024),
  qqgzrw      VARCHAR2(1024),
  sbdw        VARCHAR2(255),
  zt          NUMBER,
  sbnd        NUMBER(4),
  sbr         VARCHAR2(255),
  sbrlxfs     VARCHAR2(255),
  sbrq        DATE,
  xmxz        INTEGER,
  tbyh        VARCHAR2(255),
  bx          NUMBER,
  tbdwid      VARCHAR2(255),
  sfbxg       CHAR(2),
  zyjsnr      VARCHAR2(2048),
  bz          VARCHAR2(1024),
  sbdwid      VARCHAR2(30),
  sshj        INTEGER default 0 not null,
  tbsj        DATE,
  xmbh        VARCHAR2(100),
  bw          NUMBER(14,4),
  dj          NUMBER(14,4),
  tzlx        VARCHAR2(30),
  bndscztz    NUMBER(10,2),
  region_name VARCHAR2(60),
  tjjdid      VARCHAR2(30),
  lsgxid      VARCHAR2(30),
  csdw        VARCHAR2(50)
)
;
-- Add comments to the table 
comment on table TA_FGW_XMSBK
  is '记录项目申报的信息';
-- Add comments to the columns 
comment on column TA_FGW_XMSBK.sbxmid
  is '申报项目ID';
comment on column TA_FGW_XMSBK.yqid
  is '园区id';
comment on column TA_FGW_XMSBK.xmgkdw
  is '项目归口单位';
comment on column TA_FGW_XMSBK.sfsbj
  is '是否市本级';
comment on column TA_FGW_XMSBK.jsxzid
  is '建设性质ID';
comment on column TA_FGW_XMSBK.xmlbid
  is '项目类别ID';
comment on column TA_FGW_XMSBK.region_id
  is '区域码';
comment on column TA_FGW_XMSBK.hyid
  is '行业ID';
comment on column TA_FGW_XMSBK.dbxmid
  is '打包项目ID';
comment on column TA_FGW_XMSBK.sfyjbyy
  is '是否已经被引用';
comment on column TA_FGW_XMSBK.dbxm
  is '打包后项目标志';
comment on column TA_FGW_XMSBK.jjid
  is '简介ID';
comment on column TA_FGW_XMSBK.itemname
  is '项目名称';
comment on column TA_FGW_XMSBK.gsztz
  is '估算总投资';
comment on column TA_FGW_XMSBK.ksnd
  is '开始年';
comment on column TA_FGW_XMSBK.jsnd
  is '结束年';
comment on column TA_FGW_XMSBK.xmdw
  is '项目业主单位';
comment on column TA_FGW_XMSBK.jsdz
  is '建设地址';
comment on column TA_FGW_XMSBK.lxrjfs
  is '联系人及方式';
comment on column TA_FGW_XMSBK.jsgm
  is '建设规模及新增生产能力';
comment on column TA_FGW_XMSBK.qqgzjz
  is '项目前期工作进展情况';
comment on column TA_FGW_XMSBK.qqgzrw
  is '年前期工作任务和目标';
comment on column TA_FGW_XMSBK.sbdw
  is '申报单位';
comment on column TA_FGW_XMSBK.zt
  is '状态';
comment on column TA_FGW_XMSBK.sbnd
  is '申报年度';
comment on column TA_FGW_XMSBK.sbr
  is '申报人';
comment on column TA_FGW_XMSBK.sbrlxfs
  is '申报人联系方式';
comment on column TA_FGW_XMSBK.sbrq
  is '申报日期';
comment on column TA_FGW_XMSBK.xmxz
  is '项目性质';
comment on column TA_FGW_XMSBK.tbyh
  is '填报用户';
comment on column TA_FGW_XMSBK.bx
  is '排序';
comment on column TA_FGW_XMSBK.tbdwid
  is '填报单位id';
comment on column TA_FGW_XMSBK.sfbxg
  is '通过oracle触发器，用自带方法进行比较 是否被修改';
comment on column TA_FGW_XMSBK.zyjsnr
  is '主要建设内容';
comment on column TA_FGW_XMSBK.bz
  is '备注';
comment on column TA_FGW_XMSBK.sbdwid
  is '申报单位ID';
comment on column TA_FGW_XMSBK.sshj
  is '审核通过会插入一条新的记录；用于区分待审核的数据是否已经审核。
';
comment on column TA_FGW_XMSBK.tbsj
  is '填报时间';
comment on column TA_FGW_XMSBK.xmbh
  is '项目编号';
comment on column TA_FGW_XMSBK.tjjdid
  is '推进阶段主键IDTJJDID';
comment on column TA_FGW_XMSBK.lsgxid
  is '隶属关系主键IDLSGXID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TA_FGW_XMSBK
  add constraint PK_TA_FGW_XMSBK primary key (SBXMID)
  ;

-- Create/Recreate check constraints 
alter table TA_FGW_XMSBK
  add constraint CKC_DBXM_TA_FGW_X
  check (DBXM is null or (DBXM in ('0','1')));
alter table TA_FGW_XMSBK
  add constraint CKC_SFSBJ_TA_FGW_X
  check (SFSBJ between 0 and 1 and SFSBJ in (0,1));
alter table TA_FGW_XMSBK
  add constraint CKC_SFYJBYY_TA_FGW_X
  check (SFYJBYY is null or (SFYJBYY in ('0','1')));
alter table TA_FGW_XMSBK
  add constraint CKC_SSHJ_TA_FGW_X
  check (SSHJ between 0 and 1 and SSHJ in (0,1));
alter table TA_FGW_XMSBK
  add constraint CKC_XMXZ_TA_FGW_X
  check (XMXZ is null or (XMXZ in (0,1,2)));
alter table TA_FGW_XMSBK
  add constraint CKC_ZT_TA_FGW_X
  check (ZT is null or (ZT in (0,1,2,3,4,5)));

		 */
		GencodeServiceImpl gencodeService = new GencodeServiceImpl();
		ModuleMetaInfo moduleMetaInfo = new ModuleMetaInfo();
		moduleMetaInfo.setTableName("TA_FGW_XMSBK".toLowerCase());//指定表名，根据表结构来生成所有的文件
//		moduleMetaInfo.setPkname("ta_autho_area");//设置oracle sequence名称，用来生成表的主键,对应TABLEINFO表中TABLE_NAME字段的值
		moduleMetaInfo.setModuleName("area");//指定模块名称，源码和配置文件都会存放在相应模块名称的目录下面
		moduleMetaInfo.setModuleCNName("行政区划");//指定模块中文名称
		moduleMetaInfo.setDatasourceName("bspf");//指定数据源名称，在poolman.xml文件中配置
		moduleMetaInfo.setPackagePath("com.tuo.core");//java程序对应的包路径
		//moduleMetaInfo.setServiceName("AreaManagerService");
		moduleMetaInfo.setSourcedir("d:/sources/xmsbk");//生成文件存放的物理目录，如果不存在，会自动创建
		moduleMetaInfo.setIgnoreEntityFirstToken(true); //忽略表的第一个下滑线签名的token，例如表名td_app_bom中，只会保留app_bom部分，然后根据这部分来生成实体、配置文件名称
		moduleMetaInfo.setAuthor("liy");//程序作者
		moduleMetaInfo.setCompany("tuo");//公司信息
		moduleMetaInfo.setVersion("v1.0");//版本信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		moduleMetaInfo.setDate(date);//指定日期
		gencodeService.setTheme("default");
		
		/************以下代码片段指定界面查询字段，以及查询条件组合方式、是否是模糊查询等*******/
		ConditionField bm = new ConditionField();
		bm.setColumnname("xmgkdw");
		bm.setLike(true);
		bm.setOr(true);
		gencodeService.addCondition(bm);
		
		
		ConditionField bm1 = new ConditionField();
		bm1.setColumnname("jsdz");
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
		id.setColumnname("jjid");
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
