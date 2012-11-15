/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.sqlexecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.GetCUDResult;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.sqlexecutor.BeanVariable.Bean;
import com.frameworkset.util.ListInfo;

public class SimpleApiTest {
	@Test
	public void insertOpera()throws SQLException
	{
		SQLExecutor.delete("delete from LISTBEAN");
		ListBean lb = new ListBean();
		lb.setFieldLable("tttt");
		lb.setFieldName("testttt");
		lb.setFieldType("int");
		lb.setIsprimaryKey(false);
		lb.setRequired(true);
		lb.setSortorder("ppp");
		lb.setFieldLength(20);
		lb.setIsvalidated(6);
		
		
		//用List存放Bean，在某特定的连接池中进行crud操作
		List<ListBean> beans = new ArrayList<ListBean>();
		beans.add(lb);
		lb = new ListBean();
		lb.setFieldLable("sss");
		lb.setFieldName("ss");
		lb.setFieldType("int");
		lb.setIsprimaryKey(false);
		lb.setRequired(true);
		lb.setSortorder("ppp");
		lb.setFieldLength(20);
		lb.setIsvalidated(6);
		beans.add(lb);
		
		lb = new ListBean();
		lb.setFieldLable("sss");
		lb.setFieldName("ss556");
		lb.setFieldType("int");
		lb.setIsprimaryKey(false);
		lb.setRequired(true);
		lb.setSortorder("ppp");
		lb.setFieldLength(20);
		lb.setIsvalidated(6);
		beans.add(lb);
		
		lb = new ListBean();
		lb.setFieldLable("ddd");
		lb.setFieldName("sdds");
		lb.setFieldType("int");
		lb.setIsprimaryKey(false);
		lb.setRequired(true);
		lb.setSortorder("ppp");
		lb.setFieldLength(20);
		lb.setIsvalidated(6);
		beans.add(lb);
		String sql = "insert into LISTBEAN(ID,FIELDNAME,FIELDLABLE,FIELDTYPE,SORTORDER,ISPRIMARYKEY,REQUIRED,FIELDLENGTH,ISVALIDATED) " +
				"values(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]," +
				"#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		SQLExecutor.insertBeans("bspf",sql,beans);
		
		 
		
		SQLExecutor.insertBean("bspf", sql, lb);
		
		SQLExecutor.insertBeans("bspf", sql, beans);
		
		
		
		
		
		SQLExecutor.insertBean(sql, lb);
		
		
		
		sql ="insert into LISTBEAN(ID,FIELDNAME,FIELDLABLE,FIELDTYPE) " +
		"values(?,?,?,?)";
		SQLExecutor.insertWithDBName("bspf", sql,DBUtil.getNextPrimaryKey("bspf", "ListBean"),"insertOpreation","ttyee","int");
//		SQLExecutor.insert(sql,122,lb.getFieldName(),lb.getFieldLable(),lb.getFieldType());
		
		 
		
		
		
		
	}
	@Test
	public void arrayVariableTest() throws SQLException
	{
		/**
		 * 删除数据，数据条件由数组,FIELDNAMES，这里主要演示如果通过数组变量语法获取数据项
		 * 后台转换为预编译执行
		 */
		insertOpera();
		String[] FIELDNAMES = new String[]{"ss","testttt","sdds","insertOpreation","ss556"};
		String deleteAllsql = "delete from LISTBEAN where FIELDNAME in (#[FIELDNAMES[0]],#[FIELDNAMES[1]],#[FIELDNAMES[2]],#[FIELDNAMES[3]],#[FIELDNAMES[4]])";
		Map<String,String[]> conditions = new HashMap<String,String[]>();
		conditions.put("FIELDNAMES", FIELDNAMES);		
		SQLExecutor.deleteBean(deleteAllsql, conditions);
		
	}
	
	@Test
	public void listVariableTest() throws SQLException
	{
		/**
		 * 删除数据，数据条件由list 对象FIELDNAMES提供，这里主要演示如何通过list变量语法获取数据项
		 * 后台转换为预编译执行
		 */
		insertOpera();
		List<String> FIELDNAMES = new ArrayList<String>();
		FIELDNAMES.add("ss");
		FIELDNAMES.add("testttt");
		FIELDNAMES.add("sdds");
		FIELDNAMES.add("insertOpreation");
		FIELDNAMES.add("ss556");
		String deleteAllsql = "delete from LISTBEAN where FIELDNAME in (#[FIELDNAMES[0]],#[FIELDNAMES[1]],#[FIELDNAMES[2]],#[FIELDNAMES[3]],#[FIELDNAMES[4]])";
		Map<String,List<String> > conditions = new HashMap<String,List<String> >();
		conditions.put("FIELDNAMES", FIELDNAMES);		
		SQLExecutor.deleteBean(deleteAllsql, conditions);
		
	}
	
	@Test
	public void beanVariableTest() throws SQLException
	{
		/**
		 * 删除数据，数据条件由数组FIELDNAMES，这里主要演示如果通过bean属性引用变量语法获取数据项
		 * 后台转换为预编译执行
		 */
		insertOpera();
		BeanVariable beanvariable = new BeanVariable();
		beanvariable.setBean(new Bean());
		String deleteAllsql = "delete from LISTBEAN where FIELDNAME in (#[bean->fss],#[bean->ftestttt],#[bean->fsdds]," +
				"#[bean->finsertOpreation],#[bean->fss556])";
		SQLExecutor.deleteBean(deleteAllsql, beanvariable);
	}
	
	@Test
	public void mapVariableTest() throws SQLException
	{
		/**
		 * 删除数据，数据条件由FIELDNAMES为名称索引的map对象中，这里主要演示如果通过map变量获取数据项
		 * 后台转换为预编译执行
		 */
		insertOpera();
		Map<String,String> datas = new HashMap<String,String>();
		datas.put("sskey", "ss");
		datas.put("testtttkey", "testttt");
		datas.put("sddskey", "sdds");
		datas.put("insertOpreationkey", "insertOpreation");
		datas.put("ss556key", "ss556");
		String deleteAllsql = "delete from LISTBEAN where FIELDNAME in " +
				"(#[FIELDNAMES[sskey]],#[FIELDNAMES[testtttkey]],#[FIELDNAMES[sddskey]],#[FIELDNAMES[insertOpreationkey]]," +
				"#[FIELDNAMES[ss556key]])";
		Map conditions = new HashMap();
		conditions.put("FIELDNAMES", datas);		
		SQLExecutor.deleteBean(deleteAllsql, conditions);
	}
	
//	public void batchadd(List<TestBean> newdatas)
//	{				
//		//sql中的变量对应TestBean中的属性名称，必须要有相应的get/set方法,框架会自动转换为预编译占位符的sql语句
//		String sql = "insert into LISTBEAN(ID,FIELDNAME,FIELDLABLE,FIELDTYPE,SORTORDER," +
//				"ISPRIMARYKEY,REQUIRED,FIELDLENGTH,ISVALIDATED) " +
//				"values(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]," +
//				"#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
//        //SQLExecutor.insertBeans(sql,newdatas);//不带数据源的方法
//		SQLExecutor.insertBeans("bspf",//数据源
//		                        sql,//数据库sql语句
//		                        newdatas//批量插入的对象记录集
//		                        );
//	}
	
	@Test
	public void updateOpera() throws SQLException
	{
		//在某特定的连接池中直接crud对象
		ListBean bean = new ListBean();
		bean.setId(88);
		bean.setFieldLable("tttt");
		bean.setFieldName("test");
		bean.setFieldType("int");
		bean.setIsprimaryKey(false);
		bean.setRequired(true);
		bean.setSortorder("ppp");
		bean.setFieldLength(20);
		bean.setIsvalidated(6);
		List<ListBean> beans = new ArrayList<ListBean>();
		String sql ="";
		beans.add(bean);
		
		sql ="update LISTBEAN set FIELDNAME='yyyy' where ID=#[id]"; 
		SQLExecutor.updateBeans("bspf", sql, beans);
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBean(sql,bean);
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBean("bspf",sql,bean);
		
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBeans(sql,beans);
		
		sql = "update LISTBEAN set FIELDNAME=? where ID=?";
		SQLExecutor.update(sql, "mytest",100);
		
		sql = "update LISTBEAN set FIELDNAME=? where ID=?";
		SQLExecutor.updateWithDBName("bspf", sql, "zhansans",101);
		
		
	}
	
	/**
	 * 测试空值插入功能
	 * CREATE
    TABLE NULLNUMBER
    (
        COL1 BIGINT,
        COL2 BIGINT DEFAULT 9 NOT NULL
    )
	 */
	@Test
	public void testNULLNUMBER()
	{
		String sql = "insert into NULLNUMBER(col1,col2) " +
		"values(#[col1],#[col2])";
		
		try
		{
			NULLNUMBER number = new NULLNUMBER();
			number.setCol2(10);
			SQLExecutor.insertBean("bspf",sql,number);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 

		
	}
	
	@Test
	public void deleteOpera() throws SQLException
	{   //在特定的连接池中对数组对象进行crud
		ListBean lb = new ListBean();
		lb.setId(85);
		lb.setFieldLable("tttt");
		lb.setFieldName("testttt");
		lb.setFieldType("int");
		lb.setIsprimaryKey(false);
		lb.setRequired(true);
		lb.setSortorder("ppp");
		lb.setFieldLength(20);
		lb.setIsvalidated(6);
		ListBean lb2 = new ListBean();
		lb2.setId(15);
		lb2.setFieldName("this is lb2");
		
		List<ListBean> beans = new ArrayList<ListBean>();
		beans.add(lb);
		beans.add(lb2);
        String sql = "";
		
		sql = "delete from LISTBEAN where ID=?";
		SQLExecutor.delete(sql,68);
		
		sql = "delete from LISTBEAN where ID=?";
		SQLExecutor.deleteByKeys(sql,67);
		
		sql ="delete  from LISTBEAN where ID=#[id]"; 
		SQLExecutor.deleteBean(sql,lb);
		
		sql = "delete from LISTBEAN where ID=#[id]";
		SQLExecutor.deleteBeans(sql, beans);
		
		sql ="delete  from LISTBEAN where ID=#[id]";   
		SQLExecutor.deleteBean("bspf",sql,lb);
		
		sql ="delete  from LISTBEAN where ID=#[id]";   
		SQLExecutor.deleteBeans("bspf",sql,beans);
		
		sql = "delete from LISTBEAN where ID=?";
		SQLExecutor.deleteWithDBName("bspf", sql, 3);
		
		sql = "delete from LISTBEAN where FIELDNAME=?";
		SQLExecutor.deleteByKeysWithDBName("bspf", sql,"pppp");
		
		
	}
	@Test
	public void queryListMap() throws SQLException
	{
		String sql = "select * from LISTBEAN ";
		List<HashMap> dbBeans  =  SQLExecutor.queryListWithDBName(HashMap.class, "bspf", sql);
		System.out.println(dbBeans);
	}
	
	@Test
	public void queryMap() throws SQLException
	{
		String sql = "select * from LISTBEAN ";
		Map dbBeans  =  SQLExecutor.queryObject(HashMap.class, sql);
		System.out.println(dbBeans);
	}
	
	@Test
	public void queryTIntField() throws SQLException
	{
		String sql = "select REQUIRED from LISTBEAN ";
		int id=  SQLExecutor.queryTField(int.class, sql);
//		long id=  SQLExecutor.queryTField(long.class, "select seq_name.nextval from LISTBEAN ");
		System.out.println(id);
	}
	
	@Test
	public void queryTStringField() throws SQLException
	{
		String sql = "select REQUIRED from LISTBEAN ";
		int iid=  SQLExecutor.queryTField(int.class, sql);
		long lid=  SQLExecutor.queryTField(long.class, "select seq_name.nextval from LISTBEAN ");
		String sid=  SQLExecutor.queryTField(String.class, sql);

	}
	
	@Test
	public void queryMapListInfo() throws SQLException
	{
		String sql = "select * from LISTBEAN ";
		ListInfo dbBeans  =  SQLExecutor.queryListInfo(HashMap.class, sql, 0, 3);
		System.out.println(dbBeans);
	}
	
	
	@Test
	public void queryOprea() throws SQLException{
		List<ListBean> beans = null;
	    
		String sql ="select * from LISTBEAN where ID=?";
		
		sql = "select * from LISTBEAN where id=?";
		List<ListBean> lbs =  SQLExecutor.queryList(ListBean.class, sql,22);
		
		sql = "select * from LISTBEAN where fieldName=?";
		beans = (List<ListBean>) SQLExecutor.queryListWithDBName(ListBean.class,"bspf",sql,"testttt");
		for(int i=0;i<beans.size();i++)
		System.out.println(beans.get(i).getId());
		
		sql = "select * from LISTBEAN where fieldName=?";
		List<ListBean> dbBeans  =  (List<ListBean>) SQLExecutor.queryListWithDBName(ListBean.class, "bspf", sql, "testttt");
		for(int i=0;i<dbBeans.size();i++)
			System.out.println(dbBeans.get(i).getFieldName());
		
		sql = "select * from LISTBEAN where fieldName=? and id=?";
		ListBean bean = SQLExecutor.queryObject(ListBean.class, sql,"object",22);
		System.out.println(bean.getId());
		
		sql="select * from LISTBEAN where FIELDNAME=? or id=?";
        lbs = (List<ListBean>) SQLExecutor.queryList(ListBean.class, sql, "testttt",100);
		
		
		sql = "select FIELDNAME from LISTBEAN where ID=?";
		String lbs1 = SQLExecutor.queryField(sql,2);
		System.out.println(lbs1);
		
		sql="select FIELDNAME from LISTBEAN where  ID=?";
		String result = SQLExecutor.queryFieldWithDBName("bspf", sql, 100);
		System.out.println(result);
		
		sql = "select * from LISTBEAN where ID=?";
		ListBean lb = (ListBean)SQLExecutor.queryObjectWithDBName(ListBean.class,"bspf",sql,20);
		
		
		sql="select * from LISTBEAN where ID<? and ID>?";
		ListInfo lif = SQLExecutor.queryListInfo(ListBean.class, sql, 0, 10, 20,10);
		beans = lif.getDatas();
		for(int i=0;i<beans.size();i++)
		System.out.println(beans.get(i).getFieldName()+".......");
		
		
	     bean = new ListBean();
	    bean.setFieldName("testttt");
	    bean.setFieldLable("lisi");
	    
        sql ="select * from LISTBEAN where ID=?";
		
		
//		bean = (ListBean)SQLExecutor.queryObjectBean(ListBean.class, sql, bean);
	    
		sql ="select * from LISTBEAN where FIELDNAME=#[fieldName]";
		 result = SQLExecutor.queryFieldBean(sql, bean);
		System.out.println(result);
		result = SQLExecutor.queryFieldBeanWithDBName("bspf", sql, bean);
		System.out.println(result);
		
		beans = (List<ListBean>) SQLExecutor.queryListBean(ListBean.class, sql, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		beans = (List<ListBean>) SQLExecutor.queryListBeanWithDBName(ListBean.class, "bspf", sql, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		sql = "select * from LISTBEAN where ID>?";
		lif = SQLExecutor.queryListInfoWithDBName(ListBean.class, "bspf", sql, 0, 10,80);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getFieldName()+"^^^^^");
		
		sql = "select * from LISTBEAN where FIELDNAME=#[fieldName]";
		lif = SQLExecutor.queryListInfoBean(ListBean.class, sql, 0, 4, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		lif = SQLExecutor.queryListInfoBeanWithDBName(ListBean.class, "bspf", sql, 0, 5, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		bean = SQLExecutor.queryObjectBeanWithDBName(ListBean.class, "bspf", sql, bean);
		System.out.println(bean.getId());
		
		
		
	}
	
	@Test
	public void dynamicqueryBean() throws SQLException
	{
		 ListBean bean = new ListBean();
		    bean.setFieldName("阿斯顿飞");
		 //<property name="refresh_interval" value="10000"/>
		 List<ListBean> result = SQLExecutor.queryListBean(ListBean.class, "select *  from LISTBEAN", bean);
		 System.out.println(result.size());
		  bean.setFieldName("");
		 result = (List<ListBean>) SQLExecutor.queryListBean(ListBean.class,"select *  from LISTBEAN", bean);
		 System.out.println(result.size());
		 
		 bean.setFieldName(null);
		 result = (List<ListBean>) SQLExecutor.queryListBean(ListBean.class,"select *  from LISTBEAN", bean);
		 
		 
		 System.out.println(result.size());
		 
		 List<String> result_string =  SQLExecutor.queryListBean(String.class,"select *  from LISTBEAN", bean);
		 
		 
		 System.out.println(result_string.size());
		 
		 List<Integer> result_int =  SQLExecutor.queryListBean(Integer.class,"select *  from LISTBEAN", bean);
		 
		 
		 System.out.println(result_int.size());
		 
	}
	
	@Test
	public void dynamicquery() throws SQLException
	{
		 
		List<ListBean> result =  SQLExecutor.queryList(ListBean.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result.size());
		 
		 List<String> result_string =  SQLExecutor.queryList(String.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_string.size());
		 
		 List<Integer> result_int =  SQLExecutor.queryList(Integer.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_int.size());
		 
	}
	
	@Test
	public void dynamicqueryObject() throws SQLException
	{
		 
		ListBean result =  SQLExecutor.queryObject(ListBean.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result.getId());
		 
		 String result_string =  SQLExecutor.queryObject(String.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_string);
		 
		 int result_int =  SQLExecutor.queryObject(int.class,"select id  from LISTBEAN");
		 
		 
		 System.out.println(result_int);
		 
	}
	
	@Test
	public void rowHandlerQuery() throws SQLException{
		String sql ="";
		List<ListBean> beans = null;
		ListBean bean = new ListBean();
		ListInfo lif = new ListInfo();
		final List<ListBean> lbs = new ArrayList<ListBean>();
	    bean.setFieldName("testttt");
	    bean.setFieldLable("lisi");
		
	    sql ="select * from LISTBEAN where ID=?";
	    
	    SQLExecutor.queryByNullRowHandler(new NullRowHandler(){
			@Override
			public void handleRow(Record record) throws Exception {
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, sql, 22);
	    System.out.println(lbs.size()+"9999999");
	    
		
		sql = "select * from LISTBEAN where ID>?";
		beans = (List<ListBean>) SQLExecutor.queryListByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean lb, Record record)
					throws Exception {
				System.out.println("queryListByRowHandler test Result**:"+record.getString("fieldName"));
				
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				
			}}, ListBean.class, sql, 80);
		for(int i=0;i<lbs.size();i++)
		System.out.println(lbs.get(i).getId()+"*****");
		
		
		
		
		lbs.clear();
		System.out.println(lbs.size());
		lif = SQLExecutor.queryListInfoByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, sql, 0, 10, 20);
		System.out.println(lif.getTotalSize()+"----");
		
		
		
		

		sql = "select * from LISTBEAN where FIELDNAME=#[fieldName]";
		lbs.clear();
		beans = (List<ListBean>) SQLExecutor.queryListBeanByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, sql, bean);
		for(int i=0;i<beans.size();i++)
		System.out.println(beans.get(i).getId()+"  ggg");
		
		
		
		
		lbs.clear();
		beans = (List<ListBean>) SQLExecutor.queryListBeanWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, "bspf", sql, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId()+"  ccccccccc");
		
		
		lbs.clear();
		lif = (ListInfo) SQLExecutor.queryListInfoBeanByRowHandler(new RowHandler<ListBean>(){
			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}},ListBean.class, sql, 5, 5, bean);
		beans = lif.getDatas();
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId()+"  ddddddddddddddddddddddddd");
		
		
		lbs.clear();
		lif = SQLExecutor.queryListInfoBeanWithDBNameByRowHandler(new RowHandler<ListBean>(){
			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}},ListBean.class, "bspf",sql, 0, 5, bean);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"  ffff");
		
		sql = "select * from LISTBEAN where ID=#[id]";
		bean.setId(2);
		ListBean  lb1 =SQLExecutor.queryObjectBeanByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
				
			}}, ListBean.class, sql, bean);
		System.out.println(lb1.getFieldName());
		
		sql = "select * from LISTBEAN where ID<?";
		lbs.clear();
		lif = SQLExecutor.queryListInfoWithDBNameByRowHandler(new RowHandler(){

			@Override
			public void handleRow(Object rowValue, Record record)
					throws Exception {
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lbs.add(lb);
				lb.setFieldName(record.getString("fieldName"));
			}},ListBean.class,"bspf", sql, 0, 5, 20);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"  kkkk");
		
		
		
		beans = (List<ListBean>) SQLExecutor.queryListWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
			rowValue.setId(record.getInt("id"));
			rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, "bspf", sql, 20);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getFieldName()+"  wwwww");
		
		ListBean lb3 = SQLExecutor.queryObjectByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, sql, 20);
		System.out.println(lb3.getFieldName()+"lbbbbb");
		
		ListBean lb4 = SQLExecutor.queryObjectWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class,"bspf", sql, 20);
		System.out.println(lb4.getFieldName()+"lb4444");
		sql = "select * from LISTBEAN where ID=#[id]";
		
		ListBean lb2 = SQLExecutor.queryObjectBeanWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, "bspf", sql, bean);
		System.out.println(lb2.getId()+"++++");
	}
	
	@Test
	public void nullRowHandlerQuery() throws SQLException{
		String sql = "";
		List<ListBean> beans = null;
		ListBean b = new ListBean();
	    b.setFieldName("testttt");
	    b.setFieldLable("lisi");
	    
	    
		sql = "select * from LISTBEAN where id>?";
		beans = null;
		final List<ListBean> lbs = new ArrayList<ListBean>();
		
		ListInfo lif = SQLExecutor.queryListInfoByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
				System.out.println("queryListInfoByNullRowHandler test result:"+record.getInt("id"));
				
			}}, sql, 0, 5, 10);
		beans = (List<ListBean>)lif.getDatas();
//		for(int i=0;i<beans.size();i++)
//		  System.out.println(beans.get(i).getId()+".......");
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getFieldName()+"####");
		
		lbs.clear();
		lif =SQLExecutor.queryListInfoWithDBNameByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
				System.out.println("queryListInfoByNullRowHandler test result:"+record.getInt("id"));
				
			}},"bspf", sql, 0, 5, 10);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getFieldName()+"oooooooo");
		
		
		lbs.clear();
		SQLExecutor.queryWithDBNameByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, "bspf", sql, 80);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getFieldName()+"ppppp");
		
		sql = "select * from LISTBEAN where FIELDNAME=#[fieldName]";
		lbs.clear();
		SQLExecutor.queryBeanByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, sql, b);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"yyyyy");
		
		lbs.clear();
		SQLExecutor.queryBeanWithDBNameByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, "bspf",sql, b);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"rrrrrrr");
		
		lbs.clear();
		lif = SQLExecutor.queryListInfoBeanByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, sql, 0, 5, b);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"eeee");
		
		SQLExecutor.queryListInfoBeanWithDBNameByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record record) throws Exception {
				// TODO Auto-generated method stub
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
			}}, "bspf",sql, 0, 5, b);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"-----");
		
		
		
	}
	@Test
	public void testValidate()
	{
		try {
			SQLExecutor.queryField("select 1 as numbb from LISTBEAN");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class Timestamp
	{
		java.sql.Timestamp times;

		/**
		 * @return the times
		 */
		public java.sql.Timestamp getTimes() {
			return times;
		}

		/**
		 * @param times the times to set
		 */
		public void setTimes(java.sql.Timestamp times) {
			this.times = times;
		}
	}
	public @Test void testTimestamp() throws SQLException
	{
		String insert = "insert into tb_times(times) values(#[times])";
		Timestamp times = new Timestamp();
		times.setTimes(new java.sql.Timestamp(new Date().getTime()));
		SQLExecutor.insertBean(insert, times);
		List<Timestamp> datas = SQLExecutor.queryList(Timestamp.class,"select * from tb_times");
		System.out.println();
	}
	
	public @Test void gnt() throws SQLException
	{
		TlkzlsqlcBean bean = new TlkzlsqlcBean();
		String insertLc = "INSERT INTO DTJF.T_ZT_ZDRY_LKZLSQLC (BKSQLCBH,LKZLBH,LKZLSPR,LKZLSPDWID,LKZLSPDWMC,LKZLSPZT,LKZLSPSJ,LKZLSPYJ,LKZLSPYJSM) VALUES ("+
		"#[BKSQLCBH],#[LKZLBH],#[LKZLSPR],#[LKZLSPDWID],#[LKZLSPDWMC],#[LKZLSPZT],TO_DATE(#[LKZLSPSJ],'YYYY-MM-DD HH24:MI:SS'),#[LKZLSPYJ],#[LKZLSPYJSM])";
		SQLExecutor.insertBean(insertLc, bean);
	}
	public @Test void testETL()
	{
		try
		{
			SQLExecutor.queryField("select 1 from r_cluster");
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public @Test void testReturnObject() throws SQLException
	{
		Demo demo = new Demo();
		demo.setId("aaa");
		demo.setName("name");
		Object delret = SQLExecutor.deleteBean("delete from demo where id=#[id]", demo);
		Object ret = SQLExecutor.insertBean("insert into demo(id,name) values(#[id],#[name])", demo);
		demo.setName("newname");
		Object upret = SQLExecutor.updateBean("update demo set name=#[name] where id=#[id]", demo);
		System.out.println();
		
	}
	
	public @Test void testAutoGenalKeysReturnObject() throws SQLException
	{
		AutoKeyDemo demo = new AutoKeyDemo();
		
		demo.setName("name2");
		
		GetCUDResult ret = (GetCUDResult)SQLExecutor.insertBean("insert into demo(name) values(#[name])", demo,true);
		demo.setId((Long)ret.getKeys());
		demo.setName("newname");
		Object upret = SQLExecutor.updateBean("update demo set name=#[name] where id=#[id]", demo);
		System.out.println();
		
	}

}
