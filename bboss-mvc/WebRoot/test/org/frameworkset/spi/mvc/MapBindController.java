/**
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.frameworkset.util.annotations.MapKey;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;


/**
 * <p>MapBindController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-5-28
 * @author biaoping.yin
 * @version 1.0
 */
public class MapBindController
{
	private List<ListBean> convertMaptoList(Map<String,ListBean> beans)
	{
		List<ListBean> listbeans = new ArrayList<ListBean> ();
		Iterator<Map.Entry<String, ListBean>> ddd = beans.entrySet().iterator();
		while(ddd.hasNext())
		{
			Map.Entry<String, ListBean> entry = ddd.next();
			listbeans.add(entry.getValue());
		}
		return listbeans;
		
	}
	/**
	 * 
	 * @param beans
	 * @return
	 */
	public String stringarraytoMap(@MapKey("fieldName") Map<String,ListBean> beans, ModelMap model,
			@RequestParam(name = "fieldName")
			List fieldNames) {
		String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME," + "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
				+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
				+ "ISVALIDATED" + ")" + "VALUES"
				+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
				+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();
			SQLExecutor.delete("delete from LISTBEAN");
			List temp =  convertMaptoList( beans);
			SQLExecutor.insertBeans(sql, temp);
			tm.commit();
			model.addAttribute("datas", temp);
			model.addAttribute("fieldNames", fieldNames);
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:stringarraytoMap";
	}

	public String showstringarraytoMap(ModelMap model) {
		List<ListBean> beans = null;
		try {
			beans = (List<ListBean>) SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "path:showstringarraytoMap";
	}

	public String showlist(ModelMap model) {
		List<ListBean> beans = null;
		try {
			beans = (List<ListBean>) SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:showlist";
	}

	public String showbean(ModelMap model, @RequestParam(name = "id") int id) {
		ListBean bean = null;
		try {
			bean = SQLExecutor.queryObject(ListBean.class,
					"select * from LISTBEAN where id=?", id);
			model.addAttribute("datas", bean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:showbean";
	}

	public String delete(ModelMap model, @RequestParam(name = "id") int id) {
		List beans = null;
		try {
			SQLExecutor.delete("delete from LISTBEAN where id=?", id);
			beans = SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:delete";
	}

	public String deletebatch(ModelMap model,
			@RequestParam(name = "CK") int[] ids) {
		List beans = null;
		try {
			if (ids != null && ids.length > 0) {

				// SQLExecutor.deleteByKeys("delete from LISTBEAN where id=?",
				// ids[0],ids[1]);
				SQLExecutor
						.deleteByKeys("delete from LISTBEAN where id=?", ids);
			}
			beans = SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:deletebatch";
	}

	public String update(ModelMap model, ListBean bean) {
		List beans = null;
		try {
			SQLExecutor.updateBean("UPDATE" + " LISTBEAN " + "SET "
					+ "FIELDNAME = #[fieldName], "
					+ "FIELDLABLE = #[fieldLable], "
					+ "FIELDTYPE = #[fieldType], "
					+ "SORTORDER = #[sortorder], "
					+ "ISPRIMARYKEY = #[isprimaryKey], "
					+ "REQUIRED = #[required], "
					+ "FIELDLENGTH = #[fieldLength], "
					+ "ISVALIDATED = #[isvalidated] " + "WHERE "
					+ "ID = #[id] ", bean); 

			beans = SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:update";
	}

	public String updatebatch(ModelMap model, Map<String,ListBean> updatebeans) {
		List beans = null;
		try {
			SQLExecutor.updateBeans("UPDATE" + "LISTBEAN " + "SET "
					+ "FIELDNAME = #[fieldName], "
					+ "FIELDLABLE = #[fieldLable], "
					+ "FIELDTYPE = #[fieldType], "
					+ "SORTORDER = #[sortorder], "
					+ "ISPRIMARYKEY = #[isprimaryKey], "
					+ "REQUIRED = #[required], "
					+ "FIELDLENGTH = #[fieldLength], "
					+ "ISVALIDATED = #[isvalidated] " + "WHERE "
					+ "ID = #[id] ", this.convertMaptoList(updatebeans));
			beans = SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:updatebatch";
	}

	public String mapbean(@MapKey("fieldName") Map<String,ListBean> beans, ModelMap model) {
		String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME,"
				+ "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
				+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
				+ "ISVALIDATED" + ")" + "VALUES"
				+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
				+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		TransactionManager tm = new TransactionManager();
		try {
			if(beans != null)
			{
				List temp =  convertMaptoList( beans);
				
				tm.begin();
				SQLExecutor.delete("delete from LISTBEAN");
				SQLExecutor.insertBeans(sql, temp);
				tm.commit();
				model.addAttribute("datas", temp);
			}
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:mapbean";
	}

}
