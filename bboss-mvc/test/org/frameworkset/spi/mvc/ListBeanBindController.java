package org.frameworkset.spi.mvc;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.RollbackException;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;

public class ListBeanBindController {
	private ConfigSQLExecutor executor;

	/**
	 * 
	 * @param beans
	 * @return
	 */
	public String stringarraytoList(List<ListBean> beans, ModelMap model,
			@RequestParam(name = "fieldName")
			List fieldNames) {
		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();
			executor.delete("stringarraytoList_delete");
			executor.insertBeans("stringarraytoList_insert", beans);
			tm.commit();
			model.addAttribute("datas", beans);
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

		return "/databind/stringarraytoList";
	}
	
//	/**
//	 * 
//	 * @param beans
//	 * @return
//	 */
//	public String savedatas(List<ListBean> beans) {
//		TransactionManager tm = new TransactionManager();
//		try {
//			tm.begin();
//			executor.delete("stringarraytoList_delete");
//			executor.insertBeans("stringarraytoList_insert", beans);
//			tm.commit();			
//		} catch (Exception e) {
//			try {tm.rollback();} catch (RollbackException e1) {}			
//		}
//		return "/databind/stringarraytoList";
//	}
//	
//	
//	/**
//	 * 
//	 * @param beans
//	 * @return
//	 */
//	public String savedatas(List<ListBean> beans) {
//		testservice.savedatas( beans);
//		return "/databind/stringarraytoList";
//	}
//	
	
	

	public String showstringarraytoList(ModelMap model) {
		List<ListBean> beans = null;
		try {
			beans = (List<ListBean>) SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/databind/stringarraytoList";
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

		return "/databind/table";
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

		return "/databind/tableinfo";
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

		return "/databind/table";
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

		return "/databind/table";
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

		return "/databind/table";
	}

	public String updatebatch(ModelMap model, List<ListBean> updatebeans) {
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
					+ "ID = #[id] ", updatebeans);
			beans = SQLExecutor.queryList(ListBean.class,
					"select * from LISTBEAN");
			model.addAttribute("datas", beans);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/databind/table";
	}

	public String listbean(List<ListBean> beans, ModelMap model) {
		String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME,"
				+ "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
				+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
				+ "ISVALIDATED" + ")" + "VALUES"
				+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
				+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
		TransactionManager tm = new TransactionManager();
		try {

			tm.begin();
			SQLExecutor.delete("delete from LISTBEAN");
			SQLExecutor.insertBeans(sql, beans);
			tm.commit();
			model.addAttribute("datas", beans);
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

		return "/databind/table";
	}

	
	public void setExecutor(ConfigSQLExecutor executor)
	{
	
		this.executor = executor;
	}

}
