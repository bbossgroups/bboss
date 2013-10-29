package org.frameworkset.spi.mvc;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.RollbackException;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * <p>Title: PathController.java</p> 
 * <p>Description: 测试path:类型跳转配置，对应的配置文件为bboss-path.xml</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-3-31 下午12:57:04
 * @author biaoping.yin
 * @version 1.0
 */
public class PathController {
	/**
	 * 
	 * @param beans
	 * @return
	 */
	public String stringarraytoList(List<ListBean> beans, ModelMap model,
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
			SQLExecutor.insertBeans(sql, beans);
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
		//"/databind/stringarraytoList" 
		return "path:stringarraytoList-ok";
	}

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
//		return "/databind/stringarraytoList";
		return "path:showstringarraytoList-ok";
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

//		return "/databind/table";
		return "path:showlist-ok";
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

//		return "/databind/tableinfo";
		return "path:showbean-ok";
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

//		return "/databind/table";
		return "path:delete-ok";
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

//		return "/databind/table";
		return "path:deletebatch-ok";
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

//		return "/databind/table";
		return "path:update-ok";
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

//		return "/databind/table";
		return "path:updatebatch-ok";
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

//		return "/databind/table";
		return "path:listbean-ok";
	}
	

}
