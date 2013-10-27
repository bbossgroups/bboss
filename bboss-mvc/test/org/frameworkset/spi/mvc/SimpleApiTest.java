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
package org.frameworkset.spi.mvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;

public class SimpleApiTest {
	@Test
	public void insertOpera()throws SQLException
	{
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
		
		String sql = "insert into LISTBEAN(ID,FIELDNAME,FIELDLABLE,FIELDTYPE,SORTORDER,ISPRIMARYKEY,REQUIRED,FIELDLENGTH,ISVALIDATED) " +
				"values(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]," +
				"#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		SQLExecutor.insertBeans("mysql",sql,beans);
		
		 
		
		SQLExecutor.insertBean("mysql", sql, lb);
		
		SQLExecutor.insertBeans("mysql", sql, beans);
		
		
		
		SQLExecutor.insertBean(sql, lb);
		
		
		
		sql ="insert into LISTBEAN(ID,FIELDNAME,FIELDLABLE,FIELDTYPE) " +
		"values(?,?,?,?)";
//		SQLExecutor.insert(sql,122,lb.getFieldName(),lb.getFieldLable(),lb.getFieldType());
		
		 
//		SQLExecutor.insertWithDBName("mysql", sql,101,"insertOpreation","ttyee","int");
		
		
		
	}
	
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
		SQLExecutor.updateBeans("mysql", sql, beans);
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBean(sql,bean);
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBean("mysql",sql,bean);
		
		
		sql ="update LISTBEAN set FIELDNAME=#[fieldName] where ID=#[id]"; 
		SQLExecutor.updateBeans(sql,beans);
		
		sql = "update LISTBEAN set FIELDNAME=? where ID=?";
		SQLExecutor.update(sql, "mytest",100);
		
		sql = "update LISTBEAN set FIELDNAME=? where ID=?";
		SQLExecutor.updateWithDBName("mysql", sql, "zhansans",101);
		
		
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
		SQLExecutor.deleteBean("mysql",sql,lb);
		
		sql ="delete  from LISTBEAN where ID=#[id]";   
		SQLExecutor.deleteBeans("mysql",sql,beans);
		
		sql = "delete from LISTBEAN where ID=?";
		SQLExecutor.deleteWithDBName("mysql", sql, 3);
		
		sql = "delete from LISTBEAN where FIELDNAME=?";
		SQLExecutor.deleteByKeysWithDBName("mysql", sql,"pppp");
		
		
	}
	
	
	@Test
	public void queryOprea() throws SQLException{
		List<ListBean> beans = null;
	    
		String sql ="select * from LISTBEAN where ID=?";
		
		sql = "select * from LISTBEAN where id=?";
		List<ListBean> lbs = (List<ListBean>) SQLExecutor.queryList(ListBean.class, sql,22);
		
		sql = "select * from LISTBEAN where fieldName=?";
		beans = (List<ListBean>) SQLExecutor.queryListWithDBName(ListBean.class,"mysql",sql,"testttt");
		for(int i=0;i<beans.size();i++)
		System.out.println(beans.get(i).getId());
		
		sql = "select * from LISTBEAN where fieldName=?";
		List<ListBean> dbBeans  =  (List<ListBean>) SQLExecutor.queryListWithDBName(ListBean.class, "mysql", sql, "testttt");
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
		String result = SQLExecutor.queryFieldWithDBName("mysql", sql, 100);
		System.out.println(result);
		
		sql = "select * from LISTBEAN where ID=?";
		ListBean lb = (ListBean)SQLExecutor.queryObjectWithDBName(ListBean.class,"mysql",sql,20);
		
		
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
		result = SQLExecutor.queryFieldBeanWithDBName("mysql", sql, bean);
		System.out.println(result);
		
		beans = (List<ListBean>) SQLExecutor.queryListBean(ListBean.class, sql, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		beans = (List<ListBean>) SQLExecutor.queryListBeanWithDBName(ListBean.class, "mysql", sql, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		sql = "select * from LISTBEAN where ID>?";
		lif = SQLExecutor.queryListInfoWithDBName(ListBean.class, "mysql", sql, 0, 10,80);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getFieldName()+"^^^^^");
		
		sql = "select * from LISTBEAN where FIELDNAME=#[fieldName]";
		lif = SQLExecutor.queryListInfoBean(ListBean.class, sql, 0, 4, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		lif = SQLExecutor.queryListInfoBeanWithDBName(ListBean.class, "mysql", sql, 0, 5, bean);
		for(int i=0;i<beans.size();i++)
			System.out.println(beans.get(i).getId());
		

		
		
		bean = SQLExecutor.queryObjectBeanWithDBName(ListBean.class, "mysql", sql, bean);
		System.out.println(bean.getId());
		
		
		
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
		beans = (List<ListBean>) SQLExecutor.queryListByRowHandler(new RowHandler(){

			@Override
			public void handleRow(Object rowValue, Record record)
					throws Exception {
				System.out.println("queryListByRowHandler test Result**:"+record.getString("fieldName"));
				ListBean lb = new ListBean();
				lb.setId(record.getInt("id"));
				lb.setFieldName(record.getString("fieldName"));
				lbs.add(lb);
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
			}}, ListBean.class, "mysql", sql, bean);
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
			}},ListBean.class, "mysql",sql, 0, 5, bean);
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
			}},ListBean.class,"mysql", sql, 0, 5, 20);
		for(int i=0;i<lbs.size();i++)
			System.out.println(lbs.get(i).getId()+"  kkkk");
		
		
		
		beans = (List<ListBean>) SQLExecutor.queryListWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
			rowValue.setId(record.getInt("id"));
			rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, "mysql", sql, 20);
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
			}}, ListBean.class,"mysql", sql, 20);
		System.out.println(lb4.getFieldName()+"lb4444");
		sql = "select * from LISTBEAN where ID=#[id]";
		
		ListBean lb2 = SQLExecutor.queryObjectBeanWithDBNameByRowHandler(new RowHandler<ListBean>(){

			@Override
			public void handleRow(ListBean rowValue, Record record)
					throws Exception {
				// TODO Auto-generated method stub
				rowValue.setId(record.getInt("id"));
				rowValue.setFieldName(record.getString("fieldName"));
			}}, ListBean.class, "mysql", sql, bean);
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
				
			}},"mysql", sql, 0, 5, 10);
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
			}}, "mysql", sql, 80);
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
			}}, "mysql",sql, b);
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
			}}, "mysql",sql, 0, 5, b);
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
	

}
