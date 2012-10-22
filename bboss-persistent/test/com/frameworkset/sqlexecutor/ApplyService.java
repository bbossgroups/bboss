package com.frameworkset.sqlexecutor;

import java.util.HashMap;

import org.junit.Test;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
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
	
	/**
	 * 
	 * @param offset
	 * @param pagesize
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public ListInfo queryMaterailListInfo(int offset, int pagesize ,PurchaseApplyCondition condition) throws Exception {
		//执行总记录查询并存入totalSize变量中，queryCountMaterialList对应一个优化后的总记录查询语句
		//condition参数保存了查询条件
		long totalSize = executor.queryObjectBean(long.class, "queryCountMaterialList", condition);
		//执行总记分页查询，queryMaterialList对应分页查询语句，通过totalsize参数从外部传入总记录数，
		//这样在分页方法内部无需执行总记录数查询操作，以便提升系统性能
		//condition参数保存了查询条件
		return executor.queryListInfoBean(ListBean.class, "queryMaterialList", offset, pagesize,totalSize ,condition);
	}
	@Test
	public void queryMaterailListInfo1() throws Exception {
//		long totalSize = executor.queryObjectBeanWithDBName(long.class,Constant.DATASOURCE_NAME_COMMON, "queryCountMaterialList", condition);
		ListInfo list = executor.queryListInfoBeanWithDBName(HashMap.class, "bspf","queryMaterialList", 0, 10,"queryCountMaterialList" ,null);
	}
}
