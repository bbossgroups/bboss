package com.frameworkset.sqlexecutor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.frameworkset.common.poolman.ConfigPagineOrderby;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.PagineOrderby;
import com.frameworkset.common.poolman.PlainPagineOrderby;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;
/**
 * 
 * <p>Title: ApplyService.java</p>
 *
 * <p>Description: 分页查询功能实例（针对新的分页接口进行测试）
 * 3.6.0之后的版本ConfigSQLExecutor/SQLExecutor/PreparedDBUtil三个持久层组件中增加了一组分页接口，
 * 这组接口和之前的分页接口的区别是增加了一个totalsize参数，也就是说可以通过totalsize参数从外部传入总记录数，
 * 这样在分页方法内部无需执行总记录数查询操作，以便提升系统性能
 * 
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2012-10-18 下午5:06:43
 * @author biaoping.yin
 * @version 1.0
 */
public class ApplyService {

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/sqlexecutor/purchaseApply.xml");
	
	/*******************************以bean方式传递查询条件开始*******************************/
	@Test
	public void queryMaterailListInfoFirstStyleBean() throws Exception {
		
		//执行分页查询，queryMaterialList对应分页查询语句，
		//根据sql语句在分页方法内部执行总记录数查询操作，这种风格使用简单，效率相对较低
		//condition参数保存了查询条件
		int offset = 0; int pagesize = 10;
		PurchaseApplyCondition condition = new PurchaseApplyCondition();
		condition.setId("3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		ListInfo datas = executor.queryListInfoBean(HashMap.class, "queryMaterialList", offset, pagesize,condition);
		return ;
	}
	@Test
	public void queryMaterailListInfoSecondStyleBean() throws Exception {
		//执行总记录查询并存入totalSize变量中，queryCountMaterialList对应一个优化后的总记录查询语句
		//condition参数保存了查询条件
		int offset = 0; int pagesize = 10;
		PurchaseApplyCondition condition = new PurchaseApplyCondition();
		condition.setId("3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		long totalSize = executor.queryObjectBean(long.class, "queryCountMaterialList", condition);
		//执行总记分页查询，queryMaterialList对应分页查询语句，通过totalsize参数从外部传入总记录数，
		//这样在分页方法内部无需执行总记录数查询操作，以便提升系统性能，这种风格使用简单，效率相对第一种风格较高，但是要额外配置总记录数查询sql
		//condition参数保存了查询条件
		ListInfo datas = executor.queryListInfoBean(HashMap.class, "queryMaterialList", offset, pagesize,totalSize ,condition);
		return;
	}
	
	
	public @Test void queryMaterailListInfoThirdStyleBean() throws Exception {
		//根据sql语句和外部传入的总记录数sql语句进行分页，这种风格使用简单，效率最高，但是要额外配置总记录数查询sql
		PurchaseApplyCondition condition = new PurchaseApplyCondition();
		condition.setId("3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		ListInfo list = executor.queryListInfoBeanWithDBName(HashMap.class, "bspf","queryMaterialList", 0, 10,"queryCountMaterialList" ,condition);
		return ;
	}
	/*******************************以bean方式传递查询条件结束*******************************/
	
	/*******************************以传统绑定变量方式传递查询条件开始*******************************/
	public @Test void queryMaterailListInfoFirstStyle( ) throws Exception {
		
		//执行分页查询，queryMaterialList对应分页查询语句，
		//根据sql语句在分页方法内部执行总记录数查询操作，这种风格使用简单，效率相对较低
		//condition参数保存了查询条件
		ListInfo list = executor.queryListInfo(HashMap.class, "queryMaterialListbindParam", 0, 10, "3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		return;
	}
	
	public @Test void queryMaterailListInfoSecondStyle( ) throws Exception {
		//执行总记录查询并存入totalSize变量中，queryCountMaterialList对应一个优化后的总记录查询语句
		//condition参数保存了查询条件
		long totalSize = executor.queryObject(long.class, "queryCountMaterialListbindParam", "3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		//执行总记分页查询，queryMaterialList对应分页查询语句，通过totalsize参数从外部传入总记录数，
		//这样在分页方法内部无需执行总记录数查询操作，以便提升系统性能，这种风格使用简单，效率相对第一种风格较高，但是要额外配置总记录数查询sql
		//condition参数保存了查询条件
		ListInfo datas = executor.queryListInfoWithTotalsize(HashMap.class, "queryMaterialListbindParam", 0, 10,totalSize , "3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		return;
	}
	
	
	public @Test void queryMaterailListInfoThirdStyle() throws Exception {
		//根据sql语句和外部传入的总记录数sql语句进行分页，这种风格使用简单，效率最高，但是要额外配置总记录数查询sql
		ListInfo list = executor.queryListInfoWithDBName2ndTotalsizesql(HashMap.class, "bspf","queryMaterialListbindParam", 0, 10,"queryCountMaterialListbindParam", "3952ce4f-fce7-4f9e-a92b-81ebdcbe57ed");
		return ;
	}
	public @Test void testoraclerownumoverorderby() throws Exception {
		testsqlinfoorderby("oracle");
	}
	public @Test void testmysqlrownumoverorderby() throws Exception {
		testsqlinfoorderby("mysql");
	}
	public @Test void testderbyrownumoverorderby() throws Exception {
		testsqlinfoorderby("derby");
	}
	public @Test void testsqliterownumoverorderby() throws Exception {
		testsqliteorderby("sqlite");
	}
	
	public @Test void testdb2rownumoverorderby() throws Exception {
		testsqlinfoorderby("db2");
	}
	public @Test void testpostgresrownumoverorderby() throws Exception {
		testsqlinfoorderby("postgres");
	}
	public @Test void testmssqlrownumoverorderby() throws Exception {
		testsqlinfoorderby("mssql");
	}
	public void testsqlinfoorderby(String dbname) throws Exception {
		//根据sql语句和外部传入的总记录数sql语句进行分页，这种风格使用简单，效率最高，但是要额外配置总记录数查询sql
		ListInfo list = executor.queryListInfoWithDBName (HashMap.class, dbname,"testsqlinfo", 0, 10,new PlainPagineOrderby("order by bm"));
		Map params = new HashMap();
		params.put("app_name_en", "%C%");
		list = executor.queryListInfoBeanWithDBName (HashMap.class, dbname,"ROW_NUMBERquery", 0, 10,new PlainPagineOrderby("order by bm",params));
		StringBuilder orderby = new StringBuilder();
		orderby.append(" #if($sortKey && !$sortKey.equals(\"\"))")
		.append(" order by $sortKey ")
		.append(" #if($sortDESC )")
		.append("  	desc ")
		.append(" #else")
		.append(" 	asc")
		.append(" #end")
		.append(" #else")
		.append(" order by bm ")
		.append(" #end");
		list = executor.queryListInfoBeanWithDBName (HashMap.class, dbname,"ROW_NUMBERquery", 0, 10,new PagineOrderby(orderby.toString(),params));
		params.put("sortKey", "id");
		params.put("sortDESC", true);
		list = executor.queryListInfoBeanWithDBName (HashMap.class, dbname,"ROW_NUMBERquery", 0, 10,new ConfigPagineOrderby("ROW_NUMBERquery_orderby",params));
		
		
		list = SQLExecutor.queryListInfoWithDBName (HashMap.class, dbname,"select * from TD_APP_BOM", 0, 10,new PlainPagineOrderby("order by bm"));
		StringBuilder testsqlinfoorderby = new StringBuilder();
		testsqlinfoorderby.append("select * from TD_APP_BOM where 1=1")
				.append("	#if($bm && !$bm.equals(\"\"))")
				.append("	and bm = #[bm]")
				.append("	#end")
				.append("	#if($app_name_en && !$app_name_en.equals(\"\"))")
				.append("		and app_name_en like #[app_name_en]")
				.append("	#end")
				.append("	#if($app_name && !$app_name.equals(\"\"))")
				.append("		and app_name like #[app_name]")
				.append(" #end")
				.append("		#if($soft_level && !$soft_level.equals(\"\"))")
				.append("		and soft_level=#[soft_level]")
				.append("	#end")
				.append("	#if($state && !$state.equals(\"\"))")
				.append("		and state=#[state]")
				.append("		#end")
				.append("			#if($rd_type && !$rd_type.equals(\"\"))")
				.append("	and rd_type=#[rd_type]")
				.append("	#end");
		 
		list = SQLExecutor.queryListInfoBeanWithDBName (HashMap.class, dbname,testsqlinfoorderby.toString(), 0, 10,new PlainPagineOrderby("order by bm",params));
		
		list = SQLExecutor.queryListInfoBeanWithDBName (HashMap.class, dbname,testsqlinfoorderby.toString(), 0, 10,new PagineOrderby(orderby.toString(),params));
	 
		
		return ;
	}
	
	public void testsqliteorderby(String dbname) throws Exception {
		//根据sql语句和外部传入的总记录数sql语句进行分页，这种风格使用简单，效率最高，但是要额外配置总记录数查询sql
		ListInfo list = null;
		Map params = new HashMap();
		params.put("moudleName", "%t%");
		 
		StringBuilder orderby = new StringBuilder();
		orderby.append(" #if($sortKey && !$sortKey.equals(\"\"))")
		.append(" order by $sortKey ")
		.append(" #if($sortDESC )")
		.append("  	desc ")
		.append(" #else")
		.append(" 	asc")
		.append(" #end")
		.append(" #else")
		.append(" order by moudleName ")
		.append(" #end");
	 
		
		
		list = SQLExecutor.queryListInfoWithDBName (HashMap.class, dbname,"select * from BBOSS_GENCODE", 0, 3,new PlainPagineOrderby("order by moudleName"));
		StringBuilder testsqlinfoorderby = new StringBuilder();
		testsqlinfoorderby.append("select * from BBOSS_GENCODE where 1=1")
				.append("	#if($DBNAME && !$DBNAME.equals(\"\"))")
				.append("	and DBNAME = #[DBNAME]")
				.append("	#end")
				.append("	#if($moudleName && !$moudleName.equals(\"\"))")
				.append("		and moudleName like #[moudleName]")
				.append("	#end")
				;
		 
		list = SQLExecutor.queryListInfoBeanWithDBName (HashMap.class, dbname,testsqlinfoorderby.toString(), 0, 3,new PlainPagineOrderby("order by moudleName",params));
		params.put("sortKey", "id");
		params.put("sortDESC", true);
		list = SQLExecutor.queryListInfoBeanWithDBName (HashMap.class, dbname,testsqlinfoorderby.toString(), 0, 3,new PagineOrderby(orderby.toString(),params));
	 
		
		return ;
	}
//	#if($sortKey && !$sortKey.equals(""))
//	  	order by $sortKey 
//	  	#if($sortDESC )
//		  	desc
//		#else
//		 	asc
//		#end	
//	#else
	 	
//	#end
	/*******************************以传统绑定变量方式传递查询条件结束*******************************/
}
